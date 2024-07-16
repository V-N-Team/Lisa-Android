package ht.lisa.app.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import ht.lisa.app.R;
import ht.lisa.app.util.EditTextUtil;
import ht.lisa.app.util.PinViewTextWatcher;
import ht.lisa.app.util.ViewUtil;

public class VerificationCodeEditView extends LinearLayout {

    @BindView(R.id.verification_registration_edit_text_layout)
    LinearLayout verificationRegistrationEditTextLayout;

    private TextView errorTextView;

    private ArrayList<EditText> editTextsArray;

    private OnCodeEnterCompleteListener onCodeEnterCompleteListener;


    public VerificationCodeEditView(Context context) {
        super(context);
        init();
    }

    public VerificationCodeEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VerificationCodeEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init() {
        init(null);
    }

    private void init(@Nullable AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.verification_code_edit_view, this);
        ButterKnife.bind(this, view);
        editTextsArray = EditTextUtil.getEditTextsFromLayout(verificationRegistrationEditTextLayout, true);
        setTextChangedListeners();
    }

    @OnTouch({R.id.verification_registration_edittext_1, R.id.verification_registration_edittext_2, R.id.verification_registration_edittext_3, R.id.verification_registration_edittext_4, R.id.verification_registration_edittext_5, R.id.verification_registration_edittext_6})
    void onCodeClick(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            EditTextUtil.setFocusOnFirstEmptyPosition(editTextsArray, getContext(), true);
        }
    }

    public void setErrorTextView(TextView errorTextView) {
        this.errorTextView = errorTextView;
    }

    public void setOnCodeEnterCompleteListener(OnCodeEnterCompleteListener onCodeEnterCompleteListener) {
        this.onCodeEnterCompleteListener = onCodeEnterCompleteListener;
    }

    public void clearEditTexts() {
        EditTextUtil.clearEditTexts(editTextsArray);
    }

    public String getVerificationCode() {
        return EditTextUtil.getStringFromEditTexts(editTextsArray);
    }

    public void setFocusOnFirstEmptyPosition() {
        EditTextUtil.setFocusOnFirstEmptyPosition(editTextsArray, getContext(), false);
    }

    private void setTextChangedListeners() {
        PinViewTextWatcher pinViewTextWatcher = new PinViewTextWatcher(editTextsArray, getContext());
        for (EditText editText : editTextsArray) {
            editText.addTextChangedListener(pinViewTextWatcher);
            editText.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    onCodeEnterCompleteListener.onCodeChange();
                    checkErrorState(false);
                    EditTextUtil.changeEditTextFocus(getContext(), editTextsArray, keyCode == KeyEvent.KEYCODE_DEL);
                    editText.post(() -> {
                        if (isVerificationCodeFull()) {
                            onCodeEnterCompleteListener.onCodeEnterComplete();
                        }
                    });
                }
                return false;
            });
        }
    }

    private ArrayList<View> getViews() {
        ArrayList<View> views = new ArrayList<>();
        for (View view : ViewUtil.getViews(verificationRegistrationEditTextLayout, View.class)) {
            if (!(view instanceof EditText)) {
                views.add(view);
            }
        }
        return views;
    }

    public void checkErrorState(boolean isButtonClick) {
        for (int i = 0; i < getViews().size(); i++) {
            if (isButtonClick) {
                errorTextView.setVisibility(EditTextUtil.getStringFromEditTexts(editTextsArray).length() == editTextsArray.size() ? View.GONE : View.VISIBLE);
                getViews().get(i).setBackgroundColor(editTextsArray.get(i).getText().toString().isEmpty() ? getResources().getColor(R.color.red) : getResources().getColor(R.color.inactiveBG));
            } else {
                errorTextView.setVisibility(View.GONE);
                getViews().get(i).setBackgroundColor(getResources().getColor(R.color.inactiveBG));
            }
        }
    }

    private boolean isVerificationCodeFull() {
        return EditTextUtil.isEditTextsNotEmpty(editTextsArray);
    }

    public interface OnCodeEnterCompleteListener {
        void onCodeEnterComplete();
        void onCodeChange();
    }
}
