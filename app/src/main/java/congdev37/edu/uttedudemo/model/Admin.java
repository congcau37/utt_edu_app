package congdev37.edu.uttedudemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Admin {

    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("teacherCode")
    @Expose
    private String teacherCode;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Birthday")
    @Expose
    private String birthday;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Gender")
    @Expose
    private String gender;

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
