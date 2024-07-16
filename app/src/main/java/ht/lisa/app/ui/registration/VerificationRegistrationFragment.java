package ht.lisa.app.ui.registration;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.Device;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.response.BaseResponse;
import ht.lisa.app.model.response.BindDeviceResponse;
import ht.lisa.app.model.response.ConfirmDeviceResponse;
import ht.lisa.app.model.response.UserInfoResponse;
import ht.lisa.app.ui.component.VerificationCodeEditView;
import ht.lisa.app.util.Constants;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.FragmentUtil;
import ht.lisa.app.util.HashUtil;
import ht.lisa.app.util.RxUtil;
import ht.lisa.app.util.Settings;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextUtil;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class VerificationRegistrationFragment extends BaseRegistrationFragment implements VerificationCodeEditView.OnCodeEnterCompleteListener {

    private static final int START_TIME = 60000;

    @BindView(R.id.verification_registration_code_edit_text)
    VerificationCodeEditView verificationRegistrationCodeEditText;
    @BindView(R.id.verification_registration_resend_text)
    TextView verificationRegistrationResendText;
    @BindView(R.id.verification_registration_error_text)
    TextView verificationRegistrationErrorText;
    @BindView(R.id.verification_registration_button)
    Button verificationRegistrationButton;

    private CountDownTimer countDownTimer;
    private int delay = 0;

    private Disposable timerDisposable;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verification_registration, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        verificationRegistrationCodeEditText.setErrorTextView(verificationRegistrationErrorText);
        verificationRegistrationCodeEditText.setOnCodeEnterCompleteListener(this);
        startCountDownTimer(START_TIME);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timerDisposable != null) {
            timerDisposable.dispose();
            timerDisposable = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        verificationRegistrationCodeEditText.clearEditTexts();
        verificationRegistrationCodeEditText.setFocusOnFirstEmptyPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharedPreferencesUtil.getBindDeviceRequestTime() > 0 && (SharedPreferencesUtil.getBindDeviceRequestTime() + START_TIME) > DateTimeUtil.getCurrentTime()) {
            startCountDownTimer((SharedPreferencesUtil.getBindDeviceRequestTime() + START_TIME) - DateTimeUtil.getCurrentTime());
        }
    }

    private void startCountDownTimer(long startTime) {
        String resendText = getStringFromResource(R.string.re_send_code_in) + DateTimeUtil.getSimpleDateFormatTime(startTime, getActivity());
        verificationRegistrationResendText.setText(resendText);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(startTime, DateTimeUtil.SECOND) {

            public void onTick(long millisUntilFinished) {
                if (getContext() != null) {
                    String resendText = getStringFromResource(R.string.re_send_code_in) + DateTimeUtil.getSimpleDateFormatTime(millisUntilFinished, getActivity());
                    verificationRegistrationResendText.setText(resendText);
                }
            }

            public void onFinish() {
                setClickableTexts();
                SharedPreferencesUtil.setBindDeviceRequestTime(-1);
            }
        }.start();
    }

    private void setClickableTexts() {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                registrationPresenter.bindDevice(new Device(SharedPreferencesUtil.getPhone(), Constants.DEVICE_TYPE, SharedPreferencesUtil.getDeviceId()), getContext());
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getColorFromResource(R.color.reSendCode));
            }
        };
        String clickHereText = getStringFromResource(R.string.click_here) + " " + getStringFromResource(R.string.re_send_code);
        verificationRegistrationResendText.setText(clickHereText);
        TextUtil.createClickableSpan(verificationRegistrationResendText, verificationRegistrationResendText.getText().length() - getStringFromResource(R.string.re_send_code).length(), clickableSpan);
    }

    @Override
    public void getData(Object object) {
        if (object instanceof BindDeviceResponse) {
            BindDeviceResponse response = (BindDeviceResponse) object;
            SharedPreferencesUtil.setBindDeviceRequestTime(DateTimeUtil.getCurrentTime());
            startCountDownTimer(START_TIME);
            verificationRegistrationCodeEditText.clearEditTexts();
            SharedPreferencesUtil.setSalt(response.getSalt());

            checkDelay(object);
        } else if (object instanceof ConfirmDeviceResponse) {
            ConfirmDeviceResponse response = (ConfirmDeviceResponse) object;
            if (response.getState() == 0) {
                SharedPreferencesUtil.setBindDeviceRequestTime(-1);
                SharedPreferencesUtil.setSalt("");
                registrationPresenter.getUserInfo(SharedPreferencesUtil.getTemporaryToken(), new Phone(SharedPreferencesUtil.getPhone()));
            } else {
                verificationRegistrationCodeEditText.clearEditTexts();
                verificationRegistrationCodeEditText.checkErrorState(true);
                changeButtonState(verificationRegistrationButton, false);

                checkDelay(object);
            }
        } else if (object instanceof UserInfoResponse) {
            UserInfoResponse response = (UserInfoResponse) object;
         /*   if (response.getPinActive() == 1) {
                SharedPreferencesUtil.setToken(SharedPreferencesUtil.getTemporaryToken());
                SharedPreferencesUtil.setTemporaryToken("");
            }*/
            LisaApp.getInstance().authorize(SharedPreferencesUtil.getTemporaryToken());
            SharedPreferencesUtil.setTemporaryToken("");

            SharedPreferencesUtil.setSecretWord(response.getSecretActive() == 1 ? Settings.SECRET_WORD : "");
            showNextFragment(BaseRegistrationFragment.newInstance(TermsRegistrationFragment.class, null));

            checkDelay(object);
        }
    }

    private void checkDelay(Object object) {
        if (object instanceof BaseResponse) {
            if (((BaseResponse) object).getState() == 6) {
                this.delay = ((BaseResponse) object).getDelay();
                showDelayMessage(delay);
                timerDisposable = RxUtil.repeatConsumer(1000, new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        VerificationRegistrationFragment.this.delay--;
                        if (VerificationRegistrationFragment.this.delay == 0) {
                            if (timerDisposable != null) {
                                timerDisposable.dispose();
                                timerDisposable = null;
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }


    @OnClick(R.id.back_arrow)
    void onBackArrowClick() {
        FragmentUtil.replaceFragment(getFragmentManager(), new MobileRegistrationFragment(), false);
        SharedPreferencesUtil.setSalt("");
    }

    @Override
    public void onCodeEnterComplete() {
        changeButtonState(verificationRegistrationButton, true);
    }

    @Override
    public void onCodeChange() {
        changeButtonState(verificationRegistrationButton, false);
    }

    @OnClick(R.id.verification_registration_button)
    void onRegistrationButtonClick() {
        if (delay > 0) {
            showDelayMessage(delay);
            return;
        }
        String token = HashUtil.hashStringWithSha256(HashUtil.hashStringWithSha256(verificationRegistrationCodeEditText.getVerificationCode()) + SharedPreferencesUtil.getSalt() + SharedPreferencesUtil.getDeviceId());
        SharedPreferencesUtil.setTemporaryToken(token);
        registrationPresenter.confirmDevice(token, new Phone(SharedPreferencesUtil.getPhone()));
    }
}
