package ht.lisa.app.ui.wallet.reloadmoney;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import ht.lisa.app.R;
import ht.lisa.app.ui.component.VerificationCodeEditView;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.SharedPreferencesUtil;

public class ReloadMoneyVerificationFragment extends BaseWalletFragment implements VerificationCodeEditView.OnCodeEnterCompleteListener {

    private static final int START_TIME = 600000;

    @BindView(R.id.reload_money_verification_code_edit_text)
    VerificationCodeEditView reloadMoneyVerificationCodeEditText;
    @BindView(R.id.reload_money_verification_resend_text)
    TextView reloadMoneyVerificationResendText;
    @BindView(R.id.reload_money_verification_error_text)
    TextView reloadMoneyVerificationErrorText;
    private CountDownTimer countDownTimer;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reload_money_verification, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reloadMoneyVerificationCodeEditText.setErrorTextView(reloadMoneyVerificationErrorText);
        reloadMoneyVerificationCodeEditText.setOnCodeEnterCompleteListener(this);
        startCountDownTimer(START_TIME);
        setResendCode();
    }

    @Override
    public void onStart() {
        super.onStart();
        reloadMoneyVerificationCodeEditText.clearEditTexts();
        reloadMoneyVerificationCodeEditText.setFocusOnFirstEmptyPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharedPreferencesUtil.getBindDeviceRequestTime() > 0 && (SharedPreferencesUtil.getBindDeviceRequestTime() + START_TIME) > DateTimeUtil.getCurrentTime()) {
            startCountDownTimer((SharedPreferencesUtil.getBindDeviceRequestTime() + START_TIME) - DateTimeUtil.getCurrentTime());
        }
    }

    private void setResendCode() {
        reloadMoneyVerificationResendText.setOnClickListener(reSendView -> {
        });
    }

    private void startCountDownTimer(long startTime) {
        reloadMoneyVerificationResendText.setEnabled(false);
        String resendText = getStringFromResource(R.string.re_send_code_in) + DateTimeUtil.getSimpleDateFormatTime(startTime, getActivity());
        reloadMoneyVerificationResendText.setText(resendText);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(startTime, DateTimeUtil.SECOND) {
            public void onTick(long millisUntilFinished) {
                if (getContext() != null) {
                    String resendText = getStringFromResource(R.string.re_send_code_in) + DateTimeUtil.getSimpleDateFormatTime(millisUntilFinished, getActivity());
                    reloadMoneyVerificationResendText.setText(resendText);
                }
            }

            public void onFinish() {
                if (getContext() != null) {
                    reloadMoneyVerificationResendText.setText(getStringFromResource(R.string.re_send_code));
                    reloadMoneyVerificationResendText.setEnabled(true);
                    SharedPreferencesUtil.setBindDeviceRequestTime(-1);
                }
            }
        }.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    @Override
    public void onCodeEnterComplete() {
        startActivity(new Intent(getContext(), MainActivity.class));
    }

    @Override
    public void onCodeChange() {

    }
}
