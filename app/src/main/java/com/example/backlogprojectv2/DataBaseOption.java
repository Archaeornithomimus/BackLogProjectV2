package com.example.backlogprojectv2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DataBaseOption extends AppCompatActivity {

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_option);

        Button clickButtonResetApp = findViewById(R.id.buttonResetApp);
        clickButtonResetApp.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                db.cleanTable();
            }
        });

    }


}