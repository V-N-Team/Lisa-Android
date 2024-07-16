package ht.lisa.app.ui.wallet.transferfriend;

import android.os.Bundle;
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
import ht.lisa.app.model.UserTransfer;
import ht.lisa.app.ui.component.PhoneEditView;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.ViewUtil;

public class TransferFriendPhoneNumberFragment extends BaseWalletFragment {

    public static final String SCREEN_NAME = "TransferToFriendScreen";

    @BindView(R.id.transfer_friend_phone_number_number_edittext)
    PhoneEditView transferFriendPhoneNumberNumberEditText;
    @BindView(R.id.transfer_friend_phone_number_button)
    Button transferFriendPhoneNumberButton;
    @BindView(R.id.transfer_friend_phone_number_error_text)
    TextView transferFriendPhoneNumberErrorText;
    @BindView(R.id.transfer_friend_phone_number_card_bottom_guideline)
    Guideline transferFriendPhoneNumberCardBottomGuideline;
    @BindView(R.id.transfer_friend_phone_number_card_inner_top_guideline)
    Guideline transferFriendPhoneNumberCardInnerTopGuideline;
    @BindView(R.id.transfer_friend_phone_number_card_inner_central_guideline)
    Guideline transferFriendPhoneNumberCardInnerCentralGuideline;
    @BindView(R.id.transfer_friend_phone_number_card_middle_guideline)
    Guideline transferFriendPhoneNumberCardMiddleGuideline;
    @BindView(R.id.transfer_friend_phone_number_button_bottom_guideline)
    Guideline transferFriendPhoneNumberButtonBottomGuideline;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transfer_friend_phone_number, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        transferFriendPhoneNumberNumberEditText.setOnEditTextChangeListener(() -> {
            showErrorText(false);
            ViewUtil.changeGuidelinePercent(transferFriendPhoneNumberCardBottomGuideline, 0.75F);
            changeButtonState(transferFriendPhoneNumberButton, transferFriendPhoneNumberNumberEditText.isPhoneFull());
        });
    }

    private void showErrorText(boolean isError) {
        transferFriendPhoneNumberErrorText.setVisibility(isError ? View.VISIBLE : View.GONE);
        ViewUtil.changeGuidelinePercent(transferFriendPhoneNumberCardBottomGuideline, isError ? 0.8F : 0.75F);
        ViewUtil.changeGuidelinePercent(transferFriendPhoneNumberCardInnerTopGuideline, isError ? 0.2F : 0.14F);
        ViewUtil.changeGuidelinePercent(transferFriendPhoneNumberCardInnerCentralGuideline, isError ? 0.46F : 0.40F);
        ViewUtil.changeGuidelinePercent(transferFriendPhoneNumberCardMiddleGuideline, isError ? 0.58F : 0.54F);
        ViewUtil.changeGuidelinePercent(transferFriendPhoneNumberButtonBottomGuideline, isError ? 0.9F : 0.86F);
    }

    @OnClick(R.id.transfer_friend_phone_number_button)
    void onFriendNumberButtonClick() {
        if (transferFriendPhoneNumberNumberEditText.isPhoneFull()) {
            Bundle bundle = new Bundle();
            UserTransfer userTransfer = new UserTransfer();
            userTransfer.setPhone(SharedPreferencesUtil.getPhone());
            userTransfer.setRecipient(transferFriendPhoneNumberNumberEditText.getPhone());
            bundle.putSerializable(UserTransfer.TRANSFER_USER, userTransfer);
            showNextFragment(BaseWalletFragment.newInstance(TransferFriendAddAmountFragment.class, bundle));
        } else {
            transferFriendPhoneNumberNumberEditText.setErrorState(true);
            showErrorText(true);
        }
    }
}
