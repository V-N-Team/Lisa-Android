package ht.lisa.app.ui.registration;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.DialogFragment;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.R;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.Pin;
import ht.lisa.app.model.PinChange;
import ht.lisa.app.model.response.PinResponse;
import ht.lisa.app.model.response.UserInfoResponse;
import ht.lisa.app.ui.component.PinView;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.util.BiometricUtil;
import ht.lisa.app.util.OnDismissDialogInterface;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextUtil;
import ht.lisa.app.util.ViewUtil;

public class ResetPinCodeDialog extends BaseRegistrationDialogFragment implements PinView.OnPinEnteredListener {

    public static final String PIN_CHANGED = "pinChanged";
    private static final int PIN_ATTEMPTS = 3;

    private static final int CREATE_PIN = 0;
    private static final int CONFIRM_NEW_PIN = 1;
    private static final int ENTER_EXISTING_PIN = 3;
    private static final int ENTER_CURRENT_PIN = 4;
    private static final int ENTER_NEW_PIN = 5;

    @BindView(R.id.pincode_registration_button)
    Button pincodeRegistrationButton;
    @BindView(R.id.pincode_registration_title)
    TextView pinCodeRegistrationTitle;
    @BindView(R.id.pincode_registration_message)
    TextView pinCodeRegistrationMessage;
    @BindView(R.id.pincode_registration_middle_guideline)
    Guideline pinCodeRegistrationMiddleGuideline;
    @BindView(R.id.pincode_registration_card_bottom_guideline)
    Guideline pinCodeRegistrationCardBottomGuideline;
    @BindView(R.id.error_pin_code_text)
    TextView errorPinCodeText;
    @BindView(R.id.pinView)
    PinView pinView;
    @BindView(R.id.back_arrow)
    ImageButton backArrow;
    @BindView(R.id.pincode_registration_existing_pin_info)
    ImageView pinCodeRegistrationExistingPinInfo;

    private boolean isExistingPinCode;
    private int attempts;
    private String currentPin;
    private int screenName;
    private OnDismissDialogInterface onDismissDialogInterface;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pin_code_registration, parent, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pinView.setOnPinEnteredListener(this);
        attempts = PIN_ATTEMPTS;
        if (isExistingPinCode && SharedPreferencesUtil.isBiometricAuthenticated() && BiometricUtil.isSensorAvialable(getContext())) {
            startBiometricLogin();
        }
        pinView.setPinEnabled(false);
        if (SharedPreferencesUtil.getToken() != null && !SharedPreferencesUtil.getToken().isEmpty()) {
            registrationPresenter.getUserInfo(null, new Phone(SharedPreferencesUtil.getPhone()));
        } else {
            setDataToViews();
        }
        backArrow.setVisibility(View.GONE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        pinView.clear();
        pinView.showKeyBoard();

        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            if (getActivity() == null) return;
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            if (dialog.getWindow() == null) return;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

    }


    private void setDataToViews() {
        pinCodeRegistrationTitle.setText(isExistingPinCode ? getStringFromResource(R.string.enter_existing_pin) : getStringFromResource(R.string.create_a_pin_code));
        pinCodeRegistrationMessage.setText(isExistingPinCode ? getStringFromResource(R.string.enter_existing_pin_message) : getStringFromResource(R.string.create_your_secured_pin_code_to_hold_n_your_account_safe));
        pinCodeRegistrationExistingPinInfo.setVisibility(isExistingPinCode ? View.VISIBLE : View.GONE);
    }

    private void startBiometricLogin() {
        BiometricUtil biometricUtil = new BiometricUtil(getContext(), this);
        biometricUtil.startBiometricManager();
    }

    private void onPinCodeCheck() {
        hideProgress();
        pinView.showKeyBoard();
        if (attempts == 0) {
            setClickableTexts();
            attempts = PIN_ATTEMPTS;
        } else {
            attempts--;
        }
    }

    private void showPinCodeCorrectness(int state) {
        errorPinCodeText.setVisibility(state != -1 ? View.GONE : View.VISIBLE);
        if (state != -3) {
            String errorText = state == -2 ? getStringFromResource(R.string.pin_code_identical) :
                    getStringFromResource(R.string.invalid_pincode_you_have) + attempts + getStringFromResource(attempts == 1 ? R.string.attempt : R.string.attempts);
            errorPinCodeText.setText(errorText);
            errorPinCodeText.setTextColor(getColorFromResource(R.color.red));
        }
        ViewUtil.changeGuidelinePercent(pinCodeRegistrationCardBottomGuideline, state != -1 ? 0.8f : 0.69f);
        pincodeRegistrationButton.setVisibility(state != -1 ? View.VISIBLE : View.GONE);
        pinView.clear();
        pinView.setFieldsError(state != 0);
    }

    private void setClickableTexts() {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                showForgotPinDialog();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getColorFromResource(R.color.reSendCode));
            }
        };
        pinView.setPinEnabled(false);
        pinView.hideKeyBoard();
        String errorText = getStringFromResource(R.string.attempts_over) + getStringFromResource(R.string.forgot_pin);
        errorPinCodeText.setText(errorText);
        errorPinCodeText.setTextColor(getColorFromResource(R.color.grey));
        TextUtil.createClickableSpan(errorPinCodeText, errorPinCodeText.getText().length() - getStringFromResource(R.string.forgot_pin).length(), clickableSpan);
    }

    private void showForgotPinDialog() {
        if (getFragmentManager() == null) return;
        ForgotPinDialog.newInstance().show(getFragmentManager(), ForgotPinDialog.class.getSimpleName());
    }

    @Override
    public void getData(Object object) {
        if (object instanceof UserInfoResponse) {
            UserInfoResponse response = (UserInfoResponse) object;
            if (response.getState() == 0) {
                isExistingPinCode = response.getPinActive() == 1;
            } else if (response.getState() == 2) {
                isExistingPinCode = true;
            }
            setDataToViews();
            pinView.setPinEnabled(true);
            screenName = isExistingPinCode ? ENTER_EXISTING_PIN : CREATE_PIN;
        } else if (object instanceof PinResponse) {
            PinResponse response = (PinResponse) object;
            showPinCodeCorrectness(response.getState());
            if (response.getState() == 0) {
                switch (screenName) {
                    case CREATE_PIN:
                        pinView.clear();
                        pinCodeRegistrationTitle.setText(getStringFromResource(R.string.confirm_pin_code));
                        screenName = CONFIRM_NEW_PIN;
                        break;

                    case CONFIRM_NEW_PIN:
                        if ((SharedPreferencesUtil.getSecretWord() == null || SharedPreferencesUtil.getSecretWord().isEmpty()) && SharedPreferencesUtil.isGetStarted()) {
                            showNextFragment(BaseRegistrationFragment.newInstance(SecurityWordRegistrationFragment.class, null));
                        } else {
                            startActivity(new Intent(getContext(), MainActivity.class));
                        }
                        break;

                    case ENTER_EXISTING_PIN:
                        onDismissDialogInterface.onDismissed();
                        dismiss();
                        break;


                    case ENTER_CURRENT_PIN:
                        pinCodeRegistrationTitle.setText(getStringFromResource(R.string.enter_new_pin));
                        screenName = ENTER_NEW_PIN;
                        break;

                    case ENTER_NEW_PIN:
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(PIN_CHANGED, true);
                        showNextFragment(BaseRegistrationFragment.newInstance(SettingsFragment.class, bundle));
                        break;

                }
            } else if (response.getState() == -1) {
                onPinCodeCheck();
            }
        }
    }

    @OnClick({R.id.pincode_registration_button, R.id.pincode_registration_existing_pin_info})
    void onPinCodeButtonClick(View view) {
        switch (view.getId()) {
            case R.id.pincode_registration_button:
                if (!pinView.isCurrentPinFull()) return;
                switch (screenName) {
                    case CREATE_PIN:
                        registrationPresenter.createPin(null, new Pin(SharedPreferencesUtil.getPhone(), pinView.getEnteredPin()), pinView);
                        break;

                    case ENTER_EXISTING_PIN:
                    case CONFIRM_NEW_PIN:
                    case ENTER_CURRENT_PIN:
                        registrationPresenter.authorizePin(new Pin(SharedPreferencesUtil.getPhone(), pinView.getEnteredPin()), pinView);
                        currentPin = pinView.getEnteredPin();
                        break;

                    case ENTER_NEW_PIN:
                        if (currentPin != null && !currentPin.isEmpty()) {
                            registrationPresenter.changePin(new PinChange(SharedPreferencesUtil.getPhone(), currentPin, pinView.getEnteredPin()), pinView);
                        }
                        break;

                }
                break;

            case R.id.pincode_registration_existing_pin_info:
                if (getActivity() == null) return;
                if (getFragmentManager() != null) {
                    InfoDialog.newInstance(getStringFromResource(R.string.existing_pin), getStringFromResource(R.string.the_existing_lisa_pin_is_a))
                            .show(getFragmentManager(), InfoDialog.class.getSimpleName());
                }
                break;
        }

    }

    @Override
    public void onPinEntered(String pin) {
        pinView.hideKeyBoard();
        changeButtonState(pincodeRegistrationButton, true);
    }

    @Override
    public void onPinChanged() {
        changeButtonState(pincodeRegistrationButton, false);
        if (pinView.getEnteredPin().length() != 0) {
            errorPinCodeText.setVisibility(View.GONE);
            ViewUtil.changeGuidelinePercent(pinCodeRegistrationCardBottomGuideline, 0.8f);
            pincodeRegistrationButton.setVisibility(View.VISIBLE);
        }
    }

    public void setOnDismissDialogInterface(OnDismissDialogInterface onDismissDialogInterface) {
        this.onDismissDialogInterface = onDismissDialogInterface;
    }
}
