package ht.lisa.app.ui.wallet.cashout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.R;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.response.AccountResponse;
import ht.lisa.app.model.response.MonCashTransferResponse;
import ht.lisa.app.model.response.SogexpressTransferResponse;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.main.TopSheetDialogEvent;
import ht.lisa.app.ui.registration.BaseRegistrationFragment;
import ht.lisa.app.ui.registration.TermsAgreementRegistrationFragment;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.EditTextUtil;
import ht.lisa.app.util.OnFragmentVisibleListener;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextChange;
import ht.lisa.app.util.TextUtil;

public class CashOutBalanceFragment extends BaseWalletFragment {
    public static final String SCREEN_NAME = "CashOutBalanceScreen";

    public static final String CASH_OUT_CODE = "cashOutCode";
    static final String IS_MON_CASH = "isMonCash";
    private static final int TEXT_MIN_SIZE = 1;
    private static final int MIN_MON_CASH = 5;
    private static final int MIN_SOGEXPRESS = 50;
    @BindView(R.id.cash_out_balance_number_edittext)
    EditText cashOutBalanceNumberEditText;
    @BindView(R.id.cash_out_balance_text)
    TextView cashOutBalanceText;
    @BindView(R.id.cash_out_balance_avatar)
    ImageView cashOutBalanceAvatar;
    @BindView(R.id.cash_out_balance_button)
    Button cashOutBalanceButton;
    @BindView(R.id.cash_out_balance_balance_text)
    TextView cashOutBalanceBalanceText;
    @BindView(R.id.cash_out_balance_view)
    View cashOutBalanceView;
    @BindView(R.id.cash_out_balance_balance_error)
    TextView cashOutBalanceBalanceError;
    @BindView(R.id.cash_out_balance_service_textview)
    TextView cashOutBalanceServiceTextview;
    @BindView(R.id.cash_out_balance_service_checkbox)
    CheckBox cashOutBalanceServiceCheckbox;
    private boolean isMonCash;
    private int cashOutBalance;
    OnFragmentVisibleListener onFragmentVisibleListener;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cash_out_balance, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            isMonCash = getArguments().getBoolean(IS_MON_CASH);
        }
        cashOutBalanceText.setText(getString(isMonCash ? R.string.moncash_description : R.string.sogexpress_description));
        walletPresenter.getAccount(new Phone(SharedPreferencesUtil.getPhone()));
        cashOutBalanceAvatar.setImageResource(isMonCash ? R.drawable.mon_cash_main : R.drawable.sogexpress_main);
        setTextChangedListener();
        setClickableTexts();
        setOnCheckChangeListener();
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
            onFragmentVisibleListener.onFragmentShowListener(CashOutChooseFragment.class.getSimpleName(), true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (onFragmentVisibleListener != null) {
            onFragmentVisibleListener.onFragmentShowListener(CashOutChooseFragment.class.getSimpleName(), false);
            onFragmentVisibleListener = null;
        }
    }


    private void setTextChangedListener() {
        cashOutBalanceNumberEditText.addTextChangedListener(new TextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                changeButtonState(cashOutBalanceButton, isButtonEnabled());
                checkErrorState(false);
            }
        });
    }

    private void setClickableTexts() {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(TermsAgreementRegistrationFragment.IS_PAYMENT_TERM, true);
                showNextFragment(BaseRegistrationFragment.newInstance(TermsAgreementRegistrationFragment.class, bundle));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getColorFromResource(R.color.colorPrimaryDark));
            }
        };
        TextUtil.createClickableSpan(cashOutBalanceServiceTextview, cashOutBalanceServiceTextview.getText().length() - getStringFromResource(R.string.terms_of_service).length(), clickableSpan);
    }

    private void setOnCheckChangeListener() {
        cashOutBalanceServiceCheckbox.setOnCheckedChangeListener((compoundButton, checked) -> {
            checkErrorState(false);
            changeButtonState(cashOutBalanceButton, isButtonEnabled());
        });
    }

    private boolean isButtonEnabled() {
        return isCashOutBalanceServiceChecked() && isTextMinSize() && isAmountInMinRange() && isAmountInMaxRange();
    }

    private boolean isTextMinSize() {
        return EditTextUtil.isTextMinSize(cashOutBalanceNumberEditText, TEXT_MIN_SIZE);
    }

    private boolean isCashOutBalanceServiceChecked() {
        return cashOutBalanceServiceCheckbox.isChecked();
    }

    private boolean isAmountInMinRange() {
        boolean isAmountInMinRange;
        try {
            isAmountInMinRange = Integer.parseInt(cashOutBalanceNumberEditText.getText().toString()) >= (isMonCash ? MIN_MON_CASH : MIN_SOGEXPRESS);
        } catch (Exception e) {
            isAmountInMinRange = false;
        }
        return isAmountInMinRange;
    }

    private boolean isAmountInMaxRange() {
        boolean isAmountInMaxRange;
        try {
            isAmountInMaxRange = Integer.parseInt(cashOutBalanceNumberEditText.getText().toString()) <= cashOutBalance / 100;
        } catch (Exception e) {
            isAmountInMaxRange = false;
        }
        return isAmountInMaxRange;
    }

    private void checkErrorState(boolean isButtonClick) {
        if (isButtonClick) {
            cashOutBalanceServiceCheckbox.setButtonDrawable(cashOutBalanceServiceCheckbox.isChecked() ? R.drawable.checkbox_selector : R.drawable.ic_error_checkbox);
            cashOutBalanceBalanceError.setVisibility(View.VISIBLE);
            if (!isTextMinSize()) {
                cashOutBalanceBalanceError.setText(getStringFromResource(R.string.too_short));
            } else if (!isAmountInMaxRange()) {
                cashOutBalanceBalanceError.setText(getStringFromResource(R.string.exceeded_amount));
            } else if (!isAmountInMinRange()) {
                cashOutBalanceBalanceError.setText(getStringFromResource(R.string.low_amount));
            }
            cashOutBalanceView.setBackgroundColor(isTextMinSize() && isAmountInMinRange() && isAmountInMaxRange() ? getColorFromResource(R.color.inactiveBG) : getColorFromResource(R.color.red));
        } else {
            cashOutBalanceServiceCheckbox.setButtonDrawable(R.drawable.checkbox_selector);
            cashOutBalanceBalanceError.setVisibility(View.GONE);
            cashOutBalanceView.setBackgroundColor(getColorFromResource(R.color.inactiveBG));
        }
    }

    @Override
    public void getData(Object object) {
        if (object instanceof AccountResponse) {
            AccountResponse response = (AccountResponse) object;
            if (response.getState() == 0) {
                if (getContext() == null) return;
                cashOutBalance = response.getWithdrawal();
                cashOutBalanceBalanceText.setText(TextUtil.getHTGResizedText(String.format(getStringFromResource(R.string._G_), TextUtil.getDecimalString(response.getWithdrawal()))));
            }
        } else if (object instanceof MonCashTransferResponse) {
            MonCashTransferResponse response = (MonCashTransferResponse) object;
            if (response.getState() == 0) {
                if (getContext() == null) return;
                EventBus.getDefault().post(new TopSheetDialogEvent(getString(R.string.transfer_successed_cashout)));
            } else {
                showMessage(response.getErrorMessage());
            }
        } else if (object instanceof SogexpressTransferResponse) {
            SogexpressTransferResponse response = (SogexpressTransferResponse) object;
            if (response.getState() == 0) {
                if (getContext() == null) return;
                startActivity(new Intent(getActivity(), MainActivity.class));
                SharedPreferencesUtil.setCashOutCode(response.getPin());
                SharedPreferencesUtil.setCashOutCodeTime(DateTimeUtil.getCurrentTime());
            } else {
                showMessage(response.getErrorMessage());
            }
        }
    }

    @OnClick(R.id.cash_out_balance_button)
    void onCashOutBalanceButtonClick() {
        if (isButtonEnabled()) {
            if (isMonCash) {
                walletPresenter.monCashTransfer(new Phone(SharedPreferencesUtil.getPhone()), cashOutBalanceNumberEditText.getText().toString() + getString(R.string.centimes));
            } else {
                walletPresenter.sogexpressTransfer(new Phone(SharedPreferencesUtil.getPhone()), cashOutBalanceNumberEditText.getText().toString() + getString(R.string.centimes));
            }
        } else {
            checkErrorState(true);
        }
    }
}
