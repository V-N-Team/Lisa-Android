package ht.lisa.app.ui.base;

import static ht.lisa.app.ui.main.MainActivity.TOP_SHEET_DURATION;
import static ht.lisa.app.ui.main.WebViewFragment.MESSAGE;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tapadoo.alerter.Alerter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.ui.gamesubscription.GamesSubscriptionFragment;
import ht.lisa.app.ui.main.GetMessageEvent;
import ht.lisa.app.ui.main.InvalidTokenEvent;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.main.PinCodeNeededEvent;
import ht.lisa.app.ui.main.SuccessPaymentEvent;
import ht.lisa.app.ui.main.TopSheetDialogEvent;
import ht.lisa.app.ui.main.WebViewFragment;
import ht.lisa.app.ui.mytickets.MyTicketsFragment;
import ht.lisa.app.ui.registration.ChooseLanguageRegistrationFragment;
import ht.lisa.app.ui.registration.MobileRegistrationFragment;
import ht.lisa.app.ui.registration.PinCodeRegistrationFragment;
import ht.lisa.app.ui.registration.RegistrationActivity;
import ht.lisa.app.ui.registration.SecurityWordRegistrationFragment;
import ht.lisa.app.ui.registration.SettingsFragment;
import ht.lisa.app.ui.registration.TermsAgreementRegistrationFragment;
import ht.lisa.app.ui.userprofile.ContactUsFragment;
import ht.lisa.app.ui.userprofile.UserProfileEditFragment;
import ht.lisa.app.ui.userprofile.UserProfileMainFragment;
import ht.lisa.app.ui.wallet.MainWalletFragment;
import ht.lisa.app.ui.wallet.WalletActivity;
import ht.lisa.app.ui.wallet.cashout.CashOutBalanceFragment;
import ht.lisa.app.ui.wallet.cashout.CashOutChooseFragment;
import ht.lisa.app.ui.wallet.gamelist.GameListFragment;
import ht.lisa.app.ui.wallet.games.GamesFragment;
import ht.lisa.app.ui.wallet.games.RecentNumbersFragment;
import ht.lisa.app.ui.wallet.messagecenter.MessageCenterFragment;
import ht.lisa.app.ui.wallet.poplocaton.PopLocationFragment;
import ht.lisa.app.ui.wallet.reloadmoney.ReloadMoneyFundsFragment;
import ht.lisa.app.ui.wallet.transactionhistory.TransactionHistoryFragment;
import ht.lisa.app.ui.wallet.transferfriend.TransferFriendAddAmountFragment;
import ht.lisa.app.ui.wallet.transferfriend.TransferFriendPhoneNumberFragment;
import ht.lisa.app.ui.wallet.winningnumbers.GameResultDetailsFragment;
import ht.lisa.app.ui.wallet.winningnumbers.WinningNumbersFragment;
import ht.lisa.app.util.Analytics;
import ht.lisa.app.util.FragmentUtil;
import ht.lisa.app.util.NetworkChangeReceiver;
import ht.lisa.app.util.SharedPreferencesUtil;

public class BaseActivity extends AppCompatActivity {

    public NetworkChangeReceiver receiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        registerCheckInternetReceiver();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        Bundle bundle = new Bundle();
        bundle.putString(Analytics.SCREEN_NAME, getScreenName(fragment));
        if (!TextUtils.isEmpty(getScreenName(fragment)))
            FirebaseAnalytics.getInstance(this).logEvent(Analytics.SCREEN_VIEW, bundle);
    }

    private String getScreenName(Fragment fragment) {
        String screenName = "";
        if (fragment instanceof SecurityWordRegistrationFragment) {
            screenName = SecurityWordRegistrationFragment.SCREEN_NAME;
        } else if (fragment instanceof ChooseLanguageRegistrationFragment) {
            screenName = ChooseLanguageRegistrationFragment.SCREEN_NAME;
        } else if (fragment instanceof SettingsFragment) {
            screenName = SettingsFragment.SCREEN_NAME;
        } else if (fragment instanceof GamesFragment) {
            screenName = GamesFragment.SCREEN_NAME;
        } else if (fragment instanceof RecentNumbersFragment) {
            screenName = RecentNumbersFragment.SCREEN_NAME;
        } else if (fragment instanceof GamesSubscriptionFragment) {
            screenName = GamesSubscriptionFragment.SCREEN_NAME;
        } else if (fragment instanceof PopLocationFragment) {
            screenName = PopLocationFragment.SCREEN_NAME;
        } else if (fragment instanceof CashOutBalanceFragment) {
            screenName = CashOutBalanceFragment.SCREEN_NAME;
        } else if (fragment instanceof CashOutChooseFragment) {
            screenName = CashOutChooseFragment.SCREEN_NAME;
        } else if (fragment instanceof ReloadMoneyFundsFragment) {
            screenName = ReloadMoneyFundsFragment.SCREEN_NAME;
        } else if (fragment instanceof TermsAgreementRegistrationFragment) {
            screenName = TermsAgreementRegistrationFragment.SCREEN_NAME;
        } else if (fragment instanceof TransferFriendPhoneNumberFragment) {
            screenName = TransferFriendPhoneNumberFragment.SCREEN_NAME;
        } else if (fragment instanceof TransactionHistoryFragment) {
            screenName = TransactionHistoryFragment.SCREEN_NAME;
        } else if (fragment instanceof ContactUsFragment) {
            screenName = ContactUsFragment.SCREEN_NAME;
        } else if (fragment instanceof UserProfileEditFragment) {
            screenName = UserProfileEditFragment.SCREEN_NAME;
        } else if (fragment instanceof UserProfileMainFragment) {
            screenName = UserProfileMainFragment.SCREEN_NAME;
        } else if (fragment instanceof MessageCenterFragment) {
            screenName = MessageCenterFragment.SCREEN_NAME;
        } else if (fragment instanceof MainWalletFragment) {
            screenName = MainWalletFragment.SCREEN_NAME;
        } else if (fragment instanceof WinningNumbersFragment) {
            screenName = WinningNumbersFragment.SCREEN_NAME;
        } else if (fragment instanceof GameResultDetailsFragment) {
            screenName = GameResultDetailsFragment.SCREEN_NAME;
        } else if (fragment instanceof MyTicketsFragment) {
            screenName = MyTicketsFragment.SCREEN_NAME;
        } else if (fragment instanceof GameListFragment) {
            screenName = GameListFragment.SCREEN_NAME;
        } else if (fragment instanceof TransferFriendAddAmountFragment) {
            screenName = TransferFriendAddAmountFragment.SCREEN_NAME;
        } else if (fragment instanceof PinCodeRegistrationFragment) {
            screenName = PinCodeRegistrationFragment.SCREEN_NAME;
        }
        return screenName;
    }


    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        unRegisterInternetReceiver();
    }

    private void registerCheckInternetReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
    }

    private void unRegisterInternetReceiver() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInvalidTokenEvent(InvalidTokenEvent event) {
        SharedPreferencesUtil.clearToken();
        FragmentUtil.replaceFragment(getSupportFragmentManager(), new MobileRegistrationFragment(), true);
        LisaApp.getInstance().unbindOneSignal();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPinCodeNeededEvent(PinCodeNeededEvent event) {
        Intent intent = new Intent(BaseActivity.this, RegistrationActivity.class);
        intent.putExtra(RegistrationActivity.FROM_BASE_FRAGMENT_KEY, true);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessageEvent(GetMessageEvent event) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (currentFragment instanceof WebViewFragment) return;

        Intent intent = new Intent(this, WalletActivity.class);
        intent.putExtra(WalletActivity.WALLET_FRAGMENT, WalletActivity.WalletPage.WEB_VIEW);
        intent.putExtra(MESSAGE, event.getMessageFromEvent());
        startActivity(intent);
    }


    @Subscribe
    public void onTopSheetDialogEvent(TopSheetDialogEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.TOP_SHEET_MESSAGE, event.getMessage());
        startActivity(intent);
    }

    @Subscribe
    public void onSuccessPaymentEvent(SuccessPaymentEvent event) {
        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.container);
        if (currentFragment != null && currentFragment.getActivity() != null)
            if (currentFragment instanceof WebViewFragment) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.TOP_SHEET_MESSAGE, getString(R.string.transaction_confirmation));
                startActivity(intent);
                this.finish();
            } else {
                showMessage(getString(R.string.transaction_confirmation));
            }

    }

    protected void showMessage(String text) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage(text)
                .setPositiveButton(android.R.string.ok, null)
                .show();


       /* Alerter alerter = Alerter.create(this, R.layout.dialog_top_sheet);
        alerter.setDuration(TOP_SHEET_DURATION);
        RelativeLayout layout = (RelativeLayout) alerter.getLayoutContainer();
        if (layout != null) {
            ((TextView) alerter.getLayoutContainer().findViewById(R.id.top_sheet_message)).setText(text);
            alerter.getLayoutContainer().findViewById(R.id.top_sheet_close_icon).setOnClickListener(view ->
                    Alerter.hide());
            alerter.setBackgroundColorRes(R.color.successPaymentColor);
            layout.setBackgroundResource(R.color.successPaymentColor);
            alerter.show();
        }*/
    }

}
