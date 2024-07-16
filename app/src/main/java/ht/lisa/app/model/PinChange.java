package ht.lisa.app.model;

import com.google.gson.annotations.SerializedName;

public class PinChange {

    private String phone;
    @SerializedName("current")
    private String currentPin;
    @SerializedName("new")
    private String newPin;

    public PinChange(String phone, String currentPin, String newPin) {
        this.phone = phone;
        this.currentPin = currentPin;
        this.newPin = newPin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCurrentPin() {
        return currentPin;
    }

    public void setCurrentPin(String currentPin) {
        this.currentPin = currentPin;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }
}
