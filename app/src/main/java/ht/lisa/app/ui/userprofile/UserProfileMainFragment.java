package ht.lisa.app.ui.userprofile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.Avatar;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.User;
import ht.lisa.app.model.response.AvatarResponse;
import ht.lisa.app.model.response.CitiesResponse;
import ht.lisa.app.model.response.ProfileResponse;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.registration.RegistrationActivity;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.ui.wallet.WalletActivity;
import ht.lisa.app.util.OnFragmentVisibleListener;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextUtil;

import static ht.lisa.app.ui.registration.RegistrationActivity.SIGN_OUT_KEY;
import static ht.lisa.app.ui.userprofile.UserProfileEditFragment.CITIES_RESPONSE_KEY;
import static ht.lisa.app.ui.userprofile.UserProfileEditFragment.USER_AVATARS;

public class UserProfileMainFragment extends BaseWalletFragment {
    public static final String SCREEN_NAME = "ProfileScreen";

    @BindView(R.id.user_profile_main_edit)
    ImageButton userProfileMainEdit;
    @BindView(R.id.user_profile_main_logo)
    ImageView userProfileMainLogo;
    @BindView(R.id.user_profile_name_tv)
    TextView userProfileNameTv;
    @BindView(R.id.user_profile_male)
    TextView userProfileMale;
    @BindView(R.id.user_profile_dob)
    TextView userProfileDob;
    @BindView(R.id.user_profile_social_card)
    CardView userProfileSocialCard;
    @BindView(R.id.user_profile_social_ic)
    ImageView userProfilesocialIc;
    @BindView(R.id.user_profile_social_tv)
    TextView userProfileSocialTv;
    @BindView(R.id.user_profile_facebook_card)
    CardView userProfileFacebookCard;
    @BindView(R.id.user_profile_facebook_ic)
    ImageView userProfileFacebookIc;
    @BindView(R.id.user_profile_facebook_tv)
    TextView userProfileFacebookTv;
    @BindView(R.id.user_profile_insta_card)
    CardView userProfileInstaCard;
    @BindView(R.id.user_profile_insta_ic)
    ImageView userProfileInstaIc;
    @BindView(R.id.user_profile_insta_tv)
    TextView userProfileInstaTv;
    @BindView(R.id.user_profile_email_ic)
    ImageView userProfileEmailIc;
    @BindView(R.id.user_profile_email_tv)
    TextView userProfileEmailTv;
    @BindView(R.id.user_profile_location)
    TextView userProfileLocation;
    @BindView(R.id.user_profile_scroll)
    ScrollView userProfileScroll;

    private List<Avatar> avatars;
    private CitiesResponse citiesResponse;
    private User user;
    private ProfileResponse profileResponse;
    private OnFragmentVisibleListener onFragmentVisibleListener;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile_main, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showProgress();
        avatars = new ArrayList<>();
        user = LisaApp.getInstance().getUser();
        if (user != null)
            setUserProfileData();
        walletPresenter.getProfile(new Phone(SharedPreferencesUtil.getPhone()));
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

    @Override
    public void onDetach() {
        super.onDetach();
        if (onFragmentVisibleListener != null) {
            onFragmentVisibleListener.onFragmentShowListener(UserProfileMainFragment.class.getSimpleName(), false);
            onFragmentVisibleListener = null;
        }
    }

