package com.example.fenyv.fittdroiddrawer.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fenyv.fittdroiddrawer.Controllers.BodyController;
import com.example.fenyv.fittdroiddrawer.R;
import com.example.fenyv.fittdroiddrawer.SignInController;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Belal on 18/09/16.
 */


public class BodyFragment extends Fragment {

    TextView calcTv;
    Button calcbtn;
    EditText etWeight;
    EditText etHeight;
    EditText etWaist;
    EditText etHip;
    EditText etNeck;
    RadioGroup radioGroup;
    BodyController bodyController;
    //DB handlerre itt szükség lesz

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_body, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Title beállítása
        getActivity().setTitle("Body");
        initialize(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEditTexts();
    }

    BodyFragment getFragment(){
        return this;
    }

    /**Body_menu-t inicializáló függvény, itt állítjuk be az egyes mezőket, illetve listenereket aggatunk rájuk.*/
    void initialize(View view){
        // Firebase handler
        bodyController=new BodyController();
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
                    bodyController.updateBodyInfo(etHeight,getContext());
                }
            }
        });


        etNeck.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    Toast.makeText(getContext(), "asd", Toast.LENGTH_SHORT).show();
                    bodyController.updateBodyInfoForAllElement(getFragment(),getContext());
                }
            }
        });
        etHip.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    bodyController.updateBodyInfoForAllElement(getFragment(),getContext());
                }
            }
        });
        etWaist.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    Toast.makeText(getContext(), "asd", Toast.LENGTH_SHORT).show();
                    bodyController.updateBodyInfoForAllElement(getFragment(),getContext());
                }
            }
        });

        etWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {

                    Toast.makeText(getContext(), "asd", Toast.LENGTH_SHORT).show();
                bodyController.updateBodyInfoForAllElement(getFragment(),getContext());
                }
        });
        etNeck.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    bodyController.updateBodyInfoForAllElement(getFragment(),getContext());
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
                    DatabaseReference myRef = bodyController.database.getReference(SignInController.mUserId +"/Datas/gender");
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
        loadEditTexts();

        calcbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodyController.Calculate(getFragment(),getContext());
            }
        });
    }


    /**
    Adatok betöltése az egyes mezőkbe.
     */
    void loadEditTexts(){ //TODO: (2) Load values from database
            etHeight.setText("");
            etWeight.setText("");
            etWaist.setText("");
            etHip.setText("");
            etNeck.setText("");

       DatabaseReference myRef = bodyController.database.getReference();
       myRef.keepSynced(true);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lastDate=null;
                try {
                    lastDate = dataSnapshot.child(SignInController.mUserId).child("Datas").child("lastUpdate").getValue().toString();
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.noLastUpdate, Toast.LENGTH_SHORT).show();
                }
                if(lastDate!=null) {
                    Toast.makeText(getContext(), lastDate, Toast.LENGTH_SHORT).show();
                    Float height = dataSnapshot.child(SignInController.mUserId).child("Statistics").child(lastDate).child("height").getValue(Float.class);
                    Float weight = dataSnapshot.child(SignInController.mUserId).child("Statistics").child(lastDate).child("weight").getValue(Float.class);
                    Float waist = dataSnapshot.child(SignInController.mUserId).child("Statistics").child(lastDate).child("waist").getValue(Float.class);
                    Float neck = dataSnapshot.child(SignInController.mUserId).child("Statistics").child(lastDate).child("neck").getValue(Float.class);
                    Float hip = dataSnapshot.child(SignInController.mUserId).child("Statistics").child(lastDate).child("hip").getValue(Float.class);
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