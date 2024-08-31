package com.example.healthmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class add_data extends AppCompatActivity {


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Database reference to User entry values
    private DatabaseReference myRef = database.getReference("User_Entry");

    Spinner spinner;
    EditText eProductName,eProductCal;
    Button bUpdate;
    String selected_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_data);

        spinner = (Spinner) findViewById(R.id.admin_code);
        eProductCal = findViewById(R.id.admin_product_cal);
        eProductName = findViewById(R.id.admin_product_name);
        bUpdate = findViewById(R.id.admin_add_data);

        //Get Data from databse user entry
        getProductCode();

        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Update to Data database and delete from user entry
                updateToDatbase();

                //Repeate
                getProductCode();
            }
        });

    }


    private void getProductCode(){

        spinner.setAdapter(null);

        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select Barcode");

        //Add values to spinner from database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){

                    String product_code = ds.getKey();
                    arrayList.add(product_code);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Set retrived values to spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_code = parent.getItemAtPosition(position).toString();

                //Get data from database if any barcode selected
                if(!selected_code.equals("Select Barcode")){
                    getProductNameCal(selected_code);
                }
                else {
                    eProductCal.setText("");
                    eProductName.setText("");
                    Toast.makeText(add_data.this,"Please select valid code",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
    }

    //Function to get name and cal
    private void getProductNameCal(String code){

        myRef.child(code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Get Values from database
                String database_name = dataSnapshot.child("Product Name").getValue(String.class);
                String database_cal = dataSnapshot.child("Product Cal").getValue(String.class);

                //Add values to textfield
                eProductName.setText(database_name);
                eProductCal.setText(database_cal);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Function to update database
    private void updateToDatbase(){
            String name = eProductName.getText().toString();
            String cal = eProductCal.getText().toString();

            //Add values to Data database
            DatabaseReference myRef1 = database.getReference("Data");
            DatabaseReference myChild = myRef1.child(selected_code);
            myChild.child("Product Name").setValue(name);
            myChild.child("Product Cal").setValue(cal);

            //Remove values from user_entry
            myRef.child(selected_code).removeValue();

    }

}
