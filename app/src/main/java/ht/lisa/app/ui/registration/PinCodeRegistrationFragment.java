package ht.lisa.app.ui.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Guideline;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.Pin;
import ht.lisa.app.model.PinChange;
import ht.lisa.app.model.response.BaseResponse;
import ht.lisa.app.model.response.PinResponse;
import ht.lisa.app.model.response.UserInfoResponse;
import ht.lisa.app.ui.component.PinView;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.userprofile.UserProfileMainFragment;
import ht.lisa.app.util.Analytics;
import ht.lisa.app.util.BiometricUtil;
import ht.lisa.app.util.FragmentUtil;
import ht.lisa.app.util.OnFragmentVisibleListener;
import ht.lisa.app.util.RxUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextUtil;
import ht.lisa.app.util.ViewUtil;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static ht.lisa.app.ui.registration.SettingsFragment.IS_FROM_SETTINGS;


public class PinCodeRegistrationFragment extends BaseRegistrationFragment implements PinView.OnPinEnteredListener {

    public static final String SCREEN_NAME = "EnterPinScreen";

    public static final String PIN_CHANGED = "pinChanged";
    private static final int PIN_ATTEMPTS = 3;

    private static final int CREATE_PIN = 0;
    private static final int CONFIRM_NEW_PIN = 1;
    private static final int ENTER_EXISTING_PIN = 3;
    private static final int ENTER_CURRENT_PIN = 4;
    private static final int ENTER_NEW_PIN = 5;

    @BindView(R.id.pincode_registration_button)
    Button pincodeRegistrationButton;
    @BindView(R.id.pincode_registration_title)
    TextView pinCodeRegistrationTitle;
    @BindView(R.id.pincode_registration_message)
    TextView pinCodeRegistrationMessage;
    @BindView(R.id.pincode_registration_middle_guideline)
    Guideline pinCodeRegistrationMiddleGuideline;
    @BindView(R.id.pincode_registration_card_bottom_guideline)
    Guideline pinCodeRegistrationCardBottomGuideline;
    @BindView(R.id.error_pin_code_text)
    TextView errorPinCodeText;
    @BindView(R.id.pinView)
    PinView pinView;
    @BindView(R.id.back_arrow)
    ImageButton backArrow;
    @BindView(R.id.pincode_registration_existing_pin_info)
    ImageView pinCodeRegistrationExistingPinInfo;
    @BindView(R.id.sign_out)
    CardView signOut;

    private boolean isExistingPinCode;
    private int attempts;
    private String currentPin;
    private boolean isFromSettings;
    private int screenName;
    private OnFragmentVisibleListener onFragmentVisibleListener;


    private int delay = 0;

