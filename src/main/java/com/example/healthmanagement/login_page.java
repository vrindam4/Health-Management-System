package com.example.healthmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_page extends AppCompatActivity {
    Button Bbutton_login,bSignup;
    EditText eUsername,ePassword;
    String sUsername,sPassword;
    String databasePassword;
    final private int REQUEST_CODE_CAMERA = 1;


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    String e="User";
    private DatabaseReference myRef = database.getReference(e);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        //To read all values from xml file or page

        Bbutton_login = findViewById(R.id.login);
        eUsername = findViewById(R.id.user_name);
        ePassword = findViewById(R.id.password);
        bSignup = findViewById(R.id.login_signup);

        int hasCameraPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

        }

        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.CAMERA},
                        REQUEST_CODE_CAMERA);
            }
            //return;
        }

        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(login_page.this,sign_up.class);
                startActivity(intent);
            }
        });

        ((View) Bbutton_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get Login details
                sUsername = eUsername.getText().toString();
                sPassword = ePassword.getText().toString();
                if((sUsername.equals("admin")) && (sPassword.equals("admin123"))) {
                    Intent intent = new Intent(login_page.this, admin_home.class);
                    startActivity(intent);
                    return;
                }
                checkUsername();

            }
        });
    }

    private void checkUsername(){
        //To check if phone number exists and retrive if exists
        DatabaseReference mychild = myRef.child(sUsername).child("Details");
        mychild.addListenerForSingleValueEvent(eventListener);
    }

    private void checkPassword(){
        if(databasePassword.equals(sPassword)){
            //To go to Scanner page
            Intent intent =new Intent(login_page.this, home_page.class);
            intent.putExtra("User Id",sUsername);
            startActivity(intent);
        }
        else{
            //If password does not match
            Toast.makeText(getApplicationContext(),"Invalid Password",Toast.LENGTH_SHORT).show();
        }
    }

//Listener on the data to read
    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            // If record does exists in database
            if(dataSnapshot.exists()) {
                //To get value from data snapshot
                databasePassword = dataSnapshot.child("password").getValue(String.class);
                //Check details with password
                checkPassword();
            }
            //Record does not exists in database
            else{
                Toast.makeText(login_page.this,"User does not exists",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


}
