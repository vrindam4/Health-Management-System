package com.example.healthmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class admin_value_enter extends AppCompatActivity {

    Button update;
    EditText product_code,product_name,product_cal;
    String pro_code;
    DatabaseReference myChild;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    String e="Data";
    private DatabaseReference myRef = database.getReference(e);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_value_enter);

        Intent intent = getIntent();
        pro_code = intent.getStringExtra("Barcode");



        update = findViewById(R.id.add_data);
        product_code=findViewById(R.id.product_code);
        product_name=findViewById(R.id.product_name);
        product_cal=findViewById(R.id.product_cal);

        product_code.setText(pro_code);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To enter value in database
                checkIfExists();
                //back to home_page
                Intent intent = new Intent(admin_value_enter.this, admin_home.class);
                startActivity(intent);

            }
        });

    }
    private void checkIfExists(){
        //Checking if code already exists

        myChild = myRef.child(pro_code);
        myChild.addListenerForSingleValueEvent(eventListener);

    }

    private void dataUpdate(){

        //Update new value

        String pro_name = product_name.getText().toString();
        String pro_cal = product_cal.getText().toString();

        myChild.child("Product Name").setValue(pro_name);
        myChild.child("Product Cal").setValue(pro_cal);
    }

    //Listener on the data to read
    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            // If record does exists in database
            if(!dataSnapshot.exists()) {
                //To get value from data snapshot
                dataUpdate();
            }
            //Record does not exists in database
            else{
                Toast.makeText(admin_value_enter.this,"Value already exists",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

}
