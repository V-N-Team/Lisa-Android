package ht.lisa.app.ui.userprofile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tapadoo.alerter.Alerter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.Avatar;
import ht.lisa.app.model.City;
import ht.lisa.app.model.User;
import ht.lisa.app.model.response.AvatarResponse;
import ht.lisa.app.model.response.CitiesResponse;
import ht.lisa.app.model.response.ProfileResponse;
import ht.lisa.app.ui.datepicker.DatePickerDialog;
import ht.lisa.app.ui.datepicker.SpinnerDatePickerDialogBuilder;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.RxUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextChange;

public class UserProfileEditFragment extends BaseWalletFragment {
    public static final String SCREEN_NAME = "EditProfileFragment";

    public static final String USER_AVATARS = "userAvatars";
    public static final String CITIES_RESPONSE_KEY = "citiesResponseKey";

    @BindView(R.id.profile_edit_main_logo)
    ImageView profileEditMainLogo;
    @BindView(R.id.profile_edit_avatar_edit)
    ImageView profileEditAvatarEdit;
    @BindView(R.id.profile_edit_name)
    EditText profileEditName;
    @BindView(R.id.profile_edit_last_name)
    EditText profileEditLastName;
    @BindView(R.id.profile_edit_birthday)
    TextView profileEditBirthday;
    @BindView(R.id.profile_edit_male)
    RadioButton profileEditMale;
    @BindView(R.id.location_error)
    TextView locationError;
    @BindView(R.id.profile_edit_female)
    RadioButton profileEditFemale;
    @BindView(R.id.profile_edit_location)
    InstantAutoComplete profileEditLocation;
    @BindView(R.id.profile_edit_email)
    EditText profileEditEmail;
    @BindView(R.id.profile_edit_facebook)
    EditText profileEditFacebook;
    @BindView(R.id.profile_edit_insta)
    EditText profileEditInsta;
    @BindView(R.id.profile_edit_button)
    Button profileEditButton;
    @BindView(R.id.social_media_chb)
    CheckBox profileEditSocialMediaChb;
    @BindView(R.id.location_line)
    View locationLine;
    Date birthday;

    Calendar c = Calendar.getInstance();
    private ArrayList<Avatar> avatars;
    private int newAvatarId;
    private int newCityId;
    private CitiesResponse citiesResponse;
    private City currentCity;
    private User user;

