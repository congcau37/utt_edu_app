package congdev37.edu.uttedudemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Test implements Parcelable, Serializable {

    @SerializedName("testID")
    @Expose
    private String testID;
    @SerializedName("questionID")
    @Expose
    private String questionID;
    @SerializedName("subjectCode")
    @Expose
    private String subjectCode;
    @SerializedName("testName")
    @Expose
    private String testName;
    @SerializedName("Level")
    @Expose
    private String level;
    @SerializedName("Time")
    @Expose
    private String time;
    @SerializedName("createDay")
    @Expose
    private String createDay;

    private boolean testStatus;

    public Test() {
    }

    public Test(Parcel in) {
        testID = in.readString();
        questionID = in.readString();
        subjectCode = in.readString();
        testName = in.readString();
        level = in.readString();
        time = in.readString();
        createDay = in.readString();
        testStatus = in.readByte() != 0;
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel in) {
            return new Test(in);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };

    public boolean isTestStatus() {
        return testStatus;
    }

    public void setTestStatus(boolean testStatus) {
        this.testStatus = testStatus;
    }

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(testID);
        dest.writeString(questionID);
        dest.writeString(subjectCode);
        dest.writeString(testName);
        dest.writeString(level);
        dest.writeString(time);
        dest.writeString(createDay);
        dest.writeByte((byte) (testStatus ? 1 : 0));
    }
}
