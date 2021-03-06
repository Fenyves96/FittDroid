package com.example.fenyv.fittdroiddrawer;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fenyv.fittdroiddrawer.Entities.Workout;
import com.example.fenyv.fittdroiddrawer.Fragments.BodyFragment;
import com.example.fenyv.fittdroiddrawer.Fragments.ExerscisesFragment;
import com.example.fenyv.fittdroiddrawer.Fragments.MyWorkoutsFragment;
import com.example.fenyv.fittdroiddrawer.Fragments.StatisticsFragment;
import com.example.fenyv.fittdroiddrawer.Fragments.WorkoutsFragment;
import com.example.fenyv.fittdroiddrawer.Interfaces.OnListFragmentInteractionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnListFragmentInteractionListener,View.OnClickListener
, AddNewWorkoutDialog.AddNewWorkoutListener{  //impelement onconnectionfailed

    boolean mainMenuOpened=false;
    NavigationView navigationView;

    //Google Sign In stuffs
    private SignInController signInController;


    private ImageView profilePicture;
    TextView nameTv;
    TextView emailTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set Layout Properties
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.keepSynced(true);
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
        signInController=new SignInController(this);

    }



    @Override
    public void onBackPressed() {
        int count =getSupportFragmentManager().getBackStackEntryCount();
        if(count==0) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return;
            }
            if (!mainMenuOpened) {
                displaySelectedScreen(R.id.nav_myworkouts);
                navigationView.getMenu().getItem(0).setChecked(true);
            } else {
                super.onBackPressed();
            }
        }
        else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return;
            }
             getSupportFragmentManager().popBackStack();

        }

    }

    //---------------------------------------------Navigation---------------------------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.myworkouts, menu);
        signInController=new SignInController(this);
        profilePicture = findViewById(R.id.profile_picture_imageView);
        emailTv=findViewById(R.id.emailTv);
        nameTv=findViewById(R.id.nameTv);
        signInController.initialize(profilePicture);
        emailTv.setText(signInController.getmEmailAdress());
        //TODO:database.getReference(signInController.get)
        nameTv.setText(signInController.getUsername());
        findViewById(R.id.emailTv).setOnClickListener(this);
        findViewById(R.id.nameTv).setOnClickListener(this);
        findViewById(R.id.profile_picture_imageView).setOnClickListener(this);
        Toast.makeText(this, signInController.getUserId(), Toast.LENGTH_SHORT).show();
        return true;
    }

    public void updateUserInfo(){
        emailTv.setText(signInController.getmEmailAdress());
        //TODO:database.getReference(signInController.get)
        nameTv.setText(signInController.getUsername());
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                fragment = new MyWorkoutsFragment();
                mainMenuOpened=true;
                break;
            case R.id.nav_workouts:
                fragment = new WorkoutsFragment();
                mainMenuOpened=false;
                break;
            case R.id.nav_statistics:
                fragment = new StatisticsFragment();
                mainMenuOpened=false;
                break;
            case R.id.nav_body:
                fragment = new BodyFragment();
                mainMenuOpened=false;
                break;
            case R.id.nav_exercises:
                mainMenuOpened=false;
                fragment=new ExerscisesFragment();
                break;

            case R.id.nav_logout:
                signInController.signOut();
                Toast.makeText(this, R.string.SignedOut, Toast.LENGTH_SHORT).show();
                break;


        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            //parapapapa
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    //---------------------------------------------End Of Navigation---------------------------------//




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nameTv:
                signInController.signOut();
                signInController.signIn();
                break;

            case R.id.emailTv:
                signInController.signOut();
                signInController.signIn();
                break;
            case R.id.profile_picture_imageView:
                signInController.signOut();
                signInController.signIn();
                break;
        }
    }

    //---------------------------------------------Google Sign In---------------------------------//


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        signInController.onResult(requestCode,data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        signInController.savePersonPhotoUrlSharedP();
    }

    @Override
    protected void onResume() {
        super.onResume();
        profilePicture = findViewById(R.id.profile_picture_imageView);
        signInController.resume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        signInController=new SignInController(this);
        //signInController.addAuthStateListener();
    }
    //----------------------------------------------------------------------------------------------------------------------Firebase
    @Override
    protected void onStop() {
        super.onStop();
        signInController.removeAuthStateListener();
    }

    @Override
    public void onListFragmentInteraction(Object item) {

    }

    @Override
    public void applyDatas(String workoutName) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        if(workoutName!=null)
        {

        }
        //Felveszünk egy új workoutot, aminek lesz egy neve és egy id-ja, majd megnyitjuk a workoutDetailsActivityt

            //Az adott ID-ű felhasználó, adott workoutjához hozzáadjuk a az adott gyakorlatot adott sets-el és repssel
            DatabaseReference myRef = database.getReference(SignInController.mUserId)
                    .child("MyWorkouts").child(workoutName);
            Workout workout=new Workout();
            workout.setId(getNextWorkoutId());
            workout.setName(workoutName);
            myRef.setValue(workout);
            Toast.makeText(this, R.string.added, Toast.LENGTH_SHORT).show();

    }

    int getNextWorkoutId(){
        SharedPreferences settings;
        settings = getSharedPreferences("nextWorkoutId", Context.MODE_PRIVATE);
        int id = settings.getInt("nextWorkoutId", 1);
        //set the sharedpref
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("nextWorkoutId", id+1);
        editor.apply();
        return id;
    }
}
