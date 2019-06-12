package congdev37.edu.uttedudemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Question implements Parcelable, Serializable {

    @SerializedName("questionID")
    @Expose
    private String questionID;
    @SerializedName("subCode")
    @Expose
    private String subCode;
    @SerializedName("quesContent")
    @Expose
    private String quesContent;
    @SerializedName("ansA")
    @Expose
    private String ansA;
    @SerializedName("ansB")
    @Expose
    private String ansB;
    @SerializedName("ansC")
    @Expose
    private String ansC;
    @SerializedName("ansD")
    @Expose
    private String ansD;
    @SerializedName("ansCorrect")
    @Expose
    private String ansCorrect;
    public int choiceID;
    private String Answer;
    private boolean choose;

    public Question(Parcel in) {
        questionID = in.readString();
        subCode = in.readString();
        quesContent = in.readString();
        ansA = in.readString();
        ansB = in.readString();
        ansC = in.readString();
        ansD = in.readString();
        ansCorrect = in.readString();
    }

    public Question() {
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getQuesContent() {
        return quesContent;
    }

    public void setQuesContent(String quesContent) {
        this.quesContent = quesContent;
    }

    public String getAnsA() {
        return ansA;
    }

    public void setAnsA(String ansA) {
        this.ansA = ansA;
    }

    public String getAnsB() {
        return ansB;
    }

    public void setAnsB(String ansB) {
        this.ansB = ansB;
    }

    public String getAnsC() {
        return ansC;
    }

    public void setAnsC(String ansC) {
        this.ansC = ansC;
    }

    public String getAnsD() {
        return ansD;
    }

    public void setAnsD(String ansD) {
        this.ansD = ansD;
    }

    public String getAnsCorrect() {
        return ansCorrect;
    }

    public void setAnsCorrect(String ansCorrect) {
        this.ansCorrect = ansCorrect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionID);
        dest.writeString(subCode);
        dest.writeString(quesContent);
        dest.writeString(ansA);
        dest.writeString(ansB);
        dest.writeString(ansC);
        dest.writeString(ansD);
        dest.writeString(ansCorrect);
    }
}