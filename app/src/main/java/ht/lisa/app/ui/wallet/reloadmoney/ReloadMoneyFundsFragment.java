package ht.lisa.app.ui.wallet.reloadmoney;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.Message;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.response.MonCashRequestResponse;
import ht.lisa.app.ui.main.GetMessageEvent;
import ht.lisa.app.ui.registration.BaseRegistrationFragment;
import ht.lisa.app.ui.registration.TermsAgreementRegistrationFragment;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.EditTextUtil;
import ht.lisa.app.util.RxUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextChange;
import ht.lisa.app.util.TextUtil;

public class ReloadMoneyFundsFragment extends BaseWalletFragment {

    public static final String SCREEN_NAME = "ReloadMoneyScreen";

    public static final String RELOAD_MONEY_AMOUNT = "reloadMoneyAmount";
    private static final int TEXT_MIN_SIZE = 1;
    private static final int DELAYED_TIME = 10000;
    private static String storedOrderId = null;

    @BindView(R.id.reload_money_funds_number_edittext)
    EditText reloadMoneyFundsNumberEditText;
    @BindView(R.id.reload_money_funds_button)
    Button reloadMoneyFundsButton;
    @BindView(R.id.reload_money_funds_balance_name)
    TextView reloadMoneyFundsBalanceName;
    @BindView(R.id.reload_money_funds_balance_phone)
    TextView reloadMoneyFundsBalancePhone;
    @BindView(R.id.reload_money_funds_view)
    View reloadMoneyFundsView;
    @BindView(R.id.reload_money_funds_balance_error)
    TextView reloadMoneyFundsBalanceError;
    @BindView(R.id.reload_money_funds_service_textview)
    TextView reloadMoneyFundsServiceTextView;
    @BindView(R.id.reload_money_funds_service_checkbox)
    CheckBox reloadMoneyFundsServiceCheckbox;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reload_money_funds, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTextChangedListener();
        setClickableTexts();
        setOnCheckChangeListener();
        reloadMoneyFundsButton.setEnabled(true);
        reloadMoneyFundsBalanceName.setText(LisaApp.getInstance().getUser().getFullName());
        reloadMoneyFundsBalancePhone.setText(SharedPreferencesUtil.getPhone());
    }

    private void setTextChangedListener() {
        reloadMoneyFundsNumberEditText.addTextChangedListener(new TextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                changeButtonState(reloadMoneyFundsButton, isButtonEnabled());
                checkErrorState(false);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgress();
        reloadMoneyFundsServiceCheckbox.setChecked(false);
    }

    @Override
    public void getData(Object object) {
        if (object instanceof MonCashRequestResponse) {
            MonCashRequestResponse response = (MonCashRequestResponse) object;
            if (response.getState() == 0) {
              /*  RxUtil.delayedConsumer(DELAYED_TIME, aLong -> {
                    if (ReloadMoneyFundsFragment.this.getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        hideProgress();*/
                String orderId = response.getOrderId();
                if (orderId == null) {
                    orderId = storedOrderId;
                } else {
                    storedOrderId = orderId;
                }
                String finalOrderId = orderId;
                RxUtil.delayedConsumer(2000, aLong -> walletPresenter.getReloadMoneyUrl(new Phone(SharedPreferencesUtil.getPhone()), finalOrderId));
                /*    }
                });*/
            } else {
                Toast.makeText(getContext(), response.getBaseErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (object instanceof Message) {
            Message message = (Message) object;
            if (ReloadMoneyFundsFragment.this.getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                Log.d("WUVRBR", message.getState() + " " + message.getBaseErrorMessage());

                if (message.getState() == 0)
                    EventBus.getDefault().post(new GetMessageEvent(message));
                else {
                    if (message.getState() == Message.STATE_MESSAGE_NOT_FOUND) {
                        String orderId = message.getOrderId();
                        if (orderId == null) {
                            orderId = storedOrderId;
                        } else {
                            storedOrderId = orderId;
                        }
                        String finalOrderId = orderId;
                        RxUtil.delayedConsumer(2000, aLong -> walletPresenter.getReloadMoneyUrl(new Phone(SharedPreferencesUtil.getPhone()), finalOrderId));
                    }
                    else
                        Toast.makeText(getContext(), message.getBaseErrorMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }
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
        TextUtil.createClickableSpan(reloadMoneyFundsServiceTextView, reloadMoneyFundsServiceTextView.getText().length() - getStringFromResource(R.string.terms_of_service).length(), clickableSpan);
    }

    private void setOnCheckChangeListener() {
        reloadMoneyFundsServiceCheckbox.setOnCheckedChangeListener((compoundButton, checked) -> {
            checkErrorState(false);
            changeButtonState(reloadMoneyFundsButton, isButtonEnabled());
        });
    }

    private boolean isButtonEnabled() {
        return isAddAmountServiceChecked() && isTextMinSize();
    }

    private boolean isTextMinSize() {
        return EditTextUtil.isTextMinSize(reloadMoneyFundsNumberEditText, TEXT_MIN_SIZE);
    }

    private boolean isAddAmountServiceChecked() {
        return reloadMoneyFundsServiceCheckbox.isChecked();
    }

    private void checkErrorState(boolean isButtonClick) {
        if (isButtonClick) {
            reloadMoneyFundsServiceCheckbox.setButtonDrawable(reloadMoneyFundsServiceCheckbox.isChecked() ? R.drawable.checkbox_selector : R.drawable.ic_error_checkbox);
            reloadMoneyFundsBalanceError.setVisibility(isTextMinSize() ? View.GONE : View.VISIBLE);
            reloadMoneyFundsBalanceError.setText(isTextMinSize() ? getStringFromResource(R.string.exceeded_amount) : getStringFromResource(R.string.too_short));
            reloadMoneyFundsView.setBackgroundColor(isTextMinSize() ? getColorFromResource(R.color.inactiveBG) : getColorFromResource(R.color.red));
        } else {
            reloadMoneyFundsServiceCheckbox.setButtonDrawable(R.drawable.checkbox_selector);
            reloadMoneyFundsBalanceError.setVisibility(View.GONE);
            reloadMoneyFundsView.setBackgroundColor(getColorFromResource(R.color.inactiveBG));
        }
    }

    @OnClick(R.id.reload_money_funds_button)
    void onReloadMoneyFundsButtonClick() {
        if (isButtonEnabled()) {
            walletPresenter.monCashRequest(new Phone(SharedPreferencesUtil.getPhone()), reloadMoneyFundsNumberEditText.getText().toString() + getStringFromResource(R.string.centimes));
            reloadMoneyFundsNumberEditText.setText("");
        } else {
            checkErrorState(true);
        }
    }
}
