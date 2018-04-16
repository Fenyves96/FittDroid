package com.example.fenyv.fittdroiddrawer;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by fenyv on 2018. 04. 09..
 */

public class FittDroid extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
