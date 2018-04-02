package com.example.fenyv.fittdroiddrawer;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by Belal on 18/09/16.
 */


public class body_menu extends Fragment {

    TextView calcTv;
    Button calcbtn;
    EditText etWeight;
    EditText etHeight;
    EditText etWaist;
    EditText etHip;
    EditText etNeck;
    RadioGroup radioGroup;
    FirebaseDatabase database;
    //DB handlerre itt szükség lesz

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_body, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

        getActivity().setTitle("Body");
        initialize(view);


       // ------------------------------------------------------------Firebase
        database=FirebaseDatabase.getInstance();

            loadEditTexts();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadEditTexts();
    }

    void initialize(View view){
        radioGroup=view.findViewById(R.id.genderRadioGroup);
        calcTv=view.findViewById(R.id.tvcalc);
        calcbtn=view.findViewById(R.id.calcbtn);
        etHeight=view.findViewById(R.id.etheight);
        etWeight=view.findViewById(R.id.etweight);
        etWaist=view.findViewById(R.id.etwaist);
        etHip=view.findViewById(R.id.ethip);
        etNeck=view.findViewById(R.id.etneck);
        etHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    Toast.makeText(getContext(), "asd", Toast.LENGTH_SHORT).show();
                    updateBodyInfo(etHeight);
                }
            }
        });


        etNeck.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    Toast.makeText(getContext(), "asd", Toast.LENGTH_SHORT).show();
                    updateBodyInfo(etNeck);
                }
            }
        });
        etHip.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    updateBodyInfo(etHip);
                }
            }
        });
        etWaist.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    Toast.makeText(getContext(), "asd", Toast.LENGTH_SHORT).show();
                    updateBodyInfo(etWaist);
                }
            }
        });

        etWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {

                    Toast.makeText(getContext(), "asd", Toast.LENGTH_SHORT).show();
                    updateBodyInfo(etWeight);
                }
        });
        etNeck.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    updateBodyInfo(etNeck);
                    getActivity().findViewById(R.id.bodyfragment).requestFocus();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    try {
                            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    }
                    catch (Exception e){e.printStackTrace();}
                    return true;
                }
                return false;
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    DatabaseReference myRef = database.getReference(SignInController.acc_id+"/Datas/gender");
                    switch (checkedRadioButton.getId()){
                        case R.id.rbfemale:
                            myRef.setValue("female");
                            break;
                        case R.id.rbmale:

                            myRef.setValue("male");
                            break;
                    }
                    // Changes the textview's text to "Checked: example radiobutton text"

                }
            }
        });



    }

    void updateBodyInfo(EditText editText) { //TODO: (1) Insert values to database
        try {
            if (!editText.getText().equals("") && !(editText.getText() == null)) {
                float value = Float.valueOf(editText.getText().toString());
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c);
                DatabaseReference myRef = database.getReference(SignInController.acc_id);
                myRef.child("Datas").child("lastUpdate").setValue(formattedDate);
                Toast.makeText(getContext(), "parapapapa", Toast.LENGTH_SHORT).show();
                switch (editText.getId()) {
                    case R.id.etheight:
                        myRef.child("Statistics").child(formattedDate).child("height").setValue(value);
                        break;
                    case R.id.etweight:
                        myRef.child("Statistics").child(formattedDate).child("weight").setValue(value);
                        break;
                    case R.id.etwaist:
                        myRef.child("Statistics").child(formattedDate).child("waist").setValue(value);
                        break;
                    case R.id.ethip:
                        myRef.child("Statistics").child(formattedDate).child("hip").setValue(value);
                        break;
                    case R.id.etneck:
                        myRef.child("Statistics").child(formattedDate).child("neck").setValue(value);
                        break;

                    default:
                        Toast.makeText(getContext(), "something happened", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    void loadEditTexts(){ //TODO: (2) Load values from database


            etHeight.setText("");
            etWeight.setText("");
            etWaist.setText("");
            etHip.setText("");
            etNeck.setText("");

       DatabaseReference myRef = database.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lastDate=null;
                try {
                    lastDate = dataSnapshot.child(SignInController.acc_id).child("Datas").child("lastUpdate").getValue().toString();
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.noLastUpdate, Toast.LENGTH_SHORT).show();
                }
                if(lastDate!=null) {
                    Toast.makeText(getContext(), lastDate, Toast.LENGTH_SHORT).show();
                    Float height = dataSnapshot.child(SignInController.acc_id).child("Statistics").child(lastDate).child("height").getValue(Float.class);
                    Float weight = dataSnapshot.child(SignInController.acc_id).child("Statistics").child(lastDate).child("weight").getValue(Float.class);
                    Float waist = dataSnapshot.child(SignInController.acc_id).child("Statistics").child(lastDate).child("waist").getValue(Float.class);
                    Float neck = dataSnapshot.child(SignInController.acc_id).child("Statistics").child(lastDate).child("neck").getValue(Float.class);
                    Float hip = dataSnapshot.child(SignInController.acc_id).child("Statistics").child(lastDate).child("hip").getValue(Float.class);
                    if (height != null)
                        etHeight.setText(fmt(height));
                    if (weight != null)
                        etWeight.setText(fmt(weight));
                    if (waist != null)
                        etWaist.setText(fmt(waist));
                    if (hip != null)
                        etHip.setText(fmt(hip));
                    if (neck != null)
                        etNeck.setText(fmt(neck));
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }




}