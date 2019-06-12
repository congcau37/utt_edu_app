package congdev37.edu.uttedudemo.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Converter {

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

    public static ArrayList<String> splitQuestionID(String question_ID) {
        ArrayList<String> arrQuesID = new ArrayList<>();
        arrQuesID.clear();
        String[] arr = question_ID.split(",");
        for (String name : arr)
            arrQuesID.add(name);
        return arrQuesID;
    }

    public static String convertAnswer(ArrayList<String> arrayList){
        String answer = "";
        for (String a: arrayList) {
            answer +=a+",";
        }
        return answer;
    }
    public static String convertQuestionID(ArrayList<String> arrayList){
        String answer = "";
        for (String a: arrayList) {
            answer +=a+",";
        }
        return answer;
    }

    public static String setDate() {
        String date = "";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy/MM/dd");
        date = simple.format(calendar.getTime());
        return date;
    }
}
