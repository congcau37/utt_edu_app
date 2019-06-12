package congdev37.edu.uttedudemo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeInterval {

    int type;
    String startTime;
    String endTime;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTimeToServer(){
        return TimeHelper.convertToServerTime(startTime);
    }

    public String getEndTimeToServer(){
        return TimeHelper.convertToServerTime(endTime);
    }

    public long getStartTimeToMilliSecond(){
        long time = 0;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = format.parse(startTime);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public long getEndTimeToMilliSecond(){
        long time = 0;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = format.parse(endTime);
            time = date.getTime()+24*60*60*1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
