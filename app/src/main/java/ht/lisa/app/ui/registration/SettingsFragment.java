package ht.lisa.app.ui.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tapadoo.alerter.Alerter;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.util.BiometricUtil;
import ht.lisa.app.util.Constants;
import ht.lisa.app.util.SharedPreferencesUtil;

public class SettingsFragment extends BaseRegistrationFragment {
    public static final String SCREEN_NAME = "SettingsScreen";

    public static final String IS_FROM_SETTINGS = "isFromSettings";

    @BindView(R.id.change_pin)
    View changePin;
    @BindView(R.id.change_language)
    View changeLanguage;
    @BindView(R.id.security_word)
    View securityWord;
    @BindView(R.id.notification_chb)
    CheckBox notificationChb;
    @BindView(R.id.enable_face_id_chb)
    CheckBox enableFaceIdChb;
    @BindView(R.id.enable_touch_id_chb)
    CheckBox enableTouchIdChb;
    @BindView(R.id.imageViewLanguage)
    ImageView imageViewLanguage;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, parent, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            if (getArguments().getBoolean(PinCodeRegistrationFragment.PIN_CHANGED)) {
                Alerter alerter = Alerter.create(getActivity(), R.layout.dialog_top_sheet);
                alerter.setDuration(MainActivity.TOP_SHEET_DURATION);
                RelativeLayout layout = (RelativeLayout) alerter.getLayoutContainer();
                if (layout != null) {
                    ((TextView) alerter.getLayoutContainer().findViewById(R.id.top_sheet_message)).setText(getStringFromResource(R.string.pin_changed));
                    alerter.getLayoutContainer().findViewById(R.id.top_sheet_close_icon).setOnClickListener(view ->
                            Alerter.hide());
                    alerter.setBackgroundColorRes(R.color.accent);
                    alerter.show();
                    getArguments().putBoolean(PinCodeRegistrationFragment.PIN_CHANGED, false);
                }
            }
        }
        updateLanguageIcon();
        enableTouchIdChb.setVisibility(BiometricUtil.isSensorAvialable(getContext()) ? View.VISIBLE : View.GONE);
        setCheckChangeListener(notificationChb, false);
        setCheckChangeListener(enableFaceIdChb, false);
        setCheckChangeListener(enableTouchIdChb, false);

        updateNotificationsSelection();

        setCheckChangeListener(notificationChb, true);
        setCheckChangeListener(enableFaceIdChb, true);
        setCheckChangeListener(enableTouchIdChb, true);
    }

    private void updateNotificationsSelection() {
        notificationChb.setChecked(SharedPreferencesUtil.isNotificationsAllowed());
    }

    private void updateLanguageIcon() {
        switch (LisaApp.getInstance().getLanguage(getActivity())) {
            case Constants.LANGUAGE_EN:
                imageViewLanguage.setImageResource(R.drawable.ic_english_flag);
                break;
            case Constants.LANGUAGE_FR:
                imageViewLanguage.setImageResource(R.drawable.ic_france_flag);
                break;
            case Constants.LANGUAGE_KR:
                imageViewLanguage.setImageResource(R.drawable.ic_kreyol_flag);
                break;
        }
    }

    @OnClick({R.id.security_word, R.id.change_language, R.id.change_pin})
    void onSettingsItemClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_FROM_SETTINGS, true);
        switch (view.getId()) {
            case R.id.security_word:
                showNextFragment(BaseRegistrationFragment.newInstance(SecurityWordRegistrationFragment.class, bundle));
                break;

            case R.id.change_language:
                showNextFragment(BaseRegistrationFragment.newInstance(ChooseLanguageRegistrationFragment.class, bundle));
                break;

            case R.id.change_pin:
                showNextFragment(BaseRegistrationFragment.newInstance(PinCodeRegistrationFragment.class, bundle));
                break;

        }

    }

    private void setCheckChangeListener(CheckBox chb, boolean enable) {
        chb.setOnCheckedChangeListener(enable ? (CompoundButton.OnCheckedChangeListener) (buttonView, isChecked) -> {
            switch (chb.getId()) {
                case R.id.notification_chb:
                    SharedPreferencesUtil.setNotificationsAllowed(isChecked);
                    break;
                case R.id.enable_face_id_chb:

                    break;
                case R.id.enable_touch_id_chb:

                    break;
            }
        } : null);
    }

    @OnClick(R.id.back_arrow)
    void onBackArrowClick() {
        super.onBackArrowClick();
    }
}
