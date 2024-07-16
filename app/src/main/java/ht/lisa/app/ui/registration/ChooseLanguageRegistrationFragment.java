package ht.lisa.app.ui.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Guideline;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.util.Constants;
import ht.lisa.app.util.FragmentUtil;
import ht.lisa.app.util.ViewUtil;

import static ht.lisa.app.ui.registration.SettingsFragment.IS_FROM_SETTINGS;

public class ChooseLanguageRegistrationFragment extends BaseRegistrationFragment {
    public static final String SCREEN_NAME = "LanguageScreen";
    @BindView(R.id.choose_language_logo)
    ImageView chooseLanguageLogo;
    @BindView(R.id.choose_language_title)
    TextView chooseLanguageTitle;
    @BindView(R.id.choose_language_message)
    TextView chooseLanguageMessage;
    @BindView(R.id.choose_language_top_top_button_guideline)
    Guideline chooseLanguageTopTopButtonGuideline;
    @BindView(R.id.choose_language_top_bottom_button_guideline)
    Guideline chooseLanguageTopBottomButtonGuideline;
    @BindView(R.id.back_arrow)
    ImageButton backarrow;
    private boolean isFromSettings;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_language_registration, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            isFromSettings = getArguments().getBoolean(IS_FROM_SETTINGS);
        }
        setChooseLanguageLayout();
    }

    private void setChooseLanguageLayout() {
        backarrow.setVisibility(isFromSettings ? View.VISIBLE : View.GONE);
        chooseLanguageLogo.setVisibility(isFromSettings ? View.GONE : View.VISIBLE);
        chooseLanguageTitle.setVisibility(isFromSettings ? View.VISIBLE : View.GONE);
        chooseLanguageMessage.setVisibility(isFromSettings ? View.VISIBLE : View.GONE);
        ViewUtil.changeGuidelinePercent(chooseLanguageTopTopButtonGuideline, isFromSettings ? 0.43f : 0.67f);
        ViewUtil.changeGuidelinePercent(chooseLanguageTopBottomButtonGuideline, isFromSettings ? 0.57f : 0.81f);
    }

    @OnClick({R.id.choose_language_french, R.id.choose_language_kreyol, R.id.choose_language_english})
    void onFrenchButtonClick(View view) {
        String language;
        switch (view.getId()) {
            case R.id.choose_language_french:
                language = Constants.LANGUAGE_FR;
                break;
            case R.id.choose_language_kreyol:
                language = Constants.LANGUAGE_KR;
                break;
            default:
                language = Constants.LANGUAGE_EN;
        }
        LisaApp.getInstance().setLanguage(language, LisaApp.getInstance());
        if (getActivity() != null) {
            LisaApp.getInstance().setLanguage(language, getActivity());
        }


        if (isFromSettings) {
            FragmentUtil.replaceFragment(getFragmentManager(), new SettingsFragment(), false);
        } else {
            showNextFragment(BaseRegistrationFragment.newInstance(GetStartedRegistrationFragment.class, null));
        }

    }
}
