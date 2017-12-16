package example.mehakmeet.gdg_round2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import example.mehakmeet.gdg_round2.API.ApiInterface;
import example.mehakmeet.gdg_round2.API.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreData extends AppCompatActivity {

    public static String EXTRA1="dude";
    public static String EXTRA2="dude";
    Snackbar snackbar;
    int nor_i;
    String nor;
    ImageButton store;
    ProgressBar pb;
    HashMap<String,String> dataMap=new HashMap<String, String>();
    private RelativeLayout rl;
    DatabaseReference mDatabase;
    ApiInterface mAPIService;

    ArrayList<String> mylist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_data);


        Intent i=getIntent();
        rl=(RelativeLayout)findViewById(R.id.myLayout);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        store=(ImageButton)findViewById(R.id.send_data_btn);
        nor=i.getStringExtra(MainActivity.EXTRA);
        nor_i=Integer.parseInt(nor);
        mAPIService = ApiUtils.getAPIService();
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pb.setAlpha(1);
                sendPost();
            }
        });

    }
    public void sendPost() {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("limit",nor);
        Log.d("tagg","URL: "+mAPIService.savePost(jsonObject).request().url().toString());
        mAPIService.savePost(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //Log.i("tagg",call.request().url().toString());
                if(response.isSuccessful()) {
                   // Log.i("RESPONSE", String.valueOf(response.body().toString()));
                    try {
                        JSONObject jObj=new JSONObject(response.body().toString());

                        String data=jObj.getString("data");

                        JSONArray jarr=new JSONArray(data);
                        int i;
                        char x='a';
                        Log.i("DATA",data);
                        for(i=0;i<nor_i;i++) {
                            JSONObject data_jo = jarr.getJSONObject(i);
                            String api=data_jo.getString("href");

                            mylist.add(api);

                            Log.i("APIS:",api);
                            JSONObject field = new JSONObject(data_jo.getString("fields"));
                            String title = field.getString("title");

                            Log.i("TITLE", title);
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Reports" );
                            dataMap.put("Title", title);

                            mDatabase.child("Title "+x++).setValue(dataMap);
                            if(i==nor_i-1)
                            mDatabase.child("Title "+x++).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(),"Reports Successfully Stored on Firebase :)",Toast.LENGTH_SHORT).show();
                                    pb.setAlpha(0);
                                    Bundle b=new Bundle();
                                    b.putStringArrayList("KEY",mylist);
                                     Intent intent=new Intent(StoreData.this,Retrieve_Data.class);
                                    intent.putExtra(EXTRA1,nor);
                                    intent.putExtras(b);
                                   // intent.putExtra(EXTRA2,mylist);
                                    startActivity(intent);
                                }
                            });

                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                else
                {
                    Log.i("RESPONSE","Failed");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {


                Log.i("RESPONSE","OnFailure...Failed");
            }
        });
    }
}
