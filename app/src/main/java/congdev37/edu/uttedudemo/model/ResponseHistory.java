package congdev37.edu.uttedudemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseHistory implements Serializable {

    @SerializedName("exID")
    @Expose
    private String exID;
    @SerializedName("testName")
    @Expose
    private String testName;
    @SerializedName("questionID")
    @Expose
    private String questionID;
    @SerializedName("studentCode")
    @Expose
    private String studentCode;
    @SerializedName("Answer")
    @Expose
    private String answer;
    @SerializedName("exDay")
    @Expose
    private String exDay;
    @SerializedName("Score")
    @Expose
    private String score;
    @SerializedName("subjectCode")
    @Expose
    private String subjectCode;
    @SerializedName("subjectName")
    @Expose
    private String subjectName;
    @SerializedName("Level")
    @Expose
    private String Level;

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getExID() {
        return exID;
    }

    public void setExID(String exID) {
        this.exID = exID;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExDay() {
        return exDay;
    }

    public void setExDay(String exDay) {
        this.exDay = exDay;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

}