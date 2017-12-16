package example.mehakmeet.gdg_round2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText nor;
    ImageButton ok;
    public static String EXTRA="bro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nor=(EditText)findViewById(R.id.no_of_reports);
        ok=(ImageButton)findViewById(R.id.ok_btn);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(nor.getText().toString())<=0 || nor.getText().toString()==""){
                    Toast.makeText(getApplicationContext(),"Please Enter a number greater than zero :)",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i=new Intent(MainActivity.this,StoreData.class);
                    i.putExtra(EXTRA,nor.getText().toString());
                    startActivity(i);
                }
            }
        });


    }
}
