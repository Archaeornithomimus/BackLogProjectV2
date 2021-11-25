package com.example.backlogprojectv2;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Member;
import java.util.ArrayList;

public class ActivityMembers extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayAdapter monAdaptateur;
    private ArrayList<TeamMember> membersList;
    DatabaseHandler db = null;
    ListView listView;

    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        listView = findViewById(android.R.id.list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseHandler(this);
        printListMembers();
    }

    public void addNew(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = LayoutInflater.from(this).inflate(R.layout.dialog_custom_members, null);
        Button btnValidate = (Button) mView.findViewById(R.id.validate_button);
        Button btnCancel = (Button) mView.findViewById(R.id.cancel_button);
        EditText name = (EditText) mView.findViewById(R.id.editTextPersonName);
        EditText firstname = (EditText) mView.findViewById(R.id.editTextPersonFirstname);
        FloatingActionButton deleteButton = mView.findViewById(R.id.floatingActionButtonDelete);

        name.clearComposingText();
        firstname.clearComposingText();

        deleteButton.hide();
        deleteButton.setActivated(false);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().isEmpty() && !firstname.getText().toString().isEmpty()){
                    TeamMember teamMember = new TeamMember(name.getText().toString(),firstname.getText().toString());
                    db.insertNewTeamMember(teamMember);
                    membersList.add(teamMember);
                    monAdaptateur.notifyDataSetChanged();
                    alertDialog.dismiss();
                } else{
                    Toast.makeText(getApplicationContext(),"Il faut remplir les champs",Toast.LENGTH_SHORT);
                }

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

    public void modifTeamMember(View view,TeamMember teamMember) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = LayoutInflater.from(this).inflate(R.layout.dialog_custom_members, null);
        Button btnValidate = (Button) mView.findViewById(R.id.validate_button);
        Button btnCancel = (Button) mView.findViewById(R.id.cancel_button);
        EditText name = (EditText) mView.findViewById(R.id.editTextPersonName);
        EditText firstname = (EditText) mView.findViewById(R.id.editTextPersonFirstname);
        FloatingActionButton deleteButton = mView.findViewById(R.id.floatingActionButtonDelete);

        //name.clearComposingText();
        firstname.setText(teamMember.getFirstname());
        name.setText(teamMember.getName());
        deleteButton.setActivated(true);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteMember(teamMember);
                membersList.remove(teamMember);
                monAdaptateur.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void printListMembers(){
        membersList = new ArrayList<>();
        membersList = db.getAllMembers();
        this.monAdaptateur = new MemberArrayAdapter(
                this, R.layout.team_member_list, membersList);
        listView.setAdapter(this.monAdaptateur);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TeamMember teamMember = (TeamMember) parent.getItemAtPosition(position);
                modifTeamMember(view,teamMember);
                Toast.makeText(getApplicationContext(), "selected Item Name is " + String.valueOf(teamMember.getId()), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent intent = new Intent(this,MainActivity.class);
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