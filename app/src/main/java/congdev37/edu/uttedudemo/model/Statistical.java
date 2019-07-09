package congdev37.edu.uttedudemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statistical {
    @SerializedName("exID")
    @Expose
    private String exID;
    @SerializedName("exDay")
    @Expose
    private String exDay;
    @SerializedName("studentCode")
    @Expose
    private String studentCode;

    public String getExID() {
        return exID;
    }

    public void setExID(String exID) {
        this.exID = exID;
    }

    public String getExDay() {
        return exDay;
    }

    public void setExDay(String exDay) {
        this.exDay = exDay;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String subjectCode) {
        this.studentCode = subjectCode;
    }

}