    public static UserProfileEditFragment newInstance(ArrayList<Avatar> avatars, CitiesResponse citiesResponse) {
        UserProfileEditFragment userProfileEditFragment = new UserProfileEditFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_AVATARS, avatars);
        bundle.putSerializable(CITIES_RESPONSE_KEY, citiesResponse);
        userProfileEditFragment.setArguments(bundle);
        return userProfileEditFragment;
    }

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile_edit, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            avatars = (ArrayList<Avatar>) getArguments().getSerializable(USER_AVATARS);
            citiesResponse = (CitiesResponse) getArguments().getSerializable(CITIES_RESPONSE_KEY);
            if (citiesResponse == null) {
                walletPresenter.getHaitianCities();
                showProgress();
            }
            if (avatars == null || avatars.isEmpty()) {
                RxUtil.delayedConsumer(1000, aLong -> {
                    walletPresenter.getAvatarList();
                    showProgress();
                });
            }
        }
        user = LisaApp.getInstance().getUser();
        if (user != null) {
            setUserProfileData();
            setTextChangeListeners();
            setGenderRBCheckListeners();
            checkSocialMedia(null);
            showBirthdayAlertIfNeeded();
        } else {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        }
    }


    private void showBirthdayAlertIfNeeded() {
        if (!SharedPreferencesUtil.isNotNeedToShowBdayAlert()) {

            Alerter alerter = Alerter.create(getActivity(), R.layout.dialog_top_sheet);
            alerter.setDuration(5000);
            RelativeLayout layout = (RelativeLayout) alerter.getLayoutContainer();
            if (layout != null) {
                ((TextView) alerter.getLayoutContainer().findViewById(R.id.top_sheet_message)).setText(R.string.profile_birthday_alert);
                alerter.getLayoutContainer().findViewById(R.id.top_sheet_close_icon).setOnClickListener(v ->
                        Alerter.hide());
                alerter.setBackgroundColorRes(R.color.colorAccent);
                alerter.show();
                SharedPreferencesUtil.setNotNeedToShowBdayAlert(true);
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void setUserProfileData() {
        profileEditName.setText(user.getName());
        profileEditLastName.setText(user.getSurname());
        if (LisaApp.getInstance().getReadonlyFields() != null && LisaApp.getInstance().getReadonlyFields().contains(ProfileResponse.READONLY_DOB)) {
            profileEditBirthday.setClickable(false);
            profileEditBirthday.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.inactive_dob_bg));
        }
        profileEditBirthday.setText(user.getFormattedDob() == null ? "" : user.getFormattedDob());
        try {
            if (user.getDob() != null) {
                birthday = new SimpleDateFormat(User.DOB_FORMAT).parse(user.getDob());
            } else {
                birthday = Calendar.getInstance().getTime();
            }

            if (birthday != null) {
                c.setTime(birthday);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        profileEditEmail.setText(user.getEmail() == null ? "" : user.getEmail());
        profileEditFacebook.setText(user.getFb() == null ? "" : user.getFb());
        profileEditInsta.setText(user.getIg() == null ? "" : user.getIg());
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            newAvatarId = Integer.parseInt(user.getAvatar());
            Glide.with(UserProfileEditFragment.this)
                    .load(Avatar.getAvatarUrl(avatars, Integer.parseInt(user.getAvatar())))
                    .circleCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .into(profileEditMainLogo);
        }
        if (user.getSex() != null) {
            profileEditMale.setChecked(user.getSex().equals(User.Sex.M.name()));
            profileEditFemale.setChecked(!user.getSex().equals(User.Sex.M.name()));
        }
        initCitiesList();
    }

    private void initCitiesList() {
        if (user != null && citiesResponse != null && citiesResponse.getCities() != null) {
            for (City city : citiesResponse.getCities()) {
                if (user.getCity() != null && !user.getCity().isEmpty() && city.getId() == Integer.parseInt(user.getCity())) {
                    profileEditLocation.setText(city.getCity());
                    currentCity = city;
                }
            }
            changeButtonState(profileEditButton, false);
            if (getActivity() == null) return;
            profileEditLocation.setAdapter(new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line, citiesResponse.getCityNameList()));
        }
    }

    private void setTextChangeListeners() {
        setTextChangeListener(profileEditBirthday);
        setTextChangeListener(profileEditEmail);
        setTextChangeListener(profileEditName);
        setTextChangeListener(profileEditFacebook);
        setTextChangeListener(profileEditInsta);
        setTextChangeListener(profileEditLocation);
        setTextChangeListener(profileEditLastName);
    }

    private void setTextChangeListener(TextView view) {
        view.addTextChangedListener(new TextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkErrorState(false);
                changeButtonState(profileEditButton, areFieldsChanged());
                checkSocialMedia(view);
            }
        });
    }

    private void checkSocialMedia(TextView view) {
        if (getContext() == null) return;
        profileEditSocialMediaChb.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (!checked) {
                profileEditFacebook.setText("");
                profileEditInsta.setText("");
            }
        });
        profileEditSocialMediaChb.setChecked(!profileEditFacebook.getText().toString().isEmpty() || !profileEditInsta.getText().toString().isEmpty());
        Drawable facebookGrey = AppCompatResources.getDrawable(getContext(), R.drawable.ic_fb_grey);
        Drawable instaGrey = AppCompatResources.getDrawable(getContext(), R.drawable.ic_insta_grey);
        Drawable facebook = AppCompatResources.getDrawable(getContext(), R.drawable.ic_fb);
        Drawable insta = AppCompatResources.getDrawable(getContext(), R.drawable.ic_insta);
        profileEditFacebook.setCompoundDrawablesWithIntrinsicBounds(profileEditFacebook.getText().toString().isEmpty() ? facebookGrey : facebook, null, null, null);
        profileEditInsta.setCompoundDrawablesWithIntrinsicBounds(profileEditInsta.getText().toString().isEmpty() ? instaGrey : insta, null, null, null);
    }

    private void setGenderRBCheckListeners() {
        profileEditMale.setOnCheckedChangeListener((buttonView, isChecked) -> {
            profileEditFemale.setChecked(!isChecked);
            changeButtonState(profileEditButton, areFieldsChanged());
        });

        profileEditFemale.setOnCheckedChangeListener((buttonView, isChecked) -> {
            profileEditMale.setChecked(!isChecked);
            changeButtonState(profileEditButton, areFieldsChanged());
        });
    }

    @Override
    public void getData(Object object) {
        if (object instanceof ProfileResponse) {
            ProfileResponse response = (ProfileResponse) object;
            LisaApp.getInstance().setUser(response.getUser());
            LisaApp.getInstance().setReadonlyFields(response.getReadonly());
            if (response.getState() == 0) {
                RxUtil.delayedConsumer(500, aLong -> {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra(MainActivity.MAIN_ACTIVITY, MainActivity.USER_PROFILE_MAIN);
                    startActivity(intent);
                });
            }
        } else if (object instanceof CitiesResponse) {
            hideProgress();
            citiesResponse = (CitiesResponse) object;
            initCitiesList();
        } else if (object instanceof AvatarResponse) {
            AvatarResponse response = (AvatarResponse) object;
            hideProgress();
            avatars.addAll(response.getAvatars());
            setUserProfileData();
        }
    }

    private User getEditedUser() {
        User user = new User();
        user.setPhone(SharedPreferencesUtil.getPhone());
        user.setName(profileEditName.getText().toString().replace(" ", "-"));
        user.setSurname(profileEditLastName.getText().toString().replace(" ", "-"));
        user.setDob(profileEditBirthday.getText().toString());
        user.setSex(profileEditMale.isChecked() ? User.Sex.M.name() : profileEditFemale.isChecked() ? User.Sex.F.name() : null);
        user.setEmail(profileEditEmail.getText().toString());
        user.setIg(profileEditInsta.getText().toString());
        user.setFb(profileEditFacebook.getText().toString());
        user.setCity(String.valueOf(newCityId));
        user.setAvatar(String.valueOf(newAvatarId));
        return user;
    }

    @OnClick(R.id.profile_edit_birthday)
    void onEditBirthdayClick() {
        if (birthday != null) {
            c.setTime(birthday);
        } else {
            c.setTime(new Date());
        }
        Locale.setDefault(new Locale("fr", "HT"));

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, -18);
        int yearBefore18 = cal.get(Calendar.YEAR);
        int monthBefore18 = cal.get(Calendar.MONTH);
        int dayBefore18 = cal.get(Calendar.DATE);

        cal.setTime(new Date());
        cal.add(Calendar.YEAR, -120);
        int yearBefore120 = cal.get(Calendar.YEAR);
        int monthBefore120 = cal.get(Calendar.MONTH);
        int dayBefore120 = cal.get(Calendar.DATE);

        DatePickerDialog dialog = new SpinnerDatePickerDialogBuilder()
                .context(getContext())
                .callback((view, year, monthOfYear, dayOfMonth) -> {
                    c.set(year, monthOfYear, dayOfMonth);
                    profileEditBirthday.setText(new SimpleDateFormat(User.DOB_FORMATTED, Locale.getDefault()).format(c.getTime()));
                })
                .showTitle(false)
                .showDaySpinner(true)
                .defaultDate(yearBefore18, monthBefore18, dayBefore18)
                .maxDate(yearBefore18, monthBefore18, dayBefore18)
                .minDate(yearBefore120, monthBefore120, dayBefore120)
                .build();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.GREEN);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
    }


    @OnClick(R.id.profile_edit_avatar_edit)
    void onEditAvatarClick() {
        ChooseAvatarDialog chooseAvatarDialog = new ChooseAvatarDialog(avatars);
        chooseAvatarDialog.setOnChooseAvatarListener(id -> {
            Glide.with(UserProfileEditFragment.this)
                    .load(Avatar.getAvatarUrl(avatars, id))
                    .circleCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .into(profileEditMainLogo);
            newAvatarId = id;
            changeButtonState(profileEditButton, areFieldsChanged());
        });

        if (getFragmentManager() != null) {
            chooseAvatarDialog.show(getFragmentManager(), ChooseAvatarDialog.class.getSimpleName());
        }

    }

    private boolean areFieldsChanged() {
        return !profileEditBirthday.getText().toString().equals(user.getFormattedDob() == null ? "" : user.getFormattedDob())
                || profileEditMale.isChecked() != (user.getSex() == null ? "" : user.getSex()).equals(User.Sex.M.name())
                || profileEditFemale.isChecked() != (user.getSex() == null ? "" : user.getSex()).equals(User.Sex.F.name())
                || !profileEditEmail.getText().toString().equals(user.getEmail() == null ? "" : user.getEmail())
                || !profileEditName.getText().toString().equals(user.getName() == null ? "" : user.getName())
                || !profileEditLastName.getText().toString().equals(user.getSurname() == null ? "" : user.getSurname())
                || !profileEditFacebook.getText().toString().equals(user.getFb() == null ? "" : user.getFb())
                || !profileEditInsta.getText().toString().equals(user.getFb() == null ? "" : user.getFb())
                || !profileEditLocation.getText().toString().equals(currentCity != null ? currentCity.getCity() == null ? "" : currentCity.getCity() : "")
                || newAvatarId != Integer.parseInt(user.getAvatar() == null ? "0" : user.getAvatar());
    }

    private void checkErrorState(boolean isButtonClick) {
        if (isButtonClick) {
            locationError.setVisibility(View.VISIBLE);
            locationLine.setBackgroundColor(getColorFromResource(R.color.red));
        } else {
            locationError.setVisibility(View.GONE);
            locationLine.setBackgroundColor(getColorFromResource(R.color.inactive));
        }
    }

    @OnClick(R.id.profile_edit_button)
    void onEditProfileButtonClick() {
        if (areFieldsChanged()) {
            if (citiesResponse == null || citiesResponse.getCities() == null) {
                walletPresenter.getHaitianCities();
                return;
            }

            for (int i = 0; i < citiesResponse.getCities().size(); i++) {
                if (profileEditLocation.getText().toString().equals(citiesResponse.getCities().get(i).getCity())) {
                    newCityId = citiesResponse.getCities().get(i).getId();
                    walletPresenter.editProfile(getEditedUser());
                    return;
                }
            }
            checkErrorState(true);
        }
    }
}
