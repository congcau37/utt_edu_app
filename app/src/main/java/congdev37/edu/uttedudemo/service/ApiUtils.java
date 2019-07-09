package congdev37.edu.uttedudemo.service;

public class ApiUtils {
    //webhost
//    public static final String BASE_URL = "http://uttedu.000webhostapp.com/utt_edu_app/";
    //local
//    public static final String BASE_URL = "http://192.168.15.107/utt_edu_app/";
    //wifi đt
    private static final String BASE_URL = "http://192.168.15.107/utt_edu_app/";
//    public static final String BASE_URL = "http://localhost/utt_edu_app/";


    public static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}
