package ht.lisa.app.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

public class EditTextUtil {

    public static void clearEditTexts(ArrayList<EditText> editTextsArray) {
        for (EditText editText : editTextsArray) {
            editText.setText("");
        }
    }

    public static String getStringFromEditTexts(ArrayList<EditText> editTextsArray) {
        StringBuilder result = new StringBuilder();
        for (EditText editText : editTextsArray) {
            result.append(editText.getText().toString());
        }
        return result.toString();
    }

    public static boolean isEditTextsNotEmpty(ArrayList<EditText> editTextsArray) {
        for (EditText editText : editTextsArray) {
            if (editText.getText().toString().isEmpty())
                return false;
        }
        return true;
    }

    public static void changeEditTextFocus(Context context, ArrayList<EditText> editTextsArray, boolean isBackSpace) {
        for (int i = 0; i < editTextsArray.size(); i++) {
            if (editTextsArray.get(i).isFocused()) {
                if (isBackSpace && i != 0 && editTextsArray.get(i).getText().toString().isEmpty()) {
                    editTextsArray.get(i - 1).setText("");
                    TextUtil.showKeyboardFrom(context, editTextsArray.get(i - 1));
                    break;
                } else if (!isBackSpace && i == editTextsArray.size() - 1) {
                    editTextsArray.get(i).clearFocus();
                    TextUtil.hideKeyboard(context, editTextsArray.get(i));
                    break;
                } else if (!isBackSpace && i != editTextsArray.size() - 1) {
                    TextUtil.showKeyboardFrom(context, editTextsArray.get(i + 1));
                    break;
                }
            }
        }
    }

    public static void setFocusOnFirstEmptyPosition(ArrayList<EditText> editTextsArray, Context context, boolean focusOnLast) {
        for (int i = 0; i < editTextsArray.size(); i++) {
            if (editTextsArray.get(i).getText().toString().isEmpty() || i == editTextsArray.size() - 1 && focusOnLast) {
                TextUtil.showKeyboardFrom(context, editTextsArray.get(i));
                break;
            }
        }
    }

    public static ArrayList<EditText> getEditTexts(ViewGroup viewGroup, boolean clearTexts) {
        return new ArrayList<>(getEditTextsFromLayout(viewGroup, clearTexts));
    }

    public static ArrayList<EditText> getEditTextsFromLayout(ViewGroup viewGroup, boolean clearTexts) {
        return getEditTextsFromLayout(new ArrayList<>(), viewGroup, clearTexts);
    }

    public static ArrayList<EditText> getEditTextsFromLayout(ArrayList<EditText> editTexts, ViewGroup viewGroup, boolean clearTexts) {

        final int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                getEditTextsFromLayout(editTexts, (ViewGroup) view, clearTexts);
            } else if (viewGroup.getChildAt(i) instanceof EditText) {
                EditText editText = (EditText) viewGroup.getChildAt(i);
                if (clearTexts) {
                    editText.setText(null);
                }
                editTexts.add(editText);
            }
        }
        return editTexts;
    }

    public static boolean isTextMinSize(EditText editText, int rightSize) {
        return editText.getText().toString().length() >= rightSize;
    }
}
