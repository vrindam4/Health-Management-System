package com.example.healthmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class history extends AppCompatActivity {

    //TODO: Add progress bar

    private TableLayout tTableLayout;
    DatePicker dHistory_date;
    private Button bSearch;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    String e="User";
    private DatabaseReference myRef = database.getReference(e);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        tTableLayout=(TableLayout)findViewById(R.id.history_table);
        dHistory_date = findViewById(R.id.history_datepick);
        bSearch = findViewById(R.id.history_search_button);

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To remove all the previous rows
                tTableLayout.removeAllViews();
                //Searching of data from database
                searching();
            }
        });


    }


    //To date from Datepicker
    private String datefinder(){
        String year = Integer.toString(dHistory_date.getYear());
        String month = Integer.toString(dHistory_date.getMonth()+1);
        String day = Integer.toString(dHistory_date.getDayOfMonth());

        String date = day+'-'+month+'-'+year;
        return date;
    }

    //Seaching data from database
    private void searching(){

        //To get date
        String date = datefinder();
        //Get user name from previous window
        Intent intent_old = getIntent();
        String user_id = intent_old.getStringExtra("User Id");

        //Reference to user and history
        myRef.child(user_id).child("History").child(date).addValueEventListener(eventListener);

    }

    //Listener for retrived data
    ValueEventListener eventListener = new ValueEventListener() {

        String product_cal,product_name,product_date;
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            //Create coloumn names
            TableLayout stk = (TableLayout) findViewById(R.id.history_table);
            stk.setColumnStretchable(0,true);
            stk.setColumnStretchable(1,true);
            stk.setColumnStretchable(2,true);
            TableRow tbrow0 = new TableRow(history.this);

            TextView tv0 = new TextView(history.this);
            tv0.setText(" Time ");
            tv0.setTextSize(25);
            tv0.setGravity(Gravity.CENTER);
            tbrow0.addView(tv0);

            TextView tv1 = new TextView(history.this);
            tv1.setText(" Name ");
            tv1.setTextSize(25);
            tv1.setGravity(Gravity.CENTER);
            tbrow0.addView(tv1);

            TextView tv2 = new TextView(history.this);
            tv2.setText(" Cal ");
            tv2.setTextSize(25);
            tv2.setGravity(Gravity.CENTER);
            tbrow0.addView(tv2);
            stk.addView(tbrow0);

            //Loop for all the retrived data in a sequential order
            for (DataSnapshot ds: dataSnapshot.getChildren()){

                product_date = ds.getKey();
                product_name = ds.child("Product Name").getValue(String.class);
                product_cal = ds.child("Product Cal").getValue(String.class);

                //Add retrived data into rows of table
                TableRow tbrow = new TableRow(history.this);
                TextView t1v = new TextView(history.this);
                t1v.setText(product_date);
                t1v.setTextSize(20);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);
                TextView t2v = new TextView(history.this);
                t2v.setText(product_name);
                t2v.setPadding(25,0,0,0);
                t2v.setTextSize(20);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);
                TextView t3v = new TextView(history.this);
                t3v.setText(product_cal);
                t3v.setPadding(25,0,0,0);
                t3v.setTextSize(20);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);
                stk.addView(tbrow);

            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            //Error on retiveal of data from database
            //TODO: Add the error if any reported
        }
    };
}
