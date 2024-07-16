package ht.lisa.app.model;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ht.lisa.app.R;

public class User {

    public static final String DOB_FORMAT = "yyyyMMdd";
    public static final String DOB_FORMATTED = "dd.MM.yy";

    private String phone;
    private String secret;
    private String name;
    private String surname;
    private String dob;
    private String sex;
    private String email;
    private String ig;
    private String fb;
    private String city;
    private String avatar;

    public User() {
    }

    public User(String phone, String secret, String name, String surname, String dob, String sex, String email, String ig, String fb, String city, String avatar) {
        this.phone = phone;
        this.secret = secret;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.sex = sex;
        this.email = email;
        this.ig = ig;
        this.fb = fb;
        this.city = city;
        this.avatar = avatar;

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getSexValue() {
        if (getSex() == null) return 0;
        return getSex().equals(Sex.M.name()) ? R.string.male : R.string.female;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailValue() {
        return getEmail() == null ? "-" : email;
    }

    public String getIg() {
        return ig;
    }

    public void setIg(String ig) {
        this.ig = ig;
    }

    public String getIgValue() {
        return getFb() == null ? "-" : fb;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getFbValue() {
        return getFb() == null ? "-" : fb;
    }

    public String getFullName() {
        return (getName() == null ? "" : getName()) + " " + (getSurname() == null ? "" : getSurname());
    }

    public String getFormattedForServerDob() {
        Date initDate = null;
        String dob = null;
        if (getDob() != null) {
            try {
                initDate = new SimpleDateFormat(DOB_FORMATTED, Locale.getDefault()).parse(getDob());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (initDate != null) {
                dob = new SimpleDateFormat(DOB_FORMAT, Locale.getDefault()).format(initDate);
            }
        }
        return dob;
    }

    public String getFormattedDob() {
        Date initDate = null;
        try {
            initDate = new SimpleDateFormat(DOB_FORMAT, Locale.getDefault()).parse(getDob());
        } catch (NullPointerException | ParseException e) {
            e.printStackTrace();
        }
        String dob = "";
        if (initDate != null) {
            dob = new SimpleDateFormat(DOB_FORMATTED, Locale.getDefault()).format(initDate);
        }

        return dob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public enum Sex {
        M("Male"),
        F("Female");

        private String stringValue;

        Sex(String toString) {
            stringValue = toString;
        }

        @NonNull
        @Override
        public String toString() {
            return stringValue;
        }
    }
}
