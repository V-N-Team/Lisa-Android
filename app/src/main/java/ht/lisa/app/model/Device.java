package ht.lisa.app.model;

import com.google.gson.annotations.SerializedName;

public class Device {

    private String phone;
    @SerializedName("device_type")
    private String deviceType;
    @SerializedName("device_id")
    private String deviceID;

    public Device(String phone, String deviceType, String deviceID) {
        this.phone = phone;
        this.deviceType = deviceType;
        this.deviceID = deviceID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
