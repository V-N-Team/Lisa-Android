package ht.lisa.app.ui.registration;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.User;
import ht.lisa.app.model.response.ProfileResponse;
import ht.lisa.app.util.BiometricUtil;
import ht.lisa.app.util.EditTextUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextChange;

public class SecurityWordRegistrationFragment extends BaseRegistrationFragment {

    public static final String SCREEN_NAME = "SecurityWordScreen";
    private static final int TEXT_MIN_SIZE = 6;

    @BindView(R.id.security_word_registration_number_edittext)
    EditText securityWordRegistrationNumberEdittext;
    @BindView(R.id.security_word_registration_number_check)
    ImageView securityWordRegistrationNumberCheck;
    @BindView(R.id.security_word_registration_button_done)
    Button securityWordRegistrationButtonDone;
    @BindView(R.id.security_word_registration_button_skip)
    Button securityWordRegistrationButtonSkip;
    @BindView(R.id.security_word_registration_view)
    View securityWordRegistrationView;
    @BindView(R.id.security_word_error_text)
    TextView securityWordErrorText;
    @BindView(R.id.security_word_current)
    TextView securityWordCurrent;

    private boolean isFromSettings;
    private boolean isConfirmNewWord;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_security_word_registration, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTextChangedListener();

        if (getArguments() != null) {
            isFromSettings = getArguments().getBoolean(SettingsFragment.IS_FROM_SETTINGS);
        }
        setDataToSecretWordLayout();
    }

    private void setDataToSecretWordLayout() {
        securityWordRegistrationNumberEdittext.setText(LisaApp.getInstance().getUser().getSecret() == null ? "" : LisaApp.getInstance().getUser().getSecret());
        securityWordRegistrationButtonSkip.setVisibility(isFromSettings ? View.GONE : View.VISIBLE);
        securityWordRegistrationButtonDone.setBackground(getDrawableFromResource(isFromSettings ? R.drawable.rounded_button_orange_2 : R.drawable.ic_rectangle_grey_done_2));
        securityWordCurrent.setText(LisaApp.getInstance().getUser().getSecret() != null && !LisaApp.getInstance().getUser().getSecret().isEmpty() ? R.string.enter_current_security : isConfirmNewWord ? R.string.enter_new_security : R.string.create_a_security_word);
        changeDoneButtonState(securityWordRegistrationButtonDone, LisaApp.getInstance().getUser().getSecret() != null && !LisaApp.getInstance().getUser().getSecret().isEmpty());
    }

    private void setTextChangedListener() {
        securityWordRegistrationNumberEdittext.addTextChangedListener(new TextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                changeDoneButtonState(securityWordRegistrationButtonDone, editable.toString().length() >= TEXT_MIN_SIZE);
                changeCheckImageState(isTextMinSize(), false);
                checkErrorState(false);
            }
        });
    }

    public void changeDoneButtonState(Button button, boolean isEnable) {
        button.setBackground(isEnable ? getDrawableFromResource(isFromSettings ? R.drawable.rounded_button_orange_2 : R.drawable.ic_rectangle_orange_done) : getDrawableFromResource(isFromSettings ? R.drawable.rounded_button_orange_2 : R.drawable.ic_rectangle_grey_done_2));
    }

    private void changeCheckImageState(boolean isTextMinSize, boolean isButtonClick) {
        securityWordRegistrationNumberCheck.setImageResource(isTextMinSize ? R.drawable.ic_accept_mark : isButtonClick ? R.drawable.ic_error_mark : 0);
    }

    private boolean isTextMinSize() {
        return EditTextUtil.isTextMinSize(securityWordRegistrationNumberEdittext, TEXT_MIN_SIZE);
    }

    private void showBiometricDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.fingerprint_authentification)
                .setMessage(R.string.fingerprint_verify)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    chooseBiometricLogin(true);
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.no, (dialog, i) -> {
                    chooseBiometricLogin(false);
                    dialog.dismiss();
                })
                .setIcon(R.drawable.fingerprint)
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColorFromResource(R.color.colorAccent));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColorFromResource(R.color.colorAccent));
    }

    private void chooseBiometricLogin(boolean isAccepted) {
        if (isAccepted) {
            startBiometricLogin();
        } else {
            SharedPreferencesUtil.setBiometricAuthentication(false);
            showNextFragment(BaseRegistrationFragment.newInstance(OverviewFragment.class, null));
        }
    }

    private void startBiometricLogin() {
        BiometricUtil biometricUtil = new BiometricUtil(getContext(), this);
        biometricUtil.startBiometricManager();
    }

    private void checkErrorState(boolean isButtonClick) {
        if (isButtonClick) {
            securityWordErrorText.setVisibility(isTextMinSize() ? View.GONE : View.VISIBLE);
            securityWordRegistrationView.setBackgroundColor(isTextMinSize() ? getColorFromResource(R.color.inactiveBG) : getColorFromResource(R.color.red));
            changeCheckImageState(isTextMinSize(), true);
        } else {
            securityWordErrorText.setVisibility(View.GONE);
            securityWordRegistrationView.setBackground(isFromSettings ? getDrawableFromResource(R.drawable.rounded_button_orange_2) : getDrawableFromResource(R.drawable.ic_rectangle_grey_done_2));
        }
    }

    @Override
    public void getData(Object object) {
        if (object instanceof ProfileResponse) {
            ProfileResponse response = (ProfileResponse) object;
            if (response.getState() == 0) {
                if (isFromSettings) {
                    showNextFragment(BaseRegistrationFragment.newInstance(SettingsFragment.class, null));
                } else {
                    showNextFragment(BaseRegistrationFragment.newInstance(OverviewFragment.class, null));
                }


            }
        }
    }

    @OnClick({R.id.security_word_registration_button_done, R.id.security_word_registration_button_skip})
    void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.security_word_registration_button_done:
                if (!isTextMinSize()) {
                    checkErrorState(true);
                    return;
                }
                if (!isFromSettings || isConfirmNewWord) {
                    User user = new User();
                    user.setPhone(SharedPreferencesUtil.getPhone());
                    user.setSecret(securityWordRegistrationNumberEdittext.getText().toString());
                    registrationPresenter.getProfile(user);
                    return;
                }
                if (LisaApp.getInstance().getUser().getSecret() != null && !LisaApp.getInstance().getUser().getSecret().isEmpty()) {
                    if (securityWordRegistrationNumberEdittext.getText().toString().equals(LisaApp.getInstance().getUser().getSecret())) {
                        securityWordRegistrationNumberEdittext.setText("");
                        LisaApp.getInstance().getUser().setSecret("");
                        isConfirmNewWord = true;
                        setDataToSecretWordLayout();
                    } else {
                        showMessage(getStringFromResource(R.string.current_security_different));
                    }
                } else {
                    User user = new User();
                    user.setPhone(SharedPreferencesUtil.getPhone());
                    user.setSecret(securityWordRegistrationNumberEdittext.getText().toString());
                    registrationPresenter.getProfile(user);
                }
                break;

            case R.id.security_word_registration_button_skip:
                showNextFragment(BaseRegistrationFragment.newInstance(OverviewFragment.class, null));
                break;

        }


    }


}
