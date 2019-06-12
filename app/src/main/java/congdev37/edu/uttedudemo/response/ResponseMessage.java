package congdev37.edu.uttedudemo.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseMessage {
    @SerializedName("success")
    @Expose
    int success;
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("type")
    @Expose
    String type;

    public ResponseMessage(int success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResponseMessage() {
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
