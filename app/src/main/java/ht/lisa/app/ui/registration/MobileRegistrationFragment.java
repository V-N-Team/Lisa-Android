package ht.lisa.app.ui.registration;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Guideline;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.R;
import ht.lisa.app.model.Device;
import ht.lisa.app.model.response.BindDeviceResponse;
import ht.lisa.app.ui.component.PhoneEditView;
import ht.lisa.app.util.Constants;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.ViewUtil;

public class MobileRegistrationFragment extends BaseRegistrationFragment {

    private static final int START_TIME = 30000;

    @BindView(R.id.mobile_registration_number_edittext)
    PhoneEditView mobileRegistrationNumberEditText;
    @BindView(R.id.mobile_registration_button)
    Button mobileRegistrationButton;
    @BindView(R.id.mobile_registration_error_text)
    TextView mobileRegistrationErrorText;
    @BindView(R.id.mobile_registration_change_phone_text)
    TextView mobileRegistrationChangePhoneText;
    @BindView(R.id.mobile_registration_card_bottom_guideline)
    Guideline mobileRegistrationCardBottomGuideline;
    @BindView(R.id.mobile_registration_card_inner_top_guideline)
    Guideline mobileRegistrationCardInnerTopGuideline;
    @BindView(R.id.mobile_registration_card_inner_central_guideline)
    Guideline mobileRegistrationCardInnerCentralGuideline;
    @BindView(R.id.mobile_registration_card_middle_guideline)
    Guideline mobileRegistrationCardMiddleGuideline;
    @BindView(R.id.mobile_registration_button_bottom_guideline)
    Guideline mobileRegistrationButtonBottomGuideline;

    private CountDownTimer countDownTimer;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mobile_registration, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mobileRegistrationNumberEditText.setOnEditTextChangeListener(() -> {
            showErrorText(false);
            changeButtonState(mobileRegistrationButton, mobileRegistrationNumberEditText.isPhoneFull());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharedPreferencesUtil.getBindDeviceRequestTime() > 0 && (SharedPreferencesUtil.getBindDeviceRequestTime() + START_TIME) > DateTimeUtil.getCurrentTime()) {
            startCountDownTimer((SharedPreferencesUtil.getBindDeviceRequestTime() + START_TIME) - DateTimeUtil.getCurrentTime());
        }
    }

    private void startCountDownTimer(long startTime) {
        mobileRegistrationNumberEditText.setPhoneEditEnable(false);
        mobileRegistrationChangePhoneText.setVisibility(View.VISIBLE);
        String startTimeUntilFinish = getStringFromResource(R.string.change_your_phone_number_in_0_30) + DateTimeUtil.getSimpleDateFormatTime(START_TIME, getActivity());
        mobileRegistrationChangePhoneText.setText(startTimeUntilFinish);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(startTime, DateTimeUtil.SECOND) {

            public void onTick(long millisUntilFinished) {
                try {
                    String currentTimeUntilFinish = getStringFromResource(R.string.change_your_phone_number_in_0_30) + DateTimeUtil.getSimpleDateFormatTime(millisUntilFinished, getActivity());
                    mobileRegistrationChangePhoneText.setText(currentTimeUntilFinish);
                } catch (IllegalStateException | NullPointerException ignored) {

                }
            }

            public void onFinish() {
                if (mobileRegistrationChangePhoneText != null && mobileRegistrationNumberEditText != null) {
                    mobileRegistrationChangePhoneText.setVisibility(View.GONE);
                    mobileRegistrationNumberEditText.setPhoneEditEnable(true);
                }
            }
        }.start();
    }

    private void showErrorText(boolean isError) {
        mobileRegistrationErrorText.setVisibility(isError ? View.VISIBLE : View.GONE);
        ViewUtil.changeGuidelinePercent(mobileRegistrationCardBottomGuideline, isError ? 0.8F : 0.75F);
        ViewUtil.changeGuidelinePercent(mobileRegistrationCardInnerTopGuideline, isError ? 0.2F : 0.14F);
        ViewUtil.changeGuidelinePercent(mobileRegistrationCardInnerCentralGuideline, isError ? 0.46F : 0.40F);
        ViewUtil.changeGuidelinePercent(mobileRegistrationCardMiddleGuideline, isError ? 0.58F : 0.54F);
        ViewUtil.changeGuidelinePercent(mobileRegistrationButtonBottomGuideline, isError ? 0.9F : 0.86F);
    }

    @Override
    public void getData(Object object) {
        if (object instanceof BindDeviceResponse) {
            BindDeviceResponse response = (BindDeviceResponse) object;
            SharedPreferencesUtil.setPhone(mobileRegistrationNumberEditText.getPhone());
            SharedPreferencesUtil.setSalt(response.getSalt());
            SharedPreferencesUtil.setBindDeviceRequestTime(DateTimeUtil.getCurrentTime());
            startCountDownTimer(START_TIME);
            mobileRegistrationNumberEditText.clearPhone();
            showNextFragment(BaseRegistrationFragment.newInstance(VerificationRegistrationFragment.class, null));
        }
    }

    @OnClick(R.id.mobile_registration_button)
    void onButtonClick() {
        if (mobileRegistrationNumberEditText.isPhoneFull()) {
            registrationPresenter
                    .bindDevice(new Device(mobileRegistrationNumberEditText.getPhone(), Constants.DEVICE_TYPE, SharedPreferencesUtil.getDeviceId()), getContext());
        } else {
            mobileRegistrationNumberEditText.setErrorState(true);
            showErrorText(true);
        }

    }
}
