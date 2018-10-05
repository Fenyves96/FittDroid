package com.example.fenyv.fittdroiddrawer.Controllers;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fenyv.fittdroiddrawer.Fragments.BodyFragment;
import com.example.fenyv.fittdroiddrawer.R;
import com.example.fenyv.fittdroiddrawer.SignInController;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BodyController {

    public FirebaseDatabase database;
    public BodyController(){
        database=FirebaseDatabase.getInstance();
    }
    /**
     A paraméterben kapott editText-ben lévő adatot elmentjük az adatbázisban.
     */
    public void updateBodyInfo(EditText editText,Context context) { //TODO: (1) Insert values to database
        try {
            if (!editText.getText().equals("") && !(editText.getText() == null)) {
                float value = Float.valueOf(editText.getText().toString());
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c);
                DatabaseReference myRef = database.getReference(SignInController.mUserId);
                myRef.child("Datas").child("lastUpdate").setValue(formattedDate);
                Toast.makeText(context, "parapapapa", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "something happened", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void updateBodyInfoForAllElement(BodyFragment source,Context context){
        EditText etHeight=source.getActivity().findViewById(R.id.etheight);
        EditText etWeight=source.getActivity().findViewById(R.id.etweight);
        EditText etWaist=source.getActivity().findViewById(R.id.etwaist);
        EditText etHip=source.getActivity().findViewById(R.id.ethip);
        EditText etNeck=source.getActivity().findViewById(R.id.etneck);
        updateBodyInfo(etHeight,context);
        updateBodyInfo(etWeight,context);
        updateBodyInfo(etHip,context);
        updateBodyInfo(etWaist,context);
        updateBodyInfo(etNeck,context);
    }

}
