package com.example.fenyv.fittdroiddrawer;


import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fenyv.fittdroiddrawer.dummy.DummyContent;
import com.example.fenyv.fittdroiddrawer.dummy.DummyContent2;
import com.example.fenyv.fittdroiddrawer.dummy.DummyContent3;

import java.util.ArrayList;
import java.util.List;

public class Main_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        ,myworkoutsFragment.OnListFragmentInteractionListener,WorkoutsFragment.OnListFragmentInteractionListener
        ,ExerscisesFragment.OnListFragmentInteractionListener{

    boolean mainMenuOpened=false;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myworkouts_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        displaySelectedScreen(R.id.nav_myworkouts);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if(!mainMenuOpened){
            displaySelectedScreen(R.id.nav_myworkouts);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.myworkouts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//
//        } else if (id == R.id.nav_share) {
//
//        }
        displaySelectedScreen(item.getItemId());

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_myworkouts:
                fragment = new myworkoutsFragment();
                mainMenuOpened=true;
                break;
            case R.id.nav_workouts:
                fragment = new WorkoutsFragment();
                mainMenuOpened=false;
                break;
            case R.id.nav_statistics:
                fragment = new statistics_menu();
                mainMenuOpened=false;
                break;
            case R.id.nav_body:
                fragment = new body_menu();
                mainMenuOpened=false;
                break;
            case R.id.nav_exercises:
                mainMenuOpened=false;
                fragment=new ExerscisesFragment();
                break;



        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent2.DummyItem item) {

    }
    @Override
    public void onListFragmentInteraction(DummyContent3.DummyItem item) {

    }
}
