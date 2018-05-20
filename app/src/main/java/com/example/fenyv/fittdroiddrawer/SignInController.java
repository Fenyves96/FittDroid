package com.example.fenyv.fittdroiddrawer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * Created by fenyv on 2018. 03. 30..
 */

public class SignInController  implements GoogleApiClient.OnConnectionFailedListener{
    private static String TAG = "MAIN:ACTIVITY";
    private String personPhotoUrl;
    GoogleApiClient mgGoogleApiClient;
    GoogleSignInClient mGoogleSignInClient;
    ImageView profilePicture;

    public String getAcc_name() {
        return acc_name;
    }

    String acc_name;

    public String getAcc_id() {
        return acc_id;
    }

    public static String acc_id;

    public String getAcc_email() {
        return acc_email;
    }

    String acc_email;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static int getRcSignIn() {
        return RC_SIGN_IN;
    }

    public void addAuthStateListener(){
        mAuth=FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void removeAuthStateListener(){
        if(mAuthListener !=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private static int RC_SIGN_IN = 0;
    AppCompatActivity activity;

    public SignInController(AppCompatActivity activity){
        this.activity=activity;
        loadUserIDSharedP();
        loadPersonPhotoUrlSharedP();
    }


    public String getPersonPhotoUrl() {
        SharedPreferences settings;
        settings =  activity.getSharedPreferences("savephoto", Context.MODE_PRIVATE);

        //get the sharepref
        personPhotoUrl = settings.getString("photoUri", "empty");
        return personPhotoUrl;
    }

    public void setPersonPhotoUrl(String personPhotoUrl) {
        SharedPreferences settings;
        settings = activity.getSharedPreferences("savephoto", Context.MODE_PRIVATE);
        //set the sharedpref
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("photoUri", personPhotoUrl);
        editor.apply();
    }

    public void signIn() {
        //Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //----------------------------------------------------------------------------------------------------------------------Firebase
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mgGoogleApiClient);
        activity.startActivityForResult(signInIntent,SignInController.getRcSignIn());
    }

    public void initialize(ImageView profilePicture){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(activity.getString(R.string.default_web_client))
                .requestEmail()
                .build(); //itt csak a request id az újdonság
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        mgGoogleApiClient = new GoogleApiClient.Builder(activity).enableAutoManage(activity, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        mAuth=FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("AUTH", "User logged in: " + user.getEmail());
                }
                else
                    Log.d("AUTH", "User logged out: " );

            }
        };
        this.profilePicture=profilePicture;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);

        updateUI(account);

    }

    public void signOut() {
        try {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
            updateUI(null);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity, R.string.unsuccesfullSignOut, Toast.LENGTH_SHORT).show();
        }



        //----------------------------------------------------------------------------------------------------------------------Firebase
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseDatabase database= FirebaseDatabase.getInstance();
                setUserIDSharedP(account.getId());
                DatabaseReference ref=database.getReference(account.getId()+"/Datas");
                ref.child("email").setValue(account.getEmail());
                ref.child("name").setValue(account.getDisplayName());
                Log.d("AUTH","SignInTithCredintalComplete"+task.isSuccessful());
            }
        });
    }

    void insertProfilePicture(String url){
        personPhotoUrl =url;
        if(url.equals("empty")){
            profilePicture.setImageResource(R.mipmap.ic_launcher_round);

        }else {

            Glide.with(activity.getApplicationContext()).load(url).asBitmap().fitCenter().into(new BitmapImageViewTarget(profilePicture) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(activity.getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    profilePicture.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
    }

    void onResult(int requestCode, Intent data){
        if (requestCode == SignInController.getRcSignIn()) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            //----------------------------------------------------------------------------------------------------------------------Firebase
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account=result.getSignInAccount();
                assert account != null;
                firebaseAuthWithGoogle(account);
            }else
                Toast.makeText(activity, "Google login failed", Toast.LENGTH_SHORT).show();

            handleSignInResult(task);
        }
    }

    void resume(){
        if(personPhotoUrl.equals("empty") &&personPhotoUrl==null)
            return;
        try {
            //insertProfilePicture(personPhotoUrl);
        } catch (Exception e) {
            if (e instanceof java.lang.NullPointerException) {
                e.printStackTrace();
            } else e.printStackTrace();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            savePersonPhotoUrlSharedP();
            saveUserIDSharedP();
            // Signed in successfully, show authenticated UI.
            try {
                setUserIDSharedP(account.getId());
                setPersonPhotoUrl(account.getId());
                setPersonPhotoUrl(Objects.requireNonNull(account.getPhotoUrl()).toString());
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
        saveUserIDSharedP();
        savePersonPhotoUrlSharedP();
    }

    void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            acc_name ="Signed out";
            acc_email ="";
            profilePicture.setImageResource(R.mipmap.ic_launcher_round);

        } else {
            insertProfilePicture(getPersonPhotoUrl());
            acc_email =account.getEmail();
            Toast.makeText(activity, acc_email, Toast.LENGTH_SHORT).show();
            acc_name =account.getDisplayName();
            ((Main_Activity)activity).updateUserInfo();
            setUserIDSharedP(account.getId());
        }
    }
    void setUserIDSharedP(String userID){
        acc_id=userID;
        SharedPreferences settings;
        settings = activity.getSharedPreferences("saveuserID", Context.MODE_PRIVATE);
        //set the sharedpref
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userID", acc_id);
        editor.apply();
    }

    void saveUserIDSharedP(){
        SharedPreferences settings;
        settings = activity.getSharedPreferences("saveuserID", Context.MODE_PRIVATE);
        //set the sharedpref
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userID", acc_id);
        editor.apply();
    }

    void loadUserIDSharedP(){
        SharedPreferences settings;
        settings = activity.getSharedPreferences("saveuserID", Context.MODE_PRIVATE);

        //get the sharepref
        acc_id = settings.getString("userID", "empty");
    }


    void savePersonPhotoUrlSharedP(){
        SharedPreferences settings;
        settings = activity.getSharedPreferences("savephoto", Context.MODE_PRIVATE);
        //set the sharedpref
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("photoUri", personPhotoUrl);
        editor.apply();
    }

    void loadPersonPhotoUrlSharedP(){
        SharedPreferences settings;
        settings = activity.getSharedPreferences("savephoto", Context.MODE_PRIVATE);

        //get the sharepref
        personPhotoUrl = settings.getString("photoUri", "empty");
    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
