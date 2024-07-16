package ht.lisa.app.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;

import ht.lisa.app.util.EditTextUtil;

public class PinViewTextWatcher implements TextWatcher {
    private ArrayList<EditText> editTexts;
    private Context context;

    public PinViewTextWatcher(ArrayList<EditText> editTexts, Context context) {
        this.editTexts = editTexts;
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        EditTextUtil.setFocusOnFirstEmptyPosition(editTexts, context, false);
    }
}
