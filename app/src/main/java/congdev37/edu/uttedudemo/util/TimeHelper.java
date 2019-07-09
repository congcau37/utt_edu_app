package congdev37.edu.uttedudemo.util;

import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeHelper {

    private static TimeHelper instance;
    SimpleDateFormat simple;

    public TimeHelper() {
        simple = new SimpleDateFormat("dd/MM/yyyy");
    }

    public static TimeHelper getInstance() {
        if (instance == null) {
            instance = new TimeHelper();
        }
        return instance;
    }

    //hôm nay
    public TimeInterval getToday() {
        TimeInterval interval = new TimeInterval();
        Calendar calendar = Calendar.getInstance();
        String time = simple.format(calendar.getTime());
        interval.setStartTime(time);
        interval.setEndTime("today");
        interval.setType(0);
        return interval;
    }

    //tuần này
    public TimeInterval getThisWeek() {
        TimeInterval interval = new TimeInterval();
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 1);
        } else {
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 7 + 1);
        }
        interval.setStartTime(simple.format(calendar.getTime()));
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 7 - 1);
        interval.setEndTime(simple.format(calendar.getTime()));
        interval.setType(1);
        return interval;
    }

    //tháng này
    public TimeInterval getThisMonth() {
        TimeInterval interval = new TimeInterval();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        interval.setStartTime(simple.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        interval.setEndTime(simple.format(calendar.getTime()));
        interval.setType(2);
        return interval;
    }

    // 7 ngày gần đây
    public TimeInterval get7Days() {
        TimeInterval interval = new TimeInterval();
        Calendar calendar = Calendar.getInstance();
        interval.setEndTime(simple.format(calendar.getTime()));
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 7 + 1);
        interval.setStartTime(simple.format(calendar.getTime()));
        interval.setType(3);
        return interval;
    }

    //30 ngày
    public TimeInterval get30Days() {
        TimeInterval interval = new TimeInterval();
        Calendar calendar = Calendar.getInstance();
        interval.setEndTime(simple.format(calendar.getTime()));
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 30 + 1);
        interval.setStartTime(simple.format(calendar.getTime()));
        interval.setType(4);
        return interval;
    }

    //mọi lúc
    public TimeInterval getAll() {
        TimeInterval interval = new TimeInterval();
        interval.setStartTime("");
        interval.setEndTime("");
        interval.setType(-2);
        return interval;
    }

    public static String convertToServerTime(String time) {
        DateFormat convert = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = convert.parse(time);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(date);
            return dateString;
        } catch (ParseException e) {
            return time;
        }
    }

    public static String convertToLabelTime(String time) {
        DateFormat convert = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = convert.parse(time);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM");
            String dateString = dateFormat.format(date);
            return dateString;
        } catch (ParseException e) {
            return time;
        }
    }

}
