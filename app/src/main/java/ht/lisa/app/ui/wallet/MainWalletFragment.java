package ht.lisa.app.ui.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.response.AccountResponse;
import ht.lisa.app.model.response.ProfileResponse;
import ht.lisa.app.ui.wallet.cashout.CashOutChooseFragment;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.FragmentUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextUtil;

public class MainWalletFragment extends BaseWalletFragment {
    public static final String SCREEN_NAME = "WalletScreen";


    private static final long START_TIME = 900000;
    @BindView(R.id.profile_name)
    TextView profileName;
    @BindView(R.id.wallet_balance)
    TextView walletBalance;
    @BindView(R.id.wallet_bonus)
    TextView walletBonus;
    @BindView(R.id.wallet_cashout_balance)
    TextView walletCashOutBalance;
    @BindView(R.id.wallet_playable_funds)
    TextView walletPlayableFunds;
    @BindView(R.id.cash_out_code_layout)
    RelativeLayout cashOutCodeLayout;
    @BindView(R.id.cash_out_code_text)
    TextView cashOutCodeText;
    @BindView(R.id.cash_out_time_text)
    TextView cashOutTimeText;
    @BindView(R.id.cash_out_date_text)
    TextView cashOutDateText;
    @BindView(R.id.wallet_menu_layout)
    ConstraintLayout walletMenuLayout;

    private CountDownTimer countDownTimer;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_wallet, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (LisaApp.getInstance().getUser() != null && LisaApp.getInstance().getUser().getFullName() != null) {
            profileName.setText(String.format(getStringFromResource(R.string.wallet), LisaApp.getInstance().getUser().getFullName()));
        } else {
            walletPresenter.getProfile(new Phone(SharedPreferencesUtil.getPhone()));
        }
        walletPresenter.getAccount(new Phone(SharedPreferencesUtil.getPhone()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharedPreferencesUtil.getCashOutCodeTime() > 0 && DateTimeUtil.getCurrentTime() < SharedPreferencesUtil.getCashOutCodeTime() + START_TIME) {
            walletMenuLayout.setBackgroundColor(getColorFromResource(R.color.white));
            cashOutCodeLayout.setVisibility(View.VISIBLE);
            cashOutCodeText.setText(SharedPreferencesUtil.getCashOutCode());
            cashOutDateText.setText(DateTimeUtil.getTransactionDateFormat(String.valueOf(SharedPreferencesUtil.getCashOutCodeTime() / DateTimeUtil.SECOND), getActivity()));
            startCountDownTimer(SharedPreferencesUtil.getCashOutCodeTime() + START_TIME - DateTimeUtil.getCurrentTime());
        }
    }

    private void startCountDownTimer(long startTime) {
        String resendText = String.format(getStringFromResource(R.string.left_s_min), DateTimeUtil.getSimpleDateFormatTime(startTime, getActivity()));
        cashOutTimeText.setText(resendText);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(startTime, DateTimeUtil.SECOND) {
            public void onTick(long millisUntilFinished) {
                if (getContext() != null) {
                    String resendText = String.format(getStringFromResource(R.string.left_s_min), DateTimeUtil.getSimpleDateFormatTime(millisUntilFinished, getActivity()));
                    cashOutTimeText.setText(resendText);
                }
            }

            public void onFinish() {
                if (!MainWalletFragment.this.isDetached()) {
                    if (getContext() != null) {
                        walletMenuLayout.setBackground(getDrawableFromResource(R.drawable.main_bottom_bg));
                    }
                    cashOutCodeLayout.setVisibility(View.GONE);
                }
                SharedPreferencesUtil.setCashOutCode("");
                SharedPreferencesUtil.setCashOutCodeTime(-1);
            }
        }.start();
    }

    @OnClick({R.id.wallet_transfer_friend, R.id.wallet_reload_money, R.id.wallet_pop_locations, R.id.wallet_transaction_history})
    void onWalletMenuItemClick(View view) {
        Intent intent = new Intent(getContext(), WalletActivity.class);
        switch (view.getId()) {
            case R.id.wallet_transfer_friend:
                intent.putExtra(WalletActivity.WALLET_FRAGMENT, WalletActivity.WalletPage.TRANSFER_FRIEND);
                break;

            case R.id.wallet_reload_money:
                intent.putExtra(WalletActivity.WALLET_FRAGMENT, WalletActivity.WalletPage.RELOAD_MONEY_FUNDS);
                break;

            case R.id.wallet_pop_locations:
                intent.putExtra(WalletActivity.WALLET_FRAGMENT, WalletActivity.WalletPage.POP_LOCATION);
                break;

            case R.id.wallet_transaction_history:
                intent.putExtra(WalletActivity.WALLET_FRAGMENT, WalletActivity.WalletPage.TRANSACTION_HISTORY);
                break;
        }
        startActivity(intent);
    }

    @OnClick(R.id.wallet_cash_out)
    void onCashOutClick() {
        FragmentUtil.replaceFragment(getFragmentManager(), new CashOutChooseFragment(), true);
    }

    @Override
    public void getData(Object object) {
        if (object instanceof AccountResponse) {
            AccountResponse response = (AccountResponse) object;
            if (response.getState() == 0) {
                if (getContext() == null) return;
                walletBalance.setText(TextUtil.getDecimalString(response.getTotal()));
                walletBonus.setText(TextUtil.getDecimalString(response.getBonus()));
                walletCashOutBalance.setText(TextUtil.getDecimalString(response.getWithdrawal()));
                walletPlayableFunds.setText(TextUtil.getDecimalString(response.getTotal()));
            }

        } else if (object instanceof ProfileResponse) {
            ProfileResponse profileResponse = (ProfileResponse) object;
            LisaApp.getInstance().setUser(profileResponse.getUser());
            profileName.setText(String.format(getStringFromResource(R.string.wallet), LisaApp.getInstance().getUser().getFullName()));
        }
    }
}
