package com.example.healthmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.ValueEventListener;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class admin_home extends AppCompatActivity implements ValueEventListener {

    private ZXingScannerView scannerView;
    Button bScanner,bAddData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);

        //Scanner button
        bScanner = findViewById(R.id.admin_barcode_scan);
        bAddData = findViewById(R.id.admin_add_data);


        //On clicking scan button
        bScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Callind scan barcode function
                scanBarcode(view);
            }
        });

        bAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_home.this,add_data.class);
                startActivity(intent);
                scannerView = new ZXingScannerView(admin_home.this);
                scannerView.stopCamera();
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



    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler
    {

        @Override
        public void handleResult(com.google.zxing.Result result) {

            //Task after scanning barcode
            String sResultCode = result.getText();
            Toast.makeText(admin_home.this,sResultCode,Toast.LENGTH_SHORT).show();
            scannerView.stopCamera();
            Intent intent = new Intent(admin_home.this,admin_value_enter.class);
            intent.putExtra("Barcode",sResultCode);
            startActivity(intent);
        }
    }
}
