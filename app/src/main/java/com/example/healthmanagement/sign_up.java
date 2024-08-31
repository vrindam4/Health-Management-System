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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class sign_up extends AppCompatActivity {

    Button bSignup;
    EditText eName,ePassword,eEmail,ePhone;
    //Reference for parent database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    String e="User";
    private DatabaseReference myRef = database.getReference(e);

//    void removeDataFromDatabase(){
//        DatabaseReference root = FirebaseDatabase.getInstance().getReference("Data");
//        root.setValue(null);
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        bSignup = findViewById(R.id.signup_button);
        eName=findViewById(R.id.signup_name);
        ePassword=findViewById(R.id.signup_password);
        eEmail=findViewById(R.id.signup_emailid);
        ePhone=findViewById(R.id.signup_phone);

        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                removeDataFromDatabase();
                //Check if user already exists
                checkUser();
            }
        });
    }


    private void checkUser(){
        //To check if user already exists in database using phone number
        String phone=ePhone.getText().toString();
        //Trying to retrive data from phone
        DatabaseReference child =myRef.child(phone);
        child.addListenerForSingleValueEvent(eventListener);
    }

    ValueEventListener eventListener =new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            // If record does not exists then add new record
            if(!dataSnapshot.exists()) {

                //Adding details to database
                signup();
            }

            //Record already exists in database
            else{
                Toast.makeText(sign_up.this,"User already exists",Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void signup(){
        //Function to add values to database from the user input

        String phone=ePhone.getText().toString();
        String name=eName.getText().toString();
        String password=ePassword.getText().toString();
        String email=eEmail.getText().toString();

        if(!(emailCheck(email) && phoneCheck(phone))){
            Toast.makeText(sign_up.this,"Email or Phone format incorrect",Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference user = myRef.child(phone).child("Details");

        user.child("Name").setValue(name);
        user.child("password").setValue(password);
        user.child("phone").setValue(phone);
        user.child("Email").setValue(email);

        //Succes msg
        Toast.makeText(sign_up.this,"Succesful",Toast.LENGTH_SHORT).show();

        //Going to Login page
        Intent intent = new Intent(sign_up.this,login_page.class);
        startActivity(intent);
    }

    private boolean emailCheck(String mailadd ){
        if(mailadd.contains("@")){
            return true;
        }
        else{
            return false;
        }
    }
    private boolean phoneCheck(String phoneNum){
        if(phoneNum.length()==10){
            return true;
        }
        else
            return false;
    }

}
