package ht.lisa.app.util;

import android.util.Base64;

import java.util.Date;
import java.util.Locale;

public class PaymentsInfo {
    public static final String MIME_TYPE = "text/html";
    public static final String ENCODING = "UTF-8";

    /**
     * 1st argument = BusinessKey
     * 2nd argument = Amount
     * 3rd argument= OrderId
     */
    private static final String PAYMENT_HTML = "<form method=\"post\" action=\"https://sandbox.moncashbutton.digicelgroup.com/Moncash-middleware/Checkout/%s\" >\n" +
            "        <input type=\"hidden\" name=\"amount\" value=\"%s\"/>\n" +
            "        <input type=\"hidden\" name=\"orderId\" value=\"%s\"/>\n" +
            "        <input type=\"image\" name=\"ap_image\" src=\"https://sandbox.moncashbutton.digicelgroup.com/Moncash-middleware/resources/assets/images/MC_button.png\"/>\n" +
            "    </form>";

    private static final String BUSINESS_KEY = "YVZOUFJVVkZhbGxVUWxFOSBjekZhV1hKd1NYRnFjVkJzYkdOUFdrZHpZbkl3UVQwOQ==";

    public static String getAmount(int amount) {
       return encodeBase64(amount);
    }

    private static String encodeBase64(String text) {
        byte[] bytesEncoded = Base64.encode(text.getBytes(), Base64.DEFAULT);
        return new String(bytesEncoded);
    }
    private static String encodeBase64(int amount) {
        return encodeBase64(String.valueOf(amount));
    }

    private static String getOrderId() {
        return encodeBase64(String.valueOf(DateTimeUtil.getCurrentTime()));
    }

    public static String getPaymentHtml(int amount) {
        return String.format(Locale.getDefault(), PAYMENT_HTML, BUSINESS_KEY, getAmount(amount), getOrderId());
    }
}
