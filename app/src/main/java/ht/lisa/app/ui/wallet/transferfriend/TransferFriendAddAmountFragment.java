package ht.lisa.app.ui.wallet.transferfriend;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.R;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.UserTransfer;
import ht.lisa.app.model.response.AccountResponse;
import ht.lisa.app.model.response.UserTransferResponse;
import ht.lisa.app.ui.main.TopSheetDialogEvent;
import ht.lisa.app.ui.registration.BaseRegistrationFragment;
import ht.lisa.app.ui.registration.PinCodeRegistrationFragment;
import ht.lisa.app.ui.registration.TermsAgreementRegistrationFragment;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.EditTextUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextChange;
import ht.lisa.app.util.TextUtil;

public class TransferFriendAddAmountFragment extends BaseWalletFragment {
    public static final String SCREEN_NAME = "TransferBalanceScreen";

    private static final int TEXT_MIN_SIZE = 1;
    @BindView(R.id.transfer_friend_add_amount_number_edittext)
    EditText transferFriendAddAmountNumberEditText;
    @BindView(R.id.transfer_friend_add_amount_button)
    Button transferFriendAddAmountButton;
    @BindView(R.id.transfer_friend_add_amount_balance_text)
    TextView transferFriendAddAmountBalanceText;
    @BindView(R.id.transfer_friend_add_amount_balance_phone)
    TextView transferFriendAddAmountBalancePhone;
    @BindView(R.id.transfer_friend_add_amount_view)
    View transferFriendAddAmountView;
    @BindView(R.id.transfer_friend_add_amount_balance_error)
    TextView transferFriendAddAmountBalanceError;
    @BindView(R.id.transfer_friend_add_amount_service_textview)
    TextView transferFriendAddAmountServiceTextview;
    @BindView(R.id.transfer_friend_add_amount_service_checkbox)
    CheckBox transferFriendAddAmountServiceCheckbox;
    private UserTransfer userTransfer;
    private int transferBalance;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transfer_friend_add_amount, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            userTransfer = (UserTransfer) getArguments().getSerializable(UserTransfer.TRANSFER_USER);
            if (userTransfer != null) {
                transferFriendAddAmountBalancePhone.setText(String.format(getStringFromResource(R.string._509_), userTransfer.getRecipient().contains(getStringFromResource(R.string._509)) ? userTransfer.getRecipient().substring(getStringFromResource(R.string._509).length()) : userTransfer.getRecipient()));
            }
        }
        walletPresenter.getAccount(new Phone(SharedPreferencesUtil.getPhone()));
        setTextChangedListener();
        setClickableTexts();
        setOnCheckChangeListener();
    }

    private void setTextChangedListener() {
        transferFriendAddAmountNumberEditText.addTextChangedListener(new TextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                changeButtonState(transferFriendAddAmountButton, isButtonEnabled());
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
        TextUtil.createClickableSpan(transferFriendAddAmountServiceTextview, transferFriendAddAmountServiceTextview.getText().length() - getStringFromResource(R.string.terms_of_service).length(), clickableSpan);
    }

    private void setOnCheckChangeListener() {
        transferFriendAddAmountServiceCheckbox.setOnCheckedChangeListener((compoundButton, checked) -> {
            checkErrorState(false);
            changeButtonState(transferFriendAddAmountButton, isButtonEnabled());
        });
    }

    private boolean isButtonEnabled() {
        return isAddAmountServiceChecked() && isTextMinSize() && isAmountInMaxRange();
    }

    private boolean isTextMinSize() {
        return EditTextUtil.isTextMinSize(transferFriendAddAmountNumberEditText, TEXT_MIN_SIZE);
    }

    private boolean isAddAmountServiceChecked() {
        return transferFriendAddAmountServiceCheckbox.isChecked();
    }

    private boolean isAmountInMaxRange() {
        boolean isAmountInMaxRange;
        try {
            isAmountInMaxRange = Integer.parseInt(transferFriendAddAmountNumberEditText.getText().toString()) <= transferBalance / 100;
        } catch (Exception e) {
            isAmountInMaxRange = false;
        }
        return isAmountInMaxRange;
    }

    @Override
    public void getData(Object object) {
        if (object instanceof UserTransferResponse) {
            UserTransferResponse response = (UserTransferResponse) object;
            if (response.getState() == 0) {
                EventBus.getDefault().post(new TopSheetDialogEvent(getString(R.string.transfer_successed_cashout)));
            } else if (response.getState() == 8) {
                showNextFragment(BaseRegistrationFragment.newInstance(PinCodeRegistrationFragment.class, null));
            }
        } else if (object instanceof AccountResponse) {
            AccountResponse response = (AccountResponse) object;
            if (response.getState() == 0) {
                if (getContext() == null) return;
                transferBalance = response.getTotal();
                transferFriendAddAmountBalanceText.setText(TextUtil.getHTGResizedText(String.format(getStringFromResource(R.string._G_), TextUtil.getDecimalString(response.getTotal()))));
            }
        }
    }

    private void checkErrorState(boolean isButtonClick) {
        if (isButtonClick) {
            transferFriendAddAmountServiceCheckbox.setButtonDrawable(transferFriendAddAmountServiceCheckbox.isChecked() ? R.drawable.checkbox_selector : R.drawable.ic_error_checkbox);
            transferFriendAddAmountBalanceError.setVisibility(isTextMinSize() && isAmountInMaxRange() ? View.GONE : View.VISIBLE);
            if (!isTextMinSize()) {
                transferFriendAddAmountBalanceError.setText(getStringFromResource(R.string.too_short));
            } else if (!isAmountInMaxRange()) {
                transferFriendAddAmountBalanceError.setText(getStringFromResource(R.string.exceeded_amount));
            }
            transferFriendAddAmountView.setBackgroundColor(isTextMinSize() && isAmountInMaxRange() ? getColorFromResource(R.color.inactiveBG) : getColorFromResource(R.color.red));
        } else {
            transferFriendAddAmountServiceCheckbox.setButtonDrawable(R.drawable.checkbox_selector);
            transferFriendAddAmountBalanceError.setVisibility(View.GONE);
            transferFriendAddAmountView.setBackgroundColor(getColorFromResource(R.color.inactiveBG));
        }
    }

    @OnClick(R.id.transfer_friend_add_amount_button)
    void onButtonClick() {
        if (isButtonEnabled()) {
            userTransfer.setAmount(transferFriendAddAmountNumberEditText.getText().toString() + getString(R.string.centimes));
            walletPresenter.transferUser(userTransfer);
        } else {
            checkErrorState(true);
        }
    }
}
