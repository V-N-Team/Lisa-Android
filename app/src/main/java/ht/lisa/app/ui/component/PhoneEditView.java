package ht.lisa.app.ui.component;

import android.content.Context;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ht.lisa.app.R;
import ht.lisa.app.util.EditTextUtil;
import ht.lisa.app.util.TextChange;
import ht.lisa.app.util.TextUtil;

public class PhoneEditView extends LinearLayout {

    private static final int TEXT_MIN_SIZE = 8;
    private static final String PHONE_CODE = "509";

    @BindView(R.id.number_layout)
    ConstraintLayout numberLayout;

    @BindView(R.id.hyphen_code)
    TextView hyphenCode;

    @BindView(R.id.number_edittext)
    EditText numberEditText;

    @BindView(R.id.number_check_icon)
    ImageView numberCheckIcon;

    private OnEditTextChangeListener onEditTextChangeListener;


    public PhoneEditView(Context context) {
        super(context);
        init();
    }

    public PhoneEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PhoneEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init() {
        init(null);
    }

    private void init(@Nullable AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.phone_edit_view, this);
        ButterKnife.bind(this, view);
        numberEditText.addTextChangedListener(getMobileChangeListener());
    }

    private TextWatcher getMobileChangeListener() {
        return new TextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                changeHypenCodeState(editable.toString().isEmpty());
                setErrorState(false);
                changeCheckImageState(EditTextUtil.isTextMinSize(numberEditText, TEXT_MIN_SIZE));
                onEditTextChangeListener.onEditChange();
            }
        };
    }

    private void changeHypenCodeState(boolean isHidden) {
        hyphenCode.setVisibility(isHidden ? View.GONE : View.VISIBLE);
    }

    public void setOnEditTextChangeListener(OnEditTextChangeListener onEditTextChangeListener) {
        this.onEditTextChangeListener = onEditTextChangeListener;
    }

    public void clearPhone() {
        numberEditText.setText("");
    }

    public boolean isPhoneFull() {
        return numberEditText.getText().toString().length() == TEXT_MIN_SIZE;
    }

    public String getPhone() {
        return PHONE_CODE + numberEditText.getText().toString();
    }

    private void changeCheckImageState(boolean isFullLength) {
        if (isFullLength) {
            numberCheckIcon.setVisibility(VISIBLE);
            numberCheckIcon.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.ic_accept_mark));
            if (getContext() == null) return;
            TextUtil.hideKeyboard(getContext(), numberCheckIcon);
        } else {
            numberCheckIcon.setVisibility(GONE);
        }
    }

    public void setErrorState(boolean isError) {
        numberLayout.getBackground().setColorFilter(isError ? getResources().getColor(R.color.red) : getResources().getColor(R.color.inactiveBG), PorterDuff.Mode.MULTIPLY);
        numberCheckIcon.setVisibility(isError ? VISIBLE : GONE);
        numberCheckIcon.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.ic_error_mark));
    }

    public void setPhoneEditEnable(boolean isEnabled) {
        numberEditText.setEnabled(isEnabled);
    }

    public interface OnEditTextChangeListener {
        void onEditChange();
    }
}
