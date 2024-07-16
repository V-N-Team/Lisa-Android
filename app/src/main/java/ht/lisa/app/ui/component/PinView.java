package ht.lisa.app.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import ht.lisa.app.R;
import ht.lisa.app.util.TextUtil;

public class PinView extends FrameLayout implements TextWatcher, View.OnTouchListener {
    public static final int DEFAULT_LENGTH = 4;
    private int length = DEFAULT_LENGTH;

    private AppCompatEditText input;
    private LinearLayout symbolsContainer;
    private OnPinEnteredListener onPinEnteredListener;


    public PinView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public PinView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PinView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PinView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(AttributeSet attrs) {
        symbolsContainer = new LinearLayout(getContext());
        symbolsContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        symbolsContainer.setGravity(Gravity.BOTTOM);


        addView(symbolsContainer);
        fillSymbols(false);

        input = new AppCompatEditText(getContext());
        ViewCompat.setBackgroundTintList(input, ColorStateList.valueOf(ContextCompat.getColor(getContext(), android.R.color.transparent)));
        input.setCursorVisible(false);
        input.setFocusable(true);
        input.setFocusableInTouchMode(true);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setTextColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        input.addTextChangedListener(this);
        input.setOnTouchListener(this);
        input.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        setMaxLength(input, length);
        addView(input);
    }

    private int getInputLength() {
        return input == null ? 0 : input.getText().length();
    }

    private void fillSymbols(boolean error) {
        symbolsContainer.removeAllViews();
        for (int i = 0; i < length; i++) {
            AppCompatImageView imageView = new AppCompatImageView(getContext());
            imageView.setLayoutParams(getDefaultElementLayoutParams());
            imageView.setImageResource(getInputLength() > i ?
                    R.drawable.ic_filled_field :
                    error ? R.drawable.ic_error_field : R.drawable.ic_empty_field);
            symbolsContainer.addView(imageView);
        }
        if (getInputLength() > 0) {
            symbolsContainer.removeViewAt(getInputLength() - 1);
            TextView symbolText = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.input_element_pin, symbolsContainer, false);
            String textInput = getInputString();
            symbolText.setText(textInput.substring(getInputLength() - 1));
            symbolText.setLayoutParams(getDefaultElementLayoutParams());
            symbolsContainer.addView(symbolText, getInputLength() - 1);
        }
    }

    private ViewGroup.LayoutParams getDefaultElementLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        params.gravity = Gravity.BOTTOM;
        return params;
    }

    private void setMaxLength(AppCompatEditText input, int length) {
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(length);
        input.setFilters(fArray);
    }

    public void setOnPinEnteredListener(OnPinEnteredListener onPinEnteredListener) {
        this.onPinEnteredListener = onPinEnteredListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        onPinEnteredListener.onPinChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (onPinEnteredListener != null) {
            if (s.toString().length() == length) {
                onPinEnteredListener.onPinEntered(s.toString());
            }
        }
        fillSymbols(false);
    }

    public void setFieldsError(boolean error) {
        fillSymbols(error);
    }

    public String getEnteredPin() {
        return input.getText().toString();
    }

    public boolean isCurrentPinFull() {
        return getInputLength() == 4;
    }


    public void clear() {
        input.setText("");
    }


    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            input.setSelection(input.getText().length());
        }
        return false;
    }

    public void hideKeyBoard() {
        new Handler().postDelayed(() ->
                TextUtil.hideKeyboard(getContext(), input), 100);
    }

    public void showKeyBoard() {
        new Handler().postDelayed(() ->
                TextUtil.showKeyboardFrom(getContext(), input), 100);
    }

    public EditText getInput() {
        return input;
    }

    public String getInputString() {
        return input == null ? null : input.getText().toString();
    }

    public void setPinEnabled(boolean isEnabled) {
        input.setEnabled(isEnabled);
    }

    public interface OnPinEnteredListener {
        void onPinEntered(String pin);

        void onPinChanged();
    }
}