    private Disposable timerDisposable;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pin_code_registration, parent, false);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pinView.setOnPinEnteredListener(this);
        attempts = PIN_ATTEMPTS;
        if (isExistingPinCode && SharedPreferencesUtil.isBiometricAuthenticated() && BiometricUtil.isSensorAvialable(getContext())) {
            startBiometricLogin();
        }
        if (getArguments() != null) {
            isFromSettings = getArguments().getBoolean(IS_FROM_SETTINGS);
        }
        pinView.setPinEnabled(false);
        if (SharedPreferencesUtil.getToken() != null && !SharedPreferencesUtil.getToken().isEmpty()) {
            registrationPresenter.getUserInfo(null, new Phone(SharedPreferencesUtil.getPhone()));
        } else {
            setDataToViews();
        }

        if (getArguments() != null) {
            backArrow.setVisibility(getArguments().getBoolean(RegistrationActivity.FROM_BASE_FRAGMENT_KEY) ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        pinView.clear();
        pinView.showKeyBoard();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            onFragmentVisibleListener = (OnFragmentVisibleListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (onFragmentVisibleListener != null) {
            onFragmentVisibleListener.onFragmentShowListener(UserProfileMainFragment.class.getSimpleName(), true);
        }
    }

    private void setDataToViews() {
        if (isFromSettings) {
            pinCodeRegistrationTitle.setText(getStringFromResource(R.string.enter_current_pin));
            pinCodeRegistrationMessage.setText(getStringFromResource(R.string.sollicitudin));
        } else {
            pinCodeRegistrationTitle.setText(isExistingPinCode ? getStringFromResource(R.string.enter_existing_pin) : getStringFromResource(R.string.create_a_pin_code));
            pinCodeRegistrationMessage.setText(isExistingPinCode ? getStringFromResource(R.string.enter_existing_pin_message) : getStringFromResource(R.string.create_your_secured_pin_code_to_hold_n_your_account_safe));
            pinCodeRegistrationExistingPinInfo.setVisibility(isExistingPinCode ? View.VISIBLE : View.GONE);
        }

    }

    private void startBiometricLogin() {
        BiometricUtil biometricUtil = new BiometricUtil(getContext(), this);
        biometricUtil.startBiometricManager();
    }

    private void onPinCodeCheck() {
        hideProgress();
        pinView.showKeyBoard();
        if (attempts == 0) {
            setClickableTexts();
            attempts = PIN_ATTEMPTS;
        } else {
            attempts--;
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
                        PinCodeRegistrationFragment.this.delay--;
                        if (PinCodeRegistrationFragment.this.delay == 0) {
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

    private void showPinCodeCorrectness(int state) {
        errorPinCodeText.setVisibility(state != -1 ? View.GONE : View.VISIBLE);
        if (state != -3) {
            String errorText = state == -2 ? getStringFromResource(R.string.pin_code_identical) :
                    getStringFromResource(R.string.invalid_pincode_you_have) + attempts + getStringFromResource(attempts == 1 ? R.string.attempt : R.string.attempts);
            errorPinCodeText.setText(errorText);
            errorPinCodeText.setTextColor(getColorFromResource(R.color.red));
        }
        ViewUtil.changeGuidelinePercent(pinCodeRegistrationCardBottomGuideline, state != -1 ? 0.8f : 0.69f);
        pincodeRegistrationButton.setVisibility(state != -1 ? View.VISIBLE : View.GONE);
        pinView.clear();
        pinView.setFieldsError(state != 0);
    }

    private void setClickableTexts() {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                showForgotPinDialog();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getColorFromResource(R.color.reSendCode));
            }
        };
        pinView.setPinEnabled(false);
        pinView.hideKeyBoard();
        String errorText = getStringFromResource(R.string.attempts_over) + getStringFromResource(R.string.forgot_pin);
        errorPinCodeText.setText(errorText);
        errorPinCodeText.setTextColor(getColorFromResource(R.color.grey));
        TextUtil.createClickableSpan(errorPinCodeText, errorPinCodeText.getText().length() - getStringFromResource(R.string.forgot_pin).length(), clickableSpan);
    }

    private void showForgotPinDialog() {
        if (getFragmentManager() == null) return;
        ForgotPinDialog.newInstance().show(getFragmentManager(), ForgotPinDialog.class.getSimpleName());
    }

    @Override
    public void getData(Object object) {
        if (object instanceof UserInfoResponse) {
            UserInfoResponse response = (UserInfoResponse) object;
            if (response.getState() == 0) {
                isExistingPinCode = response.getPinActive() == 1;
            } else if (response.getState() == 2) {
                isExistingPinCode = true;
            } else {
                checkDelay(object);
            }
            setDataToViews();
            pinView.setPinEnabled(true);
            screenName = isFromSettings ? ENTER_CURRENT_PIN : isExistingPinCode ? ENTER_EXISTING_PIN : CREATE_PIN;
        } else if (object instanceof PinResponse) {
            PinResponse response = (PinResponse) object;
            showPinCodeCorrectness(response.getState());
            if (response.getState() == 0) {
                switch (screenName) {
                    case CREATE_PIN:
                        pinView.clear();
                        pinCodeRegistrationTitle.setText(getStringFromResource(R.string.confirm_pin_code));
                        backArrow.setVisibility(View.GONE);
                        screenName = CONFIRM_NEW_PIN;
                        break;

                    case CONFIRM_NEW_PIN:
                        backArrow.setVisibility(View.GONE);
                        if ((SharedPreferencesUtil.getSecretWord() == null || SharedPreferencesUtil.getSecretWord().isEmpty()) && SharedPreferencesUtil.isGetStarted()) {
                            showNextFragment(BaseRegistrationFragment.newInstance(SecurityWordRegistrationFragment.class, null));
                        } else {
                            startActivity(new Intent(getContext(), MainActivity.class));
                            calculateAppSpeed();
                        }
                        break;

                    case ENTER_EXISTING_PIN:
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        calculateAppSpeed();
                        break;


                    case ENTER_CURRENT_PIN:
                        pinCodeRegistrationExistingPinInfo.setVisibility(View.GONE);
                        pinCodeRegistrationTitle.setText(getStringFromResource(R.string.enter_new_pin));
                        pinCodeRegistrationMessage.setText(getStringFromResource(R.string.create_new_lisa_pin));
                        pincodeRegistrationButton.setText(getStringFromResource(R.string.save_changes));
                        screenName = ENTER_NEW_PIN;
                        break;

                    case ENTER_NEW_PIN:
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(PIN_CHANGED, true);
                        showNextFragment(BaseRegistrationFragment.newInstance(SettingsFragment.class, bundle));
                        break;

                }
            } else if (response.getState() == -1) {
                onPinCodeCheck();
            } else {
                checkDelay(object);
            }
        }
    }

    private void calculateAppSpeed() {
        if (LisaApp.getInstance().isNeedToCalculateAppSpeed()) {
            Bundle b = new Bundle();
            b.putLong(Analytics.SPEED, (new Date().getTime() - LisaApp.getInstance().getSessionStartTime()) / 1000);
            FirebaseAnalytics.getInstance(getContext())
                    .logEvent(Analytics.APP_SPEED, b);
            LisaApp.getInstance().setNeedToCalculateAppSpeed(false);
        }
    }

    @OnClick({R.id.pincode_registration_button, R.id.pincode_registration_existing_pin_info, R.id.sign_out})
    void onPinCodeButtonClick(View view) {
        switch (view.getId()) {
            case R.id.pincode_registration_button:
                if (!pinView.isCurrentPinFull()) return;
                if (delay > 0) {
                    showDelayMessage(delay);
                    return;
                }
                switch (screenName) {
                    case CREATE_PIN:
                        registrationPresenter.createPin(null, new Pin(SharedPreferencesUtil.getPhone(), pinView.getEnteredPin()), pinView);
                        break;

                    case ENTER_EXISTING_PIN:
                    case CONFIRM_NEW_PIN:
                    case ENTER_CURRENT_PIN:
                        registrationPresenter.authorizePin(new Pin(SharedPreferencesUtil.getPhone(), pinView.getEnteredPin()), pinView);
                        currentPin = pinView.getEnteredPin();
                        break;

                    case ENTER_NEW_PIN:
                        if (currentPin != null && !currentPin.isEmpty()) {
                            registrationPresenter.changePin(new PinChange(SharedPreferencesUtil.getPhone(), currentPin, pinView.getEnteredPin()), pinView);
                        }
                        break;

                }
                break;

            case R.id.pincode_registration_existing_pin_info:
                if (getActivity() == null) return;
                if (getFragmentManager() != null) {
                    InfoDialog.newInstance(getStringFromResource(R.string.existing_pin), getStringFromResource(R.string.the_existing_lisa_pin_is_a))
                            .show(getFragmentManager(), InfoDialog.class.getSimpleName());
                }
                break;

            case R.id.sign_out:
                if (getContext() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                    builder.setMessage(R.string.are_you_sure_you_want_to_sign_out)
                            .setCancelable(true)
                            .setPositiveButton(R.string.yes,
                                    (dialog, id) -> {
                                        SharedPreferencesUtil.setLisaSoundNeeded(true);
                                        SharedPreferencesUtil.clearToken();
                                        SharedPreferencesUtil.setNotNeedToShowBdayAlert(false);
                                        SharedPreferencesUtil.setNotNeedToShowProfileDialog(false);
                                        SharedPreferencesUtil.setGameGuideSet(null);
                                        SharedPreferencesUtil.setTickets(null);
                                        FragmentUtil.replaceFragment(getFragmentManager(), new ChooseLanguageRegistrationFragment(), false);
                                        dialog.cancel();
                                        LisaApp.getInstance().unbindOneSignal();
                                    })
                            .setNegativeButton(R.string.no,
                                    (dialog, id) -> dialog.cancel());
                    builder.create().show();
                }
                break;
        }

    }

    @Override
    public void onPinEntered(String pin) {
        pinView.hideKeyBoard();
        changeButtonState(pincodeRegistrationButton, true);
    }

    @Override
    public void onPinChanged() {
        changeButtonState(pincodeRegistrationButton, false);
        if (pinView != null && pinView.getEnteredPin().length() != 0) {
            errorPinCodeText.setVisibility(View.GONE);
            ViewUtil.changeGuidelinePercent(pinCodeRegistrationCardBottomGuideline, 0.8f);
            pincodeRegistrationButton.setVisibility(View.VISIBLE);
        }
    }


}