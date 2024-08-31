package com.example.healthmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class home_page extends AppCompatActivity implements ValueEventListener {

    private ZXingScannerView scannerView;
    Button bScanner,bHistory;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.home_page);

            //Scanner button
            bScanner=findViewById(R.id.barcode_scan);
            bHistory = findViewById(R.id.history_button);

            //On Clicking history button
            bHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //To send user id to next page
                    Intent intent_old = getIntent();
                    String user_id = intent_old.getStringExtra("User Id");

                    //To move to history page
                    Intent intent = new Intent(home_page.this,history.class);
                    intent.putExtra("User Id",user_id);
                    startActivity(intent);

                    scannerView = new ZXingScannerView(home_page.this);
                    scannerView.stopCamera();
                }
            });

            //On clicking scan button
            bScanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Calling scan barcode function
                    scanBarcode(view);
                }
            });

        }


        //Barcode scanning functions
        public void scanBarcode(View view){
            scannerView = new ZXingScannerView(this);
            scannerView.setResultHandler(new ZXingScannerResultHandler());
            setContentView(scannerView);
            scannerView.startCamera();
        }

        @Override
        public void onPause(){
        super.onPause();
        scannerView.stopCamera();
        }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }



    //Listener for home_page
    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler
        {

            @Override
            public void handleResult(com.google.zxing.Result result) {

                //Task after scanning barcode
                String sResultCode = result.getText();
//                Toast.makeText(home_page.this,sResultCode,Toast.LENGTH_SHORT).show();
                scannerView.stopCamera();

                //To send user id to next page
                Intent intent_old = getIntent();
                String user_id = intent_old.getStringExtra("User Id");

                //To nect page with barcode adn user id
                Intent intent =new Intent(home_page.this,after_scan.class);
                intent.putExtra("Scanned_Barcode",sResultCode);
                intent.putExtra("User Id",user_id);
                startActivity(intent);
            }
        }
}
