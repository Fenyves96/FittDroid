package com.example.fenyv.fittdroiddrawer.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fenyv.fittdroiddrawer.DBHandler;
import com.example.fenyv.fittdroiddrawer.R;

/**
 * Created by Belal on 18/09/16.
 */


public class StatisticsFragment extends Fragment {
    DBHandler dbHandler;
    TextView statisticstv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHandler=new DBHandler(getActivity().getBaseContext()); //initialize dbHandler
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Statistics");
        statisticstv=view.findViewById(R.id.statisticstv);
        statisticstv.setText("");
        getAllBodyInformation();
    }

    void getAllBodyInformation() {
        Cursor cursor = dbHandler.getAllBodyInformation();
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
                float height = cursor.getFloat(cursor.getColumnIndex(DBHandler.COL_HEIGHT));
                String date = cursor.getString(cursor.getColumnIndex("Date"));
                float weight = cursor.getFloat(cursor.getColumnIndex("Weight"));
                float waist = cursor.getFloat(cursor.getColumnIndex("Waist"));
                float hip = cursor.getFloat(cursor.getColumnIndex("Hip"));
                float neck = cursor.getFloat(cursor.getColumnIndex("Neck"));

                statisticstv.append(date + " ");
                statisticstv.append(Float.toString(height) + " ");
                statisticstv.append(Float.toString(weight) + " ");
                statisticstv.append(Float.toString(waist) + " ");
                statisticstv.append(Float.toString(hip) + " ");
                statisticstv.append(Float.toString(neck) + " ");
                statisticstv.append("\n");

        }
    }


}