package com.example.fenyv.fittdroiddrawer;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.fenyv.fittdroiddrawer.dummy.DummyContent;
import com.example.fenyv.fittdroiddrawer.dummy.DummyContent2;
import com.example.fenyv.fittdroiddrawer.dummy.DummyContent3;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class Main_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        ,myworkoutsFragment.OnListFragmentInteractionListener,WorkoutsFragment.OnListFragmentInteractionListener
        ,ExerscisesFragment.OnListFragmentInteractionListener,View.OnClickListener{

    boolean mainMenuOpened=false;
    NavigationView navigationView;

    //Google Sign In stuffs
    private static int RC_SIGN_IN = 1;
    private static String TAG = "message";

    public String getPersonPhotoUrl() {
        SharedPreferences settings;
        settings = getSharedPreferences("savephoto", Context.MODE_PRIVATE);

        //get the sharepref
        personPhotoUrl = settings.getString("photoUri", "empty");
        return personPhotoUrl;
    }

    public void setPersonPhotoUrl(String personPhotoUrl) {
        SharedPreferences settings;
        settings = getSharedPreferences("savephoto", Context.MODE_PRIVATE);
        //set the sharedpref
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("photoUri", personPhotoUrl);
        editor.apply();
    }

    String personPhotoUrl;
    GoogleSignInClient mGoogleSignInClient;
    private ImageView profilePicture;
    TextView nameTv;
    TextView emailTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set Layout Properties
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

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        // Build a GoogleSignInClient with the options specified by gso.
        //

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

    //---------------------------------------------Navigation---------------------------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        loadPersonPhotoUrlSharedP();
        getMenuInflater().inflate(R.menu.myworkouts, menu);
        nameTv=findViewById(R.id.nameTv);
        emailTv=findViewById(R.id.emailTv);
        profilePicture = findViewById(R.id.profile_picture_imageView);
        findViewById(R.id.emailTv).setOnClickListener(this);
        findViewById(R.id.nameTv).setOnClickListener(this);
        findViewById(R.id.profile_picture_imageView).setOnClickListener(this);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        updateUI(account);
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

            case R.id.nav_logout:
                signOut();
                Toast.makeText(this, R.string.SignedOut, Toast.LENGTH_SHORT).show();
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

    //---------------------------------------------End Of Navigation---------------------------------//
    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent2.DummyItem item) {

    }
    @Override
    public void onListFragmentInteraction(DummyContent3.DummyItem item) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nameTv:
                signOut();
                signIn();
                break;

            case R.id.emailTv:
                signOut();
                signIn();
                break;
            case R.id.profile_picture_imageView:
                signOut();
                signIn();
                break;
        }
    }

    //---------------------------------------------Google Sign In---------------------------------//
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        try {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
            updateUI(null);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, R.string.unsuccesfullSignOut, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    void insertProfilePicture(String url){
        if(url.equals("empty")){
            profilePicture.setImageResource(R.mipmap.ic_launcher_round);

        }else {

            Glide.with(getApplicationContext()).load(url).asBitmap().fitCenter().into(new BitmapImageViewTarget(profilePicture) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    profilePicture.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            savePersonPhotoUrlSharedP();
            // Signed in successfully, show authenticated UI.
            try {
                setPersonPhotoUrl(account.getPhotoUrl().toString());
                insertProfilePicture(getPersonPhotoUrl());
            } catch (Exception e) {
                setPersonPhotoUrl("empty");
            }

            updateUI(account);


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            profilePicture.setImageResource(R.mipmap.ic_launcher_round);
        }


        savePersonPhotoUrlSharedP();
    }




    void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            nameTv.setText("Nincs bejelntkezve");
            emailTv.setText("");
            profilePicture.setImageResource(R.mipmap.ic_launcher_round);

        } else {
            insertProfilePicture(getPersonPhotoUrl());
            emailTv.setText(account.getEmail());
            nameTv.setText(account.getDisplayName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePersonPhotoUrlSharedP();
    }


    void savePersonPhotoUrlSharedP(){
        SharedPreferences settings;
        settings = getSharedPreferences("savephoto", Context.MODE_PRIVATE);
        //set the sharedpref
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("photoUri", personPhotoUrl);
        editor.apply();
    }

    void loadPersonPhotoUrlSharedP(){
        SharedPreferences settings;
        settings = getSharedPreferences("savephoto", Context.MODE_PRIVATE);

        //get the sharepref
        personPhotoUrl = settings.getString("photoUri", "empty");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPersonPhotoUrlSharedP();
        if(personPhotoUrl.equals("empty"))
            return;
        try {
           insertProfilePicture(personPhotoUrl);
        } catch (Exception e) {
            if (e instanceof java.lang.NullPointerException) {
                e.printStackTrace();
            } else e.printStackTrace();
        }
    }





}
