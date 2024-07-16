package ht.lisa.app.ui.wallet;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ht.lisa.app.R;
import ht.lisa.app.model.Avatar;
import ht.lisa.app.model.FullSingleDraw;
import ht.lisa.app.model.Message;
import ht.lisa.app.model.Ticket;
import ht.lisa.app.model.response.CitiesResponse;
import ht.lisa.app.ui.base.BaseActivity;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.main.WebViewFragment;
import ht.lisa.app.ui.registration.SettingsFragment;
import ht.lisa.app.ui.registration.error.ErrorFragment;
import ht.lisa.app.ui.userprofile.ContactUsFragment;
import ht.lisa.app.ui.userprofile.UserProfileEditFragment;
import ht.lisa.app.ui.wallet.cashout.CashOutChooseFragment;
import ht.lisa.app.ui.wallet.games.GamesFragment;
import ht.lisa.app.ui.wallet.poplocaton.PopLocationFragment;
import ht.lisa.app.ui.wallet.reloadmoney.ReloadMoneyFundsFragment;
import ht.lisa.app.ui.wallet.transactionhistory.TransactionHistoryFragment;
import ht.lisa.app.ui.wallet.transferfriend.TransferFriendPhoneNumberFragment;
import ht.lisa.app.ui.wallet.winningnumbers.GameResultDetailsFragment;
import ht.lisa.app.util.FragmentUtil;
import ht.lisa.app.util.LanguageUtil;

import static ht.lisa.app.ui.main.WebViewFragment.MESSAGE;
import static ht.lisa.app.ui.userprofile.UserProfileEditFragment.CITIES_RESPONSE_KEY;
import static ht.lisa.app.ui.userprofile.UserProfileEditFragment.USER_AVATARS;

public class WalletActivity extends BaseActivity {

    public static final String WALLET_FRAGMENT = "walletFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageUtil.initAppLanguage(this);
        setContentView(R.layout.activity_wallet);
        if (getIntent() != null) {
            int walletFragment = getIntent().getIntExtra(WALLET_FRAGMENT, 0);
            Fragment fragment = null;

            switch (walletFragment) {
                case WalletPage.TRANSFER_FRIEND:
                    fragment = new TransferFriendPhoneNumberFragment();
                    break;
                case WalletPage.RELOAD_MONEY_FUNDS:
                    fragment = new ReloadMoneyFundsFragment();
                    break;
                case WalletPage.CASH_OUT:
                    fragment = new CashOutChooseFragment();
                    break;
                case WalletPage.ERROR_TOKEN:
                    fragment = new ErrorFragment();
                    break;
                case WalletPage.POP_LOCATION:
                    fragment = new PopLocationFragment();
                    break;
                case WalletPage.TRANSACTION_HISTORY:
                    fragment = new TransactionHistoryFragment();
                    break;
                case WalletPage.USER_PROFILE_EDIT:
                    fragment = UserProfileEditFragment.newInstance((ArrayList<Avatar>) getIntent().getSerializableExtra(USER_AVATARS), ((CitiesResponse) getIntent().getSerializableExtra(CITIES_RESPONSE_KEY)));
                    break;
                case WalletPage.SETTINGS:
                    fragment = new SettingsFragment();
                    break;
                case WalletPage.CONTACT_US:
                    fragment = new ContactUsFragment();
                    break;
                case WalletPage.WEB_VIEW:
                    fragment = WebViewFragment.newInstance((Message) getIntent().getSerializableExtra(MESSAGE));
                    break;
                case WalletPage.GAME_RESULT_DETAILS:
                    fragment = GameResultDetailsFragment.newInstance((FullSingleDraw) getIntent().getSerializableExtra(GameResultDetailsFragment.DRAW_ITEM),
                            getIntent().getStringExtra(GameResultDetailsFragment.REGION_KEY),
                            getIntent().getIntExtra(GameResultDetailsFragment.DAY_DRAW_KEY, 0), getIntent().getIntExtra(GameResultDetailsFragment.NIGHT_DRAW_KEY, 1));
                    break;
                case WalletPage.GAMES:
                    Bundle bundle = new Bundle();
                    bundle.putString(GamesFragment.GAME, getIntent().getStringExtra(GamesFragment.GAME));/*
                    bundle.putLong(GamesFragment.DRAW_SEC, getIntent().getLongExtra(GamesFragment.DRAW_SEC, 0));
                    bundle.putInt(GamesFragment.DRAW_DATE, getIntent().getIntExtra(GamesFragment.DRAW_DATE, 0));*/
                    bundle.putString(GamesFragment.JACK_POT, getIntent().getStringExtra(GamesFragment.JACK_POT));
                    bundle.putSerializable(GamesFragment.FL_DRAW, getIntent().getSerializableExtra(GamesFragment.FL_DRAW));
                    bundle.putSerializable(GamesFragment.NY_DRAW, getIntent().getSerializableExtra(GamesFragment.NY_DRAW));
                    bundle.putSerializable(GamesFragment.GA_DRAW, getIntent().getSerializableExtra(GamesFragment.GA_DRAW));
                    bundle.putString(GamesFragment.PROGRESSIVE_JACK_POT, getIntent().getStringExtra(GamesFragment.PROGRESSIVE_JACK_POT));
                    bundle.putString(GamesFragment.ROYAL5_JACK_POT, getIntent().getStringExtra(GamesFragment.ROYAL5_JACK_POT));
                    fragment = BaseWalletFragment.newInstance(GamesFragment.class, bundle);
                    break;
            }

            FragmentUtil.replaceFragment(getSupportFragmentManager(), fragment, false);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof SettingsFragment) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else
            super.onBackPressed();
    }

    public interface WalletPage {
        int TRANSFER_FRIEND = 0;
        int RELOAD_MONEY_FUNDS = 1;
        int CASH_OUT = 2;
        int POP_LOCATION = 3;
        int TRANSACTION_HISTORY = 4;
        int USER_PROFILE_EDIT = 5;
        int SETTINGS = 6;
        int CONTACT_US = 7;
        int WEB_VIEW = 8;
        int GAME_RESULT_DETAILS = 9;
        int GAMES = 10;
        int ERROR_TOKEN = 11;
    }
}
