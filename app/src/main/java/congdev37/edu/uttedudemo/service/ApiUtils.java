package congdev37.edu.uttedudemo.service;

public class ApiUtils {
    //webhost
    public static final String BASE_URL = "http://itestutt.000webhostapp.com/utt_edu_app/";
    //local
//    public static final String BASE_URL = "http://192.168.150.2/utt_edu_app/utt_edu_app/";
    //wifi đt
//    public static final String BASE_URL = "http://192.168.43.215/utt_edu_app/";

    public static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}
