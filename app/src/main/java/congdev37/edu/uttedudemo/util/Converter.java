package congdev37.edu.uttedudemo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.graphics.PorterDuff.Mode.ADD;

public class Converter {

    /**
     * Mục đính của methob:hàm chuyển cấp độ
     * @Create_by: trand
     * @Date: 7/9/2019
     * @param num : số
     * @return String
     */
    public static String convertLevel(String num){
        String level="";
        if(num.equals("1")){
            level = "Dễ";
        }else if(num.equals("2")){
            level = "Trung bình";
        }else {
            level = "Khó";
        }
        return level;
    }

    /**
     * Mục đính của methob: hàm băm text ra thành mảng
     * @Create_by: trand
     * @Date: 7/9/2019
     * @param question_ID: chuỗi mã câu hỏi
     * @return ArrayList<String>
     */
    public static ArrayList<String> splitQuestionID(String question_ID) {
        ArrayList<String> arrQuesID = new ArrayList<>();
        arrQuesID.clear();
        String[] arr = question_ID.split(",");
        for (String name : arr)
            arrQuesID.add(name);
        return arrQuesID;
    }

    /**
     * Mục đính của methob: hàm chuyển đổi list thành text
     * @Create_by: trand
     * @Date: 7/9/2019
     * @param arrayList: mảng
     * @return String
     */
    public static String convertAnswer(ArrayList<String> arrayList){
        String answer = "";
        for (String a: arrayList) {
            answer +=a+",";
        }
        return answer;
    }
//    public static String convertQuestionID(ArrayList<String> arrayList){
//        String answer = "";
//        for (String a: arrayList) {
//            answer +=a+",";
//        }
//        return answer;
//    }

    //hàm lấy ra ngày hiện tại
    public static String setDate() {
        String date = "";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy/MM/dd");
        date = simple.format(calendar.getTime());
        return date;
    }

    //hàm chuyển đổi định dạng ngày
    public static String setDate(String date) {
        String strDateTime = "";
        try {
            DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date da = (Date)inputFormatter.parse(date);
            //
            DateFormat outputFormatter = new SimpleDateFormat("dd-MM-yyyy");
            strDateTime = outputFormatter.format(da);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDateTime;
    }
}
