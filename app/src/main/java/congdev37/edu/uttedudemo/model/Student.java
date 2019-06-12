package congdev37.edu.uttedudemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Student {

    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("stdName")
    @Expose
    private String stdName;
    @SerializedName("stdCode")
    @Expose
    private String stdCode;
    @SerializedName("Birthday")
    @Expose
    private String birthday;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Class")
    @Expose
    private String _class;
    @SerializedName("Gender")
    @Expose
    private String gender;

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName;
    }

    public String getStdCode() {
        return stdCode;
    }

    public void setStdCode(String stdCode) {
        this.stdCode = stdCode;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