    @Override
    public void getData(Object object) {
        if (object instanceof ProfileResponse) {
            profileResponse = (ProfileResponse) object;
            LisaApp.getInstance().setUser(profileResponse.getUser());
            LisaApp.getInstance().setReadonlyFields(profileResponse.getReadonly());

            setUserProfileData();
            userProfileMainEdit.setClickable(true);
            userProfileMainEdit.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.ic_edit_profile));
            walletPresenter.getAvatarList();
            walletPresenter.getHaitianCities();
        } else if (object instanceof AvatarResponse) {
            AvatarResponse avatarResponse = (AvatarResponse) object;
            avatars.clear();
            avatars.addAll(avatarResponse.getAvatars());
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                Glide.with(UserProfileMainFragment.this)
                        .load(Avatar.getAvatarUrl(avatars, Integer.parseInt(user.getAvatar())))
                        .circleCrop()
                        .apply(RequestOptions.circleCropTransform())
                        .into(userProfileMainLogo);
            }
        } else if (object instanceof CitiesResponse) {
            hideProgress();
            citiesResponse = (CitiesResponse) object;
            if (user.getCity() != null) {
                userProfileLocation.setText(citiesResponse.getCityById(Integer.parseInt(user.getCity())));
            }
            userProfileLocation.setCompoundDrawablesWithIntrinsicBounds(user.getCity() != null && !user.getCity().isEmpty() ? R.drawable.ic_profile_map_marker_red : R.drawable.ic_profile_map_marker_grey, 0, 0, 0);
        }
    }

    private void setUserProfileData() {
        userProfileNameTv.setText(user.getFullName());
        userProfileMale.setText(user.getSexValue() == 0 ? "-" : getString(user.getSexValue()));
        userProfileMale.setCompoundDrawablesWithIntrinsicBounds(TextUtil.isDataValid(user.getSex()) ? R.drawable.ic_male : R.drawable.ic_male_grey, 0, 0, 0);
        userProfileDob.setText(user.getFormattedDob());
        userProfileDob.setCompoundDrawablesWithIntrinsicBounds(TextUtil.isDataValid(user.getDob()) ? R.drawable.ic_dob : R.drawable.ic_dob_grey, 0, 0, 0);
        setSocialMediaInfo();
        userProfileEmailTv.setText(user.getEmailValue());
        userProfileEmailIc.setImageResource(TextUtil.isDataValid(user.getEmail()) ? R.drawable.ic_email : R.drawable.ic_email_grey);
    }

    private void setSocialMediaInfo() {
        userProfileSocialCard.setVisibility(TextUtil.isDataValid(user.getFb()) && TextUtil.isDataValid(user.getIg()) ? View.GONE : View.VISIBLE);
        userProfileFacebookCard.setVisibility(TextUtil.isDataValid(user.getFb()) && TextUtil.isDataValid(user.getIg()) ? View.VISIBLE : View.GONE);
        userProfileInstaCard.setVisibility(TextUtil.isDataValid(user.getFb()) && TextUtil.isDataValid(user.getIg()) ? View.VISIBLE : View.GONE);
        if (TextUtil.isDataValid(user.getFb()) && TextUtil.isDataValid(user.getIg())) {
            userProfileFacebookTv.setText(user.getFb());
            userProfileInstaTv.setText(user.getIg());
        } else if (!TextUtil.isDataValid(user.getFb()) && TextUtil.isDataValid(user.getIg())) {
            userProfilesocialIc.setImageResource(R.drawable.ic_insta);
            userProfileSocialTv.setText(user.getIg());
        } else if (TextUtil.isDataValid(user.getFb()) && !TextUtil.isDataValid(user.getIg())) {
            userProfilesocialIc.setImageResource(R.drawable.ic_facebook);
            userProfileSocialTv.setText(user.getFb());
        }
    }

    @OnClick(R.id.sign_out)
    void onSignOutClick() {
        if (getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
            builder.setMessage(getString(R.string.are_you_sure_you_want_to_sign_out))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.yes),
                            (dialog, id) -> {
                                SharedPreferencesUtil.setLisaSoundNeeded(true);
                                SharedPreferencesUtil.setNotNeedToShowProfileDialog(false);
                                SharedPreferencesUtil.setNotNeedToShowBdayAlert(false);
                                SharedPreferencesUtil.clearToken();
                                SharedPreferencesUtil.setTickets(null);
                                SharedPreferencesUtil.setGameGuideSet(null);
                                Intent i = new Intent(getContext(), RegistrationActivity.class);
                                i.putExtra(SIGN_OUT_KEY, true);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                getActivity().finish();
                                dialog.cancel();
                                LisaApp.getInstance().unbindOneSignal();
                            })
                    .setNegativeButton(getString(R.string.no),
                            (dialog, id) -> dialog.cancel());

            builder.create().show();
        }
    }

    @OnClick(R.id.user_profile_main_edit)
    void onUserEditClick() {
        if (profileResponse == null) {
            walletPresenter.getProfile(new Phone(SharedPreferencesUtil.getPhone()));
            return;
        }

        if (avatars == null || avatars.isEmpty()) {
            walletPresenter.getAvatarList();
            return;
        }
        Intent intent = new Intent(getContext(), WalletActivity.class);
        intent.putExtra(WalletActivity.WALLET_FRAGMENT, WalletActivity.WalletPage.USER_PROFILE_EDIT);
        for (Avatar avatar : avatars) {
            if (LisaApp.getInstance().getUser() != null
                    && LisaApp.getInstance().getUser().getAvatar() != null)
                avatar.setAvatarChecked(String.valueOf(avatar.getId()).equals(LisaApp.getInstance().getUser().getAvatar()));
        }
        intent.putExtra(USER_AVATARS, (Serializable) avatars);
        if (citiesResponse != null) {
            intent.putExtra(CITIES_RESPONSE_KEY, citiesResponse);
        }
        startActivity(intent);
    }
}