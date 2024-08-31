package com.example.healthmanagement;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class after_scan extends AppCompatActivity {

    EditText product_name,product_cal;
    Button product_add;
    DatePicker product_date;

    String scanned_barcode,user_id;
    String database_cal,database_name;


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    String e="User";
    private DatabaseReference myRef = database.getReference(e);

    DatabaseReference myDataRef = database.getReference("Data");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_scan);

        product_name = findViewById(R.id.user_product_name);
        product_cal = findViewById(R.id.user_product_cal);
        product_add = findViewById(R.id.user_add_data);
        product_date = findViewById(R.id.user_product_date);

        //To get details of scanned product from database
        getDataFromDatabase();

        product_add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                //After clicking button to update in datbase
                addToDatabase();
                Toast.makeText(after_scan.this,"Added Succesfully",Toast.LENGTH_SHORT).show();
            }
        });

    }

    //To get data from database
    private void getDataFromDatabase(){

        //Get data from previous page
        Intent intent_old = getIntent();
        scanned_barcode = intent_old.getStringExtra("Scanned_Barcode");
        user_id = intent_old.getStringExtra("User Id");

        myDataRef.child(scanned_barcode).addListenerForSingleValueEvent(eventListener);

    }

    //Listener on the data to read
    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            // If record does exists in database
            if(dataSnapshot.exists()) {

                database_cal = dataSnapshot.child("Product Cal").getValue(String.class);
                database_name = dataSnapshot.child("Product Name").getValue(String.class);

                product_cal.setText(database_cal);
                product_name.setText(database_name);

            }
            //Record does not exists in database
            else{
                Toast.makeText(after_scan.this,"Please enter values",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    //To add the data to user history database
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addToDatabase(){

        String user_cal,user_name;

        //Get data from user text field
        user_cal = product_cal.getText().toString();
        user_name  = product_name.getText().toString();

        String year = Integer.toString(product_date.getYear());
        String month = Integer.toString(product_date.getMonth()+1);
        String day = Integer.toString(product_date.getDayOfMonth());
        String date = day+'-'+month+'-'+year;

        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        //Adding data user history in database
        DatabaseReference myChild = myRef.child(user_id).child("History").child(date).child(time.format(formatter));
        myChild.child("Product Cal").setValue(user_cal);
        myChild.child("Product Name").setValue(user_name);

        //Addign new data to separate database to update database later by admin
        addUnidentifiedBarcode(user_cal,user_name);

        //Going to home page with user id
        Intent intent =new Intent(after_scan.this, home_page.class);
        intent.putExtra("User Id",user_id);
        startActivity(intent);

    }

    //Adding to seprate database
    private void addUnidentifiedBarcode(String user_cal,String user_name){
        DatabaseReference myData = database.getReference("User_Entry");
        DatabaseReference  myChild = myData.child(scanned_barcode);

        myChild.child("Product Name").setValue(user_name);
        myChild.child("Product Cal").setValue(user_cal);

    }




}

