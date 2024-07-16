package ht.lisa.app.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TextUtil {

    public static void createClickableSpan(TextView textview, int startSpan, ClickableSpan clickableSpan) {
        SpannableString ss = new SpannableString(textview.getText().toString());
        ss.setSpan(clickableSpan, startSpan, textview.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(ss);
        textview.setMovementMethod(LinkMovementMethod.getInstance());
        textview.setHighlightColor(Color.TRANSPARENT);
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboardFrom(Context context, EditText editText) {
        editText.post(() -> {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            editText.setFocusableInTouchMode(true);
            editText.setFocusable(true);
            editText.requestFocus();
        });
    }

    public static SpannableStringBuilder getHTGResizedText(String text) {
        SpannableStringBuilder sb = new SpannableStringBuilder(text);
        sb.setSpan(new RelativeSizeSpan(0.7f), text.length() - 1, text.length(), 0);
        return sb;
    }

    public static String getFormattedDecimalString(long decimal) {
        return TextUtil.getDecimalString(decimal).replaceAll("[^0-9.G]", ",");
    }

    public static String getDecimalString(long decimal) {
        long gourde = decimal / 100;
        DecimalFormat df = new DecimalFormat("#,###,###.##");
        return checkNegativeDecimal(df.format(gourde).replaceAll(",", " ") + getDecimalPlaces(decimal));
    }

    public static String toCamelCase(String str) {
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        return builder.toString();
    }

    public static String getDecimalStringJackpot(long decimal) {
        long gourde = decimal / 100;
        DecimalFormat df = new DecimalFormat("#,###,###");
        return checkNegativeDecimal(df.format(gourde).replaceAll(",", " "));
    }

    private static String checkNegativeDecimal(String decimal) {
        return decimal.contains("-") ? "-" + decimal.replace("-", "") : decimal;
    }

    private static String getDecimalPlaces(long decimal) {
        return "." + (String.valueOf(decimal % 100).length() < 2 ? decimal % 100 + "0" : decimal % 100);
    }

    public static String getXChangedDecimal(String decimal) {
        return decimal.substring(0, 1) + decimal.substring(1, decimal.indexOf(".")).replaceAll("\\d", "X") + decimal.substring(decimal.indexOf("."));
    }

    public static String getJoinedCombo(List<String> numArray) {
        StringBuilder builder = new StringBuilder();
        for (String s : numArray) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static List<String> getThreePermutations(String one, String two, String three) {
        String[] input = {one, two, three};
        List<String> strings = new ArrayList<>();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    if (x != y && y != z && z != x) {
                        String num = input[x] + input[y] + input[z];
                        if (!strings.contains(num)) strings.add(num);
                    }
                }
            }
        }
        return strings;
    }

    public static String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }

    public static List<String> getTwoPermutationsMaryaj(String one, String two, String three) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(one + two);//ABCD
        if (!strings.contains(reverse(one) + reverse(two))) {
            strings.add(reverse(one) + reverse(two));//BADC
        }
        if (!strings.contains(one + three)) {
            strings.add(one + three);//ABEF
        }
        if (!strings.contains(reverse(one) + reverse(three))) {
            strings.add(reverse(one) + reverse(three));//BAFE
        }
        if (!strings.contains(two + three)) {
            strings.add(two + three);//CDEF
        }
        if (!strings.contains(reverse(two) + reverse(three))) {
            strings.add(reverse(two) + reverse(three));//DCFE
        } 
        return strings;
    }

    public static boolean charsEqual(String s) {
        return s.substring(0, 1).equals(s.substring(1, 2));
    }

    public static List<String> getTwoPermutationsMaryajNew(String one, String two, String three) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(one + two);//ABCD
        if (!strings.contains(reverse(one) + reverse(two))) {
            strings.add(reverse(one) + reverse(two));//BADC
        }
        if (!strings.contains(one + three)) {
            strings.add(one + three);//ABEF
        }
        if (!strings.contains(reverse(one) + reverse(three))) {
            strings.add(reverse(one) + reverse(three));//BAFE
        }
        if (!one.equals(three)) {
            if (!strings.contains(two + three)) {
                strings.add(two + three);//CDEF
            }
        } //+
        if (!one.equals(three)) {
            if (!strings.contains(reverse(two) + reverse(three))) {
                strings.add(reverse(two) + reverse(three));//DCFE
            } 
        } // +


        /*New 6*/
        if (!strings.contains(one + reverse(two))) {
            strings.add(one + reverse(two));//ABDC
        }
        if (!one.equals(three)) {
            if (!strings.contains(reverse(one) + two)) {
                strings.add(reverse(one) + two);//BACD
            }
        } //+
        if (!charsEqual(three)) {
            if (!strings.contains(one + reverse(three))) {
                strings.add(one + reverse(three));//ABFE
            }
        }  // +
        if (!one.equals(three) && !charsEqual(three)) {
            if (!strings.contains(reverse(one) + three)) {
                strings.add(reverse(one) + three);//BAEF
            }
        } // ++
        if (!charsEqual(three)) {
            if (!strings.contains(two + reverse(three))) {
                strings.add(two + reverse(three));//CDFE
            }
        } // +
        if (!one.equals(three) && !charsEqual(three)) {
            if (!strings.contains(reverse(two) + three)) {
                strings.add(reverse(two) + three);//DCEF
            }
        } // +

        return strings;
    }

    public static List<String> getTwoPermutations(String one, String two) {
        String[] input = {one, two};
        List<String> strings = new ArrayList<>();
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                if (x != y) {
                    String num = input[x] + "" + input[y];
                    if (!strings.contains(num)) strings.add(num);
                }
            }
        }
        return strings;
    }

    public static String[] getStringArray(String source, int step) {
        String[] stringArray = new String[step == 2 ? source.length() / 2 : source.length()];
        for (int i = 0; i < stringArray.length; i++) {
            stringArray[i] = source.substring(step == 2 ? i * step : i, i * step + step);
        }
        return stringArray;
    }

    public static ArrayList<String> getSeparateTicketNums(String num) {
        ArrayList<String> separateTicketNums = new ArrayList<>();
        int endFirstNumIndex = num.length() % 2 == 0 ? 2 : 3;
        switch (num.length()) {
            case 2:
            case 3:
                separateTicketNums.add(num);
                break;

            case 4:
            case 5:
                separateTicketNums.add(num.substring(0, endFirstNumIndex));
                separateTicketNums.add(num.substring(endFirstNumIndex));
                break;


            case 6:
                separateTicketNums.add(num.substring(0, endFirstNumIndex));
                separateTicketNums.add(num.substring(endFirstNumIndex, endFirstNumIndex + 2));
                separateTicketNums.add(num.substring(endFirstNumIndex + 2));
                break;
        }
        return separateTicketNums;
    }

    public static boolean isDataValid(String date) {
        return date != null && !date.isEmpty();
    }

    public static int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(9);
    }

    public static String getRandomNumberString(int count) {
        String randomNumberString = "";
        for (int i = 0; i < count; i++) {
            randomNumberString += getRandomNumber();
        }
        return randomNumberString;
    }

    public static String capitalize(CharSequence str) {
        return capitalize(str.toString());
    }

    public static String capitalize(String str) {
        try {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        } catch (Exception e) {
            return str;
        }
    }

}
