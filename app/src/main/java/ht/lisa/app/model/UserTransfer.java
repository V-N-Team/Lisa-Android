package ht.lisa.app.model;


import java.io.Serializable;

public class UserTransfer implements Serializable {

    public static final String TRANSFER_USER = "transferUser";

    private String phone;
    private String recipient;
    private String amount;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
