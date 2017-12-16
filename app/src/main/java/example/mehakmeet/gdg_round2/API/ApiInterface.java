package example.mehakmeet.gdg_round2.API;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by MEHAKMEET on 16-12-2017.
 */


public interface ApiInterface {
    @POST("v1/reports?appname=apidoc")
    Call<JsonObject> savePost(@Body JsonObject jsonObject);
}
