package example.mehakmeet.gdg_round2.API;

/**
 * Created by MEHAKMEET on 16-12-2017.
 */


public class ApiUtils {

    private ApiUtils(){}

    public static final String BASE_URL = "https://api.reliefweb.int/";

    public static ApiInterface getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(ApiInterface.class);
    }
}
