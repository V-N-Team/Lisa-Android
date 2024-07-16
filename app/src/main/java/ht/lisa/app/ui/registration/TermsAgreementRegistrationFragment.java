package ht.lisa.app.ui.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import butterknife.BindView;
import ht.lisa.app.R;
import ht.lisa.app.ui.main.WebViewActivity;

public class TermsAgreementRegistrationFragment extends BaseRegistrationFragment {

    public static final String SCREEN_NAME = "TermsScreen";

    public static final String IS_SERVICE_TERM = "isServiceTerms";
    public static final String IS_PAYMENT_TERM = "isPaymentTerms";
    public static final String IS_PRIVACY_TERM = "isPrivacyTerms";

    @BindView(R.id.terms_agreement_title)
    TextView termsAgreementTitle;
    @BindView(R.id.terms_text)
    TextView termsText;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_terms_agreement_registration, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean isServiceTerms = false;
        boolean isPaymentTerms = false;
        boolean isPrivacyTerms = false;
        if (getArguments() != null) {
            isServiceTerms = getArguments().getBoolean(IS_SERVICE_TERM);
            isPaymentTerms = getArguments().getBoolean(IS_PAYMENT_TERM);
            isPrivacyTerms = getArguments().getBoolean(IS_PRIVACY_TERM);
        }
        if (isServiceTerms) {
            termsAgreementTitle.setText(getStringFromResource(R.string.terms_agreement));
            termsText.setText(R.string.terms_service_text);
        } else if (isPaymentTerms) {
            termsAgreementTitle.setText(getStringFromResource(R.string.terms_of_payment));
            termsText.setText(R.string.terms_payment_text);
            termsAgreementTitle.setOnClickListener(v -> WebViewActivity.openUrl(getContext(), "http://lisa.s7.devpreviewr.com/terms-of-payment/"));
        } else if (isPrivacyTerms) {
            termsAgreementTitle.setText(getStringFromResource(R.string.privacy_policy));
            termsAgreementTitle.setOnClickListener(v -> WebViewActivity.openUrl(getContext(), "http://lisa.s7.devpreviewr.com/privacy-policies/"));
            termsText.setText(R.string.privacy_policy_text);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        cancelCountdown();
    }

    @Override
    void onBackArrowClick() {
        getFragmentManager().popBackStack();
    }
}
