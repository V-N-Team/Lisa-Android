package ht.lisa.app.model;

public class Pin {

    private String phone;
    private String pin;

    public Pin(String phone, String pin) {
        this.phone = phone;
        this.pin = pin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
