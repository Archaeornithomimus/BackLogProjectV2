package com.example.backlogprojectv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DatabaseHandler db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHandler(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout) ;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START) ;
        } else {
            super.onBackPressed();
        }
    }

    public void addNew(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = LayoutInflater.from(this).inflate(R.layout.dialog_task_modif, null);

        EditText name = (EditText) mView.findViewById(R.id.editTextTaskName);
        EditText poid = (EditText) mView.findViewById(R.id.editTextTaskPoid);
        EditText endDate = (EditText) mView.findViewById(R.id.editTextTaskEndDate);
        EditText description = (EditText) mView.findViewById(R.id.editTextDescription);

        ArrayList<String> priorityList = new ArrayList<>();
        priorityList.add("Could");
        priorityList.add("Can");
        priorityList.add("Should");
        Spinner priority = (Spinner) mView.findViewById(R.id.spinnerPriority);
        ArrayAdapter<String> adapterPriority = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,priorityList);
        adapterPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(adapterPriority);

        ArrayList<String> stateList = new ArrayList<>();
        stateList.add("ToDo");
        Spinner state = (Spinner) mView.findViewById(R.id.spinnerTaskState);
        ArrayAdapter<String> adapterState = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,stateList);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(adapterState);
        state.setSelection(0);

        Spinner personInCharge = (Spinner) mView.findViewById(R.id.spinnerPersonInCharge);
        ArrayList<TeamMember> teamMemberArrayList = db.getAllMembers();
        ArrayAdapter<TeamMember> adapterPersonInCharge = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,teamMemberArrayList);
        adapterPersonInCharge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personInCharge.setAdapter(adapterPersonInCharge);

        // clean des Edit Text
        name.clearComposingText();
        poid.clearComposingText();
        endDate.clearComposingText();
        description.clearComposingText();

        Button btnValidate = (Button) mView.findViewById(R.id.validateTasklModifButton);
        Button btnCancel = (Button) mView.findViewById(R.id.cancelTaskModifButton);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().isEmpty() && !poid.getText().toString().isEmpty() && !endDate.getText().toString().isEmpty()){

                    String nameNew = name.getText().toString();
                    String endDateNew  = endDate.getText().toString();
                    int poidNew = Integer.parseInt(poid.getText().toString());
                    String stateNew = state.getSelectedItem().toString();
                    String descriptionNew = description.getText().toString();
                    TeamMember personInChargeNew = (TeamMember) personInCharge.getSelectedItem();
                    String priorityNew = (String)priority.getSelectedItem();

                    Task newTask = new Task(nameNew,poidNew,(TeamMember)personInChargeNew, stateNew,endDateNew,descriptionNew,priorityNew);
                    db.insertNewTask(newTask);
                    FragmentManager monManager = getFragmentManager();
                    FragmentTransaction transaction = monManager.beginTransaction();
                    ToDoFragment toDoFragment = new ToDoFragment();
                    transaction.replace(R.id.fragmentDynamic,toDoFragment).addToBackStack(null);
                    transaction.commit();
                    alertDialog.dismiss();
                } else{
                    Toast.makeText(getApplicationContext(),"Il faut remplir les champs",Toast.LENGTH_SHORT);
                }
                alertDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.members) {
            Intent intent = new Intent(this,ActivityMembers.class);
            startActivity(intent);
        } else if (id == R.id.options) {
            Intent intent = new Intent(this,DataBaseOption.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}