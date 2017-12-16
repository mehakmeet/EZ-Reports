package example.mehakmeet.gdg_round2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Details extends AppCompatActivity {

    String nor;
    int nor_i;
    ImageView image;
    TextView sum, link;
    ProgressBar pb;
    ArrayList<String> myList = new ArrayList<>();

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();
                InputStream is = connection.getInputStream();
                Bitmap image = BitmapFactory.decodeStream(is);
                return image;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection urlConnection = null;
            String result = "";

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream is = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }

                return result;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        pb = findViewById(R.id.progressBar5);
        Intent i = getIntent();
        nor = i.getStringExtra(Retrieve_Data.Extra3);
        nor_i = Integer.parseInt(nor);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            myList = b.getStringArrayList("KEY");
        }

        image = findViewById(R.id.report_img);
        sum = findViewById(R.id.summary);
        link = findViewById(R.id.pdf);

        Log.i("API", myList.get(nor_i));

               DownloadImageTask imageTask=new DownloadImageTask();
                DownloadTask task=new DownloadTask();
                String result=null;

                try {
                    result=task.execute(myList.get(nor_i)).get();


                    try {


                        Bitmap img;

                        JSONObject jsonObject=new JSONObject(result);

                        String info=jsonObject.getString("data");
                        Log.i("DATATA",info);
                        JSONArray jarr=new JSONArray(info);
                        JSONObject jsonObject1=jarr.getJSONObject(0);
                        String field=jsonObject1.getString("fields");


                        JSONObject jobj=new JSONObject(field);

                        String body=jobj.getString("body");
                        sum.setText(body);

                        String file=jobj.getString("file");
                        JSONArray jarr2=new JSONArray(file);
                        JSONObject jobj2=jarr2.getJSONObject(0);

                        String link_to_pdf=jobj2.getString("url");
                        String link_set="<a href=\'"+link_to_pdf+"\'>Click here to Download the pdf !!</a>";
                        link.setText(Html.fromHtml(link_set));
                        link.setMovementMethod(LinkMovementMethod.getInstance());


                        String preview=jobj2.getString("preview");
                        JSONObject jobj3=new JSONObject(preview);
                        String img_url=jobj3.getString("url-small");
                        img=imageTask.execute(img_url).get();
                        image.setImageBitmap(img);

                        Log.i("LINK TO PDF",link_to_pdf);
                        Log.i("LINK TO IMAGE",img_url);



                        Log.i("BODY",body);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
    }



}
