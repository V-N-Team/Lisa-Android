package ht.lisa.app.ui.wallet.reloadmoney;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Guideline;

import butterknife.BindView;
import ht.lisa.app.R;
import ht.lisa.app.ui.component.PinView;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.ViewUtil;

public class ReloadMoneyConfirmPinFragment extends BaseWalletFragment implements PinView.OnPinEnteredListener {
    private static final int PIN_ATTEMPTS = 4;
    @BindView(R.id.reload_money_confirm_pin_title)
    TextView reloadMoneyConfirmPinTitle;
    @BindView(R.id.reload_money_confirm_pin_message)
    TextView reloadMoneyConfirmPinMessage;
    @BindView(R.id.reload_money_confirm_pin_middle_guideline)
    Guideline reloadMoneyConfirmPinMiddleGuideline;
    @BindView(R.id.reload_money_confirm_pin_card_bottom_guideline)
    Guideline reloadMoneyConfirmPinCardBottomGuideline;
    @BindView(R.id.error_pin_code_text)
    TextView errorPinCodeText;
    @BindView(R.id.pinView)
    PinView pinView;
    private int attempts;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reload_money_confirm_pin, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pinView.setOnPinEnteredListener(this);
        setDataToViews();
        attempts = PIN_ATTEMPTS;
    }

    @Override
    public void onStart() {
        super.onStart();
        pinView.clear();
        pinView.showKeyBoard();
    }

    private void setDataToViews() {
        reloadMoneyConfirmPinTitle.setText(getStringFromResource(R.string.enter_your_mon_cash_pin));
        reloadMoneyConfirmPinMessage.setText(getStringFromResource(R.string.enter_your_mon_cash_pin_to_transfer));
    }

    private void onPinCodeCheck() {
        if (attempts == PIN_ATTEMPTS) {
            showPinCodeCorrectness(true);
            attempts--;
        } else if (attempts == 0) {
            showPinCodeCorrectness(true);
            attempts = PIN_ATTEMPTS;
            if (getActivity() != null) {
                getActivity().finish();
            }
        } else {
            showPinCodeCorrectness(false);
            attempts--;
        }
    }

    private void showPinCodeCorrectness(boolean isCorrect) {
        errorPinCodeText.setVisibility(isCorrect ? View.GONE : View.VISIBLE);
        String errorText = getStringFromResource(R.string.invalid_pincode_you_have) + attempts + getStringFromResource(R.string.attempts);
        errorPinCodeText.setText(errorText);
        ViewUtil.changeGuidelinePercent(reloadMoneyConfirmPinMiddleGuideline, isCorrect ? 0.69f : 0.72f);
        ViewUtil.changeGuidelinePercent(reloadMoneyConfirmPinCardBottomGuideline, isCorrect ? 0.66f : 0.69f);
        pinView.clear();
        pinView.setFieldsError(!isCorrect);
    }

    @Override
    public void onPinEntered(String pin) {
        attempts = PIN_ATTEMPTS;
        showPinCodeCorrectness(true);
        showNextFragment(BaseWalletFragment.newInstance(ReloadMoneyVerificationFragment.class, null));
    }

    @Override
    public void onPinChanged() {

    }
}
