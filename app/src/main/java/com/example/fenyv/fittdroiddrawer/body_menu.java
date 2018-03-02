package com.example.fenyv.fittdroiddrawer;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    //DB handlerre itt szükség lesz
    DBHandler dbHandler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_body, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        dbHandler=new DBHandler(getActivity().getBaseContext()); //initialize dbHandler
        getActivity().setTitle("Body");
        calcTv=view.findViewById(R.id.tvcalc);
        calcbtn=view.findViewById(R.id.calcbtn);
        etHeight=getActivity().findViewById(R.id.etheight);
        etWeight=getActivity().findViewById(R.id.etweight);
        etWaist=getActivity().findViewById(R.id.etwaist);
        etHip=getActivity().findViewById(R.id.ethip);
        etNeck=getActivity().findViewById(R.id.etneck);
        calcbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float height=Float.valueOf(etHeight.getText().toString());
                float weight=Float.valueOf(etWeight.getText().toString());
                float waist=Float.valueOf(etWaist.getText().toString());
                float hip=Float.valueOf(etHip.getText().toString());
                float neck=Float.valueOf(etNeck.getText().toString());
                calcTv.setText(Float.toString(height)+" "+Float.toString(waist));
                boolean inserted=dbHandler.insertData(height,weight,waist,hip,neck);
                if(inserted){
                    calcTv.setText("yeah");
                }else {calcTv.setText("nooo");}
            }
        });
        loadEditTexts();
    }

    void loadEditTexts(){
        Cursor cursor= dbHandler.getAllData();
        if(cursor.getCount()==0){
            return;
        }
        if(cursor.moveToFirst()) {
            float height = cursor.getFloat(cursor.getColumnIndex(DBHandler.COL_HEIGHT));
            float weight = cursor.getFloat(cursor.getColumnIndex("Weight"));
            float waist = cursor.getFloat(cursor.getColumnIndex("Waist"));
            float hip = cursor.getFloat(cursor.getColumnIndex("Hip"));
            float neck = cursor.getFloat(cursor.getColumnIndex("Neck"));


            etHeight.setText(Float.toString(height));
            etWeight.setText(Float.toString(weight));
            etWaist.setText(Float.toString(waist));
            etHip.setText(Float.toString(hip));
            etNeck.setText(Float.toString(neck));
        }
    }


}