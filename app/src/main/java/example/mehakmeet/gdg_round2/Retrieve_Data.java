package example.mehakmeet.gdg_round2;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Retrieve_Data extends AppCompatActivity {
    String nor;

    RelativeLayout rl;
    int nor_i;
    Snackbar snackbar;
    ArrayList<String> myList=new ArrayList<>();
    public static String Extra3="bro";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    List<Mydata> values=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve__data);
 char x='a';
        recyclerView=findViewById(R.id.recyclerview);
        rl=findViewById(R.id.rel);
        Intent i=getIntent();
        nor=i.getStringExtra(StoreData.EXTRA1);
        nor_i=Integer.parseInt(nor);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            myList = b.getStringArrayList("KEY");
        }
        Log.i("LIST",myList.get(0));
        mLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
         final FirebaseDatabase database=FirebaseDatabase.getInstance();

            DatabaseReference myRef = database.getReference("Reports");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Mydata data=new Mydata();
                data=dataSnapshot.getValue(Mydata.class);
                values.add(data);

                RecyclerAdapter mAdapter=new RecyclerAdapter(values);
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, final int position) {

                        snackbar.make(rl,"Please Wait.....",8000).show();

                        final Handler handler = new Handler();


                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i;
                                Bundle b=new Bundle();
                                b.putStringArrayList("KEY",myList);
                                i = new Intent(getApplicationContext(),Details.class);
                                i.putExtras(b);
                                i.putExtra(Extra3,String.valueOf(position));
                                startActivity(i);
                                // Do something after 5s = 5000ms
                            }
                        }, 3000);

                    }
                })
        );


    }

}
