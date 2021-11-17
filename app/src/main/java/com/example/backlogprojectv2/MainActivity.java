package com.example.backlogprojectv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
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
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu) ;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}