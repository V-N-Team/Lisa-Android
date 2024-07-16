package ht.lisa.app.ui.main;

import static ht.lisa.app.ui.userprofile.UserProfileEditFragment.USER_AVATARS;

import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.Avatar;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.User;
import ht.lisa.app.model.response.ProfileResponse;
import ht.lisa.app.network.CallbackListener;
import ht.lisa.app.network.RequestManager;
import ht.lisa.app.ui.base.BaseActivity;
import ht.lisa.app.ui.gamesubscription.GamesSubscriptionFragment;
import ht.lisa.app.ui.mytickets.MyTicketsFragment;
import ht.lisa.app.ui.registration.PinCodeRegistrationFragment;
import ht.lisa.app.ui.registration.RegistrationActivity;
import ht.lisa.app.ui.registration.error.NoConnectivityDialog;
import ht.lisa.app.ui.userprofile.UserProfileMainFragment;
import ht.lisa.app.ui.wallet.MainWalletFragment;
import ht.lisa.app.ui.wallet.WalletActivity;
import ht.lisa.app.ui.wallet.gamelist.GameListFragment;
import ht.lisa.app.ui.wallet.messagecenter.MessageCenterFragment;
import ht.lisa.app.ui.wallet.winningnumbers.WinningNumbersFragment;
import ht.lisa.app.util.ActionUtil;
import ht.lisa.app.util.FragmentUtil;
import ht.lisa.app.util.LanguageUtil;
import ht.lisa.app.util.OnFragmentVisibleListener;
import ht.lisa.app.util.RxUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements MyTicketsFragment.OnNavigateToGamesListListener, View.OnClickListener, Drawer.OnDrawerListener, OnFragmentVisibleListener {

    public static final String MAIN_ACTIVITY = "mainActivity";
    public static final String TOP_SHEET_MESSAGE = "topSheetMessage";
    public static final int MAIN_WALLET_FRAGMENT = 0;
    public static final int USER_PROFILE_MAIN = 1;
    public static final int MESSAGE_CENTER = 2;
    public static final int MY_TICKETS = 3;
    public static final int TOP_SHEET_DURATION = 5000;
    @BindView(R.id.main_bottom_navigation)
    BottomNavigationView mainBottomNavigation;
    @BindView(R.id.menu)
    ImageButton menu;
    @BindView(R.id.main_message)
    ImageButton mainMessage;
    @BindView(R.id.main_bottom_navigation_layout)
    LinearLayout mainBottomNavigationLayout;
    @BindView(R.id.main_bottom_navigation_icon)
    ImageView mainBottomNavigationIcon;
    @BindView(R.id.appbar_layout)
    View appbarLayout;
    @BindView(R.id.appbar_logo)
    ImageView appbarLogo;

    @BindView(R.id.nav_view_my_profile)
    TextView textMyProfile;

    @BindView(R.id.textMyTickets)
    TextView textMyTickets;
    @BindView(R.id.textWalletCashout)
    TextView textWalletCashout;
    @BindView(R.id.textSettings)
    TextView textSettings;
    @BindView(R.id.textWinningNumbers)
    TextView textWinningNumbers;
    @BindView(R.id.textTchala)
    TextView textTchala;
    @BindView(R.id.textSubscriptions)
    TextView textSubscriptions;
    @BindView(R.id.textFaq)
    TextView textFaq;
    @BindView(R.id.textMessages)
    TextView textMessages;
    @BindView(R.id.textContact)
    TextView textContact;


    private TextView navFullName;
    private ImageView navUserProfileLogo;
    private ImageView navMessageCenterIcon;
    private final ArrayList<Avatar> avatars = new ArrayList<>();
    private Drawer drawer;
    private int profileAttempt;
    private boolean homeScreenAttached = true;
    private boolean hasUnreadMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageUtil.initAppLanguage(this);
        setContentView(R.layout.activity_main);
        setDrawer();
        ButterKnife.bind(this);
        Locale.setDefault(new Locale(LisaApp.getInstance().getLanguage(this)));
        updateDrawerTexts();
        setBottomNavigationMenu();
        appbarLayout.bringToFront();
        mainMessage.setOnClickListener(this);
        mainBottomNavigation.setSelectedItemId(R.id.game_list);
        mainBottomNavigationIcon.setOnClickListener(this);
        SharedPreferencesUtil.setGetStarted(false);
        //LisaApp.getInstance().bindOneSignal();
        if (getIntent() != null && getIntent().getIntExtra(MAIN_ACTIVITY, 0) > 0) {
            showFragment(getIntent().getIntExtra(MAIN_ACTIVITY, 0));
        } else {
            getAvatarList();
        }
        if (getIntent() != null && getIntent().hasExtra(TOP_SHEET_MESSAGE)) {
            showMessage(getIntent().getStringExtra(TOP_SHEET_MESSAGE));
        }

        playLisaSoundIfNeeded();
        showGetGiftDialogIfNeeded();

    }


    private void navigateToEditProfileScreen() {
        if (avatars == null || avatars.isEmpty()) {
            RxUtil.delayedConsumer(500, aLong -> navigateToEditProfileScreen());
        } else {
            Intent intent = new Intent(MainActivity.this, WalletActivity.class);
            intent.putExtra(WalletActivity.WALLET_FRAGMENT, WalletActivity.WalletPage.USER_PROFILE_EDIT);
            for (Avatar avatar : avatars) {
                avatar.setAvatarChecked(String.valueOf(avatar.getId()).equals(LisaApp.getInstance().getUser().getAvatar()));
            }
            intent.putExtra(USER_AVATARS, avatars);
            startActivity(intent);
        }
    }

    private void playLisaSoundIfNeeded() {
        if (SharedPreferencesUtil.isLisaSoundNeeded()) {
            MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.tagline_lisa);
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
            SharedPreferencesUtil.setLisaSoundNeeded(false);
        }
    }

    private void showGetGiftDialogIfNeeded() {
        if (!SharedPreferencesUtil.isNotNeedToShowProfileDialog())
            RxUtil.delayedConsumer(3000, aLong -> {
                GetGiftDialog getGiftDialog = new GetGiftDialog(MainActivity.this::navigateToEditProfileScreen);
                if (getGiftDialog.getDialog() != null) {
                    getGiftDialog.getDialog().setOnDismissListener(dialog ->
                            changeAppBar());
                }
                getGiftDialog.show(getSupportFragmentManager(), GetGiftDialog.class.getSimpleName());
                SharedPreferencesUtil.setNotNeedToShowProfileDialog(true);
            });
    }

    private void setDrawer() {
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withDrawerWidthPx(getDrawerWidth())
                .withCustomView(LayoutInflater.from(this).inflate(R.layout.menu_layout, (ViewGroup) getWindow().getDecorView().getRootView(), false))
                .withOnDrawerListener(this)
                .build();
    }

    private void updateDrawerTexts() {
        textMyProfile.setText(getString(R.string.view_my_profile));
        textMyTickets.setText(getString(R.string.my_tickets));
        textWalletCashout.setText(getString(R.string.wallet_cash_out));
        textSettings.setText(getString(R.string.settings));
        textWinningNumbers.setText(getString(R.string.winning_numbers));
        textTchala.setText(getString(R.string.tсhala));
        textSubscriptions.setText(getString(R.string.games_subscriptions_));
        textFaq.setText(getString(R.string.faq));
        textMessages.setText(getString(R.string.message_center));
        textContact.setText(getString(R.string.contact_us));
    }

    private void getAvatarList() {
        RequestManager.getInstance().getAvatarList(new Callback<List<Avatar>>() {
            @Override
            public void onResponse(@NonNull Call<List<Avatar>> call, @NonNull Response<List<Avatar>> response) {
                avatars.clear();
                if (response.body() != null) {
                    avatars.addAll(response.body());
                    LisaApp.getInstance().setAvatars(avatars);
                }
                getUserProfile();
            }

            @Override
            public void onFailure(@NonNull Call<List<Avatar>> call, @NonNull Throwable t) {
                getUserProfile();
            }
        });
    }

    private void getUserProfile() {
        RequestManager.getInstance().getUserProfile(new Phone(SharedPreferencesUtil.getPhone()), new CallbackListener<ProfileResponse>() {
            @Override
            public void onSuccess(ProfileResponse response) {
                profileAttempt++;
                if (response != null && response.getState() == 0) {


                    User user1 = response.getUser();
                    user1.setPhone(SharedPreferencesUtil.getPhone());
                    LisaApp.getInstance().setUser(user1);
                    LisaApp.getInstance().setReadonlyFields(response.getReadonly());
                    //showFragment(MAIN_WALLET_FRAGMENT);
                } else {
                    if (profileAttempt < 2) {
                        getUserProfile();
                    } else {
                        SharedPreferencesUtil.clearToken();
                        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        LisaApp.getInstance().unbindOneSignal();
                    }
                }
            }

            @Override
            public void onFailure(Throwable error) {

            }
        });
    }

    private void showFragment(int fragment) {
        switch (fragment) {
            case USER_PROFILE_MAIN:
                mainBottomNavigation.setSelectedItemId(R.id.user_profile);
                break;

            case MY_TICKETS:
                mainBottomNavigation.setSelectedItemId(R.id.tickets);
                break;

            case MESSAGE_CENTER:
                try {
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new MessageCenterFragment(), false);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                break;

            default:
                FragmentUtil.replaceFragment(getSupportFragmentManager(), new GameListFragment(), false);
                //mainBottomNavigation.setSelectedItemId(R.id.game_list);
                break;

        }
    }

    private int getDrawerWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width / 100 * 85;
    }

    private void initDrawerViews() {
        navUserProfileLogo = findViewById(R.id.nav_user_profile_logo);
        navFullName = findViewById(R.id.nav_full_name);
        //  navBonusLisa = findViewById(R.id.nav_bonus_lisa);
        navMessageCenterIcon = findViewById(R.id.nav_message_center_icon);
        //  navPromotions = findViewById(R.id.nav_promotions);
        //    navReferenceProgram = findViewById(R.id.nav_reference_program);
        //    navBonusLisa.setOnClickListener(this);
        findViewById(R.id.nav_view_my_profile).setOnClickListener(this);
        findViewById(R.id.nav_contact_us).setOnClickListener(this);
        findViewById(R.id.nav_faq).setOnClickListener(this);
        findViewById(R.id.nav_tchala).setOnClickListener(this);
        findViewById(R.id.nav_message_center).setOnClickListener(this);
        //    navPromotions.setOnClickListener(this);
        findViewById(R.id.nav_settings).setOnClickListener(this);
        //  navTChala.setOnClickListener(this);
        findViewById(R.id.nav_game_subscription).setOnClickListener(this);
        findViewById(R.id.nav_wallet_and_cash_out).setOnClickListener(this);
        findViewById(R.id.nav_winning_numbers).setOnClickListener(this);
        //    navReferenceProgram.setOnClickListener(this);
        findViewById(R.id.nav_my_tickets).setOnClickListener(this);
        findViewById(R.id.menu_youtube).setOnClickListener(this);
        findViewById(R.id.menu_insta).setOnClickListener(this);
        findViewById(R.id.menu_facebook).setOnClickListener(this);
        findViewById(R.id.menu_twitter).setOnClickListener(this);
    }

    private void setBottomNavigationMenu() {
        mainBottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            setCheckableBottomNav(true);
            switch (menuItem.getItemId()) {
                case R.id.user_profile:
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new UserProfileMainFragment(), true);
                    break;

                case R.id.wallet:
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new MainWalletFragment(), true);
                    break;

                case R.id.game_list:
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new GameListFragment(), true);
                    break;

                case R.id.tickets:
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new MyTicketsFragment(), true);
                    break;

                case R.id.winning_numbers:
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new WinningNumbersFragment(), true);
                    break;
            }
            return true;
        });
    }

    private void hideShowBottomNavigation(boolean show) {
        if (mainBottomNavigationLayout == null || mainBottomNavigationIcon == null) return;
        mainBottomNavigationLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        mainBottomNavigationIcon.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void changeAppBar() {
        if (appbarLayout != null) {
            appbarLayout.setBackground(ContextCompat.getDrawable(this, homeScreenAttached ? R.color.white : R.drawable.yellow_orange_gradient));
            appbarLogo.setImageDrawable(AppCompatResources.getDrawable(this, homeScreenAttached ? R.drawable.ic_lisa_logo_orange : R.drawable.ic_white_logo));
            menu.setImageDrawable(AppCompatResources.getDrawable(this, homeScreenAttached ? R.drawable.ic_orange_menu : R.drawable.ic_left_menu_2));
            mainMessage.setImageDrawable(AppCompatResources.getDrawable(this, homeScreenAttached ?
                    (hasUnreadMessage ? R.drawable.ic_have_message_orange : R.drawable.ic_message_orange) :
                    (hasUnreadMessage ? R.drawable.ic_message_new : R.drawable.ic_message)));
        }
    }

    @OnClick(R.id.menu)
    void onMenuClick() {
        if (!drawer.isDrawerOpen())
            drawer.openDrawer();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
            Fragment fragment = getCurrentFragment();
            setCheckableBottomNav(fragment instanceof UserProfileMainFragment
                    || fragment instanceof WinningNumbersFragment
                    || fragment instanceof MainWalletFragment
                    || fragment instanceof MyTicketsFragment
                    || fragment instanceof GameListFragment
                    || drawer.isDrawerOpen());
            homeScreenAttached = fragment instanceof GameListFragment;
            changeAppBar();
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        initDrawerViews();
        if (navFullName != null) {
            if (LisaApp.getInstance().getUser() != null) {
                navFullName.setText(LisaApp.getInstance().getUser().getFullName());
                int userAvatarId = -1;
                try {
                    userAvatarId = Integer.parseInt(LisaApp.getInstance().getUser().getAvatar());
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
                if (LisaApp.getInstance().getAvatars().size() > 0 && userAvatarId >= 0) {
                    Glide.with(this)
                            .load(Avatar.getAvatarUrl(LisaApp.getInstance().getAvatars(), userAvatarId))
                            .circleCrop()
                            .apply(RequestOptions.circleCropTransform())
                            .into(navUserProfileLogo);
                }
            }
        }
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        homeScreenAttached = getCurrentFragment() instanceof GameListFragment;
        changeAppBar();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof SupportRequestManagerFragment
                || fragment instanceof DialogFragment)
            return;

        setCheckableBottomNav(
                fragment instanceof UserProfileMainFragment
                        || fragment instanceof WinningNumbersFragment
                        || fragment instanceof MainWalletFragment
                        || fragment instanceof MyTicketsFragment
                        || fragment instanceof GameListFragment);


        homeScreenAttached = fragment instanceof GameListFragment;
        changeAppBar();
    }

    private void setCheckableBottomNav(boolean checkable) {
        if (mainBottomNavigation != null) {
            Menu menu = mainBottomNavigation.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setCheckable(checkable);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_view_my_profile:
                mainBottomNavigation.setSelectedItemId(R.id.user_profile);
                drawer.closeDrawer();
                break;

            case R.id.nav_my_tickets:
                mainBottomNavigation.setSelectedItemId(R.id.tickets);
                drawer.closeDrawer();
                break;

            case R.id.nav_wallet_and_cash_out:
                mainBottomNavigation.setSelectedItemId(R.id.wallet);
                drawer.closeDrawer();
                break;

            case R.id.nav_settings:
                Intent intent = new Intent(MainActivity.this, WalletActivity.class);
                intent.putExtra(WalletActivity.WALLET_FRAGMENT, WalletActivity.WalletPage.SETTINGS);
                startActivity(intent);
                drawer.closeDrawer();
                break;

            case R.id.nav_tchala:
                WebViewActivity.openUrl(this, "http://lisa.s7.devpreviewr.com/tchala/");
                drawer.closeDrawer();
                break;

            case R.id.nav_winning_numbers:
                FragmentUtil.replaceFragment(getSupportFragmentManager(), new WinningNumbersFragment(), true);
                drawer.closeDrawer();
                break;

            case R.id.nav_game_subscription:
                FragmentUtil.replaceFragment(getSupportFragmentManager(), new GamesSubscriptionFragment(), true);
                drawer.closeDrawer();
                break;

            case R.id.nav_faq:
                WebViewActivity.openUrl(this, "http://lisa.s7.devpreviewr.com/faq/");
                break;

/*
            case R.id.nav_bonus_lisa:
                drawer.closeDrawer();
                break;
*/

          /*  case R.id.nav_reference_program:
                Toast.makeText(MainActivity.this, "Reference program", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer();
                break;

            case R.id.nav_promotions:
                Toast.makeText(MainActivity.this, "Promotions", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer();
                break;*/

            case R.id.nav_message_center:
            case R.id.main_message:
                try {
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new MessageCenterFragment(), true);
                    drawer.closeDrawer();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                break;

            case R.id.nav_contact_us:
                Intent contactUsIntent = new Intent(MainActivity.this, WalletActivity.class);
                contactUsIntent.putExtra(WalletActivity.WALLET_FRAGMENT, WalletActivity.WalletPage.CONTACT_US);
                startActivity(contactUsIntent);
                drawer.closeDrawer();
                break;

            case R.id.menu_facebook:
                ActionUtil.openLisaFacebookMessenger(this);
                break;

            case R.id.menu_insta:
                ActionUtil.openLisaInstagram(this);
                break;

            case R.id.menu_youtube:
                ActionUtil.openLisaYoutube(this);
                break;

            case R.id.menu_twitter:
                ActionUtil.openLisaTwitter(this);
                break;

            case R.id.main_bottom_navigation_icon:
                mainBottomNavigation.setSelectedItemId(R.id.game_list);
                break;

        }
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }

    public void onFragmentShowListener(String fragment, boolean visible) {
        if (fragment.equals(NoConnectivityDialog.class.getSimpleName()) || fragment.equals(PinCodeRegistrationFragment.class.getSimpleName())) {
            if (appbarLayout != null) {
                appbarLayout.setVisibility(visible ? View.GONE : View.VISIBLE);
            }
            hideShowBottomNavigation(!visible);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessageSetEvent(GetMessageIdSetEvent getMessageIdSetEvent) {
        if (SharedPreferencesUtil.existUnreadMessage(getMessageIdSetEvent.getMessageSetFromEvent())) {
            hasUnreadMessage = true;
            if (mainMessage != null)
                if (!(getCurrentFragment() instanceof GetGiftDialog))
                    mainMessage.setImageResource(homeScreenAttached ? R.drawable.ic_have_message_orange : R.drawable.ic_message_new);
            if (navMessageCenterIcon != null)
                navMessageCenterIcon.setImageResource(R.drawable.ic_message_menu);
        } else {
            hasUnreadMessage = false;
            if (getCurrentFragment() instanceof GetGiftDialog) return;
            if (mainMessage != null)
                if (!(getCurrentFragment() instanceof GetGiftDialog))
                    mainMessage.setImageResource(homeScreenAttached ? R.drawable.ic_message_orange : R.drawable.ic_message);
            if (navMessageCenterIcon != null)
                navMessageCenterIcon.setImageResource(R.drawable.ic_message_empty);
        }
    }

    @Override
    public void onNavigateToGamesList() {
        mainBottomNavigation.setSelectedItemId(R.id.game_list);
    }
}