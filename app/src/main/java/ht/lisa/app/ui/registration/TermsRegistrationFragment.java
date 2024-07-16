package ht.lisa.app.ui.registration;

import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.ui.main.WebViewActivity;
import ht.lisa.app.util.Constants;
import ht.lisa.app.util.TextUtil;

public class TermsRegistrationFragment extends BaseRegistrationFragment {

    @BindView(R.id.terms_registration_terms_checkbox)
    CheckBox termsRegistrationTermsCheckbox;
    @BindView(R.id.terms_registration_policy_checkbox)
    CheckBox termsRegistrationPolicyCheckbox;
    @BindView(R.id.terms_registration_terms_textview)
    TextView termsRegistrationTermsTextView;
    @BindView(R.id.terms_registration_policy_textview)
    TextView termsRegistrationPolicyTextView;
    @BindView(R.id.terms_registration_button)
    Button termsRegistrationButton;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_terms_registration, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setClickableTexts();
        setOnCheckChangeListeners();
    }

    private void setClickableTexts() {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                if (view.getId() == termsRegistrationTermsTextView.getId()) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(TermsAgreementRegistrationFragment.IS_SERVICE_TERM, view.getId() == termsRegistrationTermsTextView.getId());
                    bundle.putBoolean(TermsAgreementRegistrationFragment.IS_PRIVACY_TERM, view.getId() == termsRegistrationPolicyTextView.getId());
                    showNextFragment(newInstance(TermsAgreementRegistrationFragment.class, bundle));
                } else {
                    WebViewActivity.openUrl(getContext(), "http://lisa.s7.devpreviewr.com/privacy-policies/");
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getColorFromResource(R.color.colorPrimaryDark));
            }
        };

        TextUtil.createClickableSpan(termsRegistrationTermsTextView, termsRegistrationTermsTextView.getText().length() - getStringFromResource(R.string.terms_of_service).length(), clickableSpan);
        TextUtil.createClickableSpan(termsRegistrationPolicyTextView, termsRegistrationPolicyTextView.getText().length() - getStringFromResource(R.string.privacy_policy).length(), clickableSpan);

        /*if (LisaApp.getInstance().isFrench()) {
            TextUtil.createClickableSpan(termsRegistrationTermsTextView, termsRegistrationTermsTextView.getText().length() - getStringFromResource(R.string.terms_of_service_fr).length(), clickableSpan);
            TextUtil.createClickableSpan(termsRegistrationPolicyTextView, termsRegistrationPolicyTextView.getText().length() - getStringFromResource(R.string.privacy_policy_fr).length(), clickableSpan);
        } else if (LisaApp.getInstance().isKreyol()) {
            TextUtil.createClickableSpan(termsRegistrationTermsTextView, termsRegistrationTermsTextView.getText().length() - getStringFromResource(R.string.terms_of_service_kr).toLowerCase().length(), clickableSpan);
            TextUtil.createClickableSpan(termsRegistrationPolicyTextView, termsRegistrationPolicyTextView.getText().length() - getStringFromResource(R.string.privacy_policy_kr).length(), clickableSpan);
        } else {
            TextUtil.createClickableSpan(termsRegistrationTermsTextView, termsRegistrationTermsTextView.getText().length() - getStringFromResource(R.string.terms_of_service).toLowerCase().length(), clickableSpan);
            TextUtil.createClickableSpan(termsRegistrationPolicyTextView, termsRegistrationPolicyTextView.getText().length() - getStringFromResource(R.string.privacy_policy).length(), clickableSpan);
        }*/
    }

    private void setOnCheckChangeListeners() {
        termsRegistrationTermsCheckbox.setOnCheckedChangeListener((compoundButton, checked) -> {
            setUncheckedFieldsError(false);
            changeButtonState(termsRegistrationButton, isTermsPrivacyChecked());
        });
        termsRegistrationPolicyCheckbox.setOnCheckedChangeListener((compoundButton, checked) -> {
            setUncheckedFieldsError(false);
            changeButtonState(termsRegistrationButton, isTermsPrivacyChecked());
        });
    }

    private boolean isTermsPrivacyChecked() {
        return termsRegistrationTermsCheckbox.isChecked() && termsRegistrationPolicyCheckbox.isChecked();
    }

    private void setUncheckedFieldsError(boolean isButtonClick) {
        if (isButtonClick) {
            termsRegistrationTermsCheckbox.setButtonDrawable(termsRegistrationTermsCheckbox.isChecked() ? R.drawable.checkbox_selector : R.drawable.ic_error_checkbox);
            termsRegistrationPolicyCheckbox.setButtonDrawable(termsRegistrationPolicyCheckbox.isChecked() ? R.drawable.checkbox_selector : R.drawable.ic_error_checkbox);
        } else {
            termsRegistrationTermsCheckbox.setButtonDrawable(R.drawable.checkbox_selector);
            termsRegistrationPolicyCheckbox.setButtonDrawable(R.drawable.checkbox_selector);
        }

    }

    @OnClick(R.id.terms_registration_button)
    void onTermsRegistrationClick() {
        if (isTermsPrivacyChecked()) {
            termsRegistrationTermsCheckbox.setChecked(false);
            termsRegistrationPolicyCheckbox.setChecked(false);
            showNextFragment(newInstance(PinCodeRegistrationFragment.class, null));
        } else {
            setUncheckedFieldsError(true);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        cancelCountdown();
    }
}
