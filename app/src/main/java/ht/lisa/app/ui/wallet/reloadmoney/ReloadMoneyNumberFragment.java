package ht.lisa.app.ui.wallet.reloadmoney;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.R;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.response.MonCashRequestResponse;
import ht.lisa.app.ui.component.PhoneEditView;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.ui.wallet.WalletActivity;

import static ht.lisa.app.ui.wallet.reloadmoney.ReloadMoneyFundsFragment.RELOAD_MONEY_AMOUNT;

public class ReloadMoneyNumberFragment extends BaseWalletFragment {
    @BindView(R.id.reload_money_number_number_edittext)
    PhoneEditView reloadMoneyNumberNumberEditText;
    @BindView(R.id.reload_money_number_button)
    Button reloadMoneyNumberButton;
    @BindView(R.id.reload_money_number_error_text)
    TextView reloadMoneyNumberErrorText;

    private String reloadMoneyAmount;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reload_money_number, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            reloadMoneyAmount = getArguments().getString(RELOAD_MONEY_AMOUNT);
        }
        reloadMoneyNumberNumberEditText.setOnEditTextChangeListener(() ->
                changeButtonState(reloadMoneyNumberButton, reloadMoneyNumberNumberEditText.isPhoneFull()));
    }

    @Override
    public void getData(Object object) {
        if (object instanceof MonCashRequestResponse) {
            MonCashRequestResponse response = (MonCashRequestResponse) object;
            if (response.getState() == 0) {
                if (getContext() == null) return;
                startActivity(new Intent(getActivity(), MainActivity.class));
            } else {
                showMessage(response.getErrorMessage());
            }
        }
    }

    @OnClick(R.id.reload_money_number_button)
    void onReloadMoneyNumberButtonClick() {
        if (reloadMoneyNumberNumberEditText.isPhoneFull()) {
            walletPresenter.monCashRequest(new Phone(reloadMoneyNumberNumberEditText.getPhone()), reloadMoneyAmount);
        }
    }
}
