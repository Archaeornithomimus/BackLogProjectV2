package com.example.backlogprojectv2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager monManager = getFragmentManager();
        FragmentTransaction transaction = monManager.beginTransaction();
        ToDoFragment toDoFragment = new ToDoFragment();
        transaction.add(R.id.fragmentDynamic,toDoFragment);
        transaction.commit();

        Button toDoButton = findViewById(R.id.ButtonToDo);
        toDoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ToDoFragment toDoFragment = new ToDoFragment();
                FragmentTransaction transaction = monManager.beginTransaction();
                transaction.replace(R.id.fragmentDynamic,toDoFragment).addToBackStack(null);
                transaction.commit();
            }
        });

        Button doneButton = findViewById(R.id.ButtonDone);
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DoneFragment doneFragment = new DoneFragment();
                FragmentTransaction transaction = monManager.beginTransaction();
                transaction.replace(R.id.fragmentDynamic,doneFragment).addToBackStack(null);
                transaction.commit();
            }
        });

        Button inProgressButton = findViewById(R.id.ButtonInProgress);
        inProgressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InProgressFragment inProgressFragment = new InProgressFragment();
                FragmentTransaction transaction = monManager.beginTransaction();
                transaction.replace(R.id.fragmentDynamic,inProgressFragment).addToBackStack(null);
                transaction.commit();
            }
        });
















    }

}