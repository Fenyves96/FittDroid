package com.example.fenyv.fittdroiddrawer.Controllers;

import android.content.Context;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

    public void Calculate(BodyFragment source,Context context) {
        EditText etHeight=source.getActivity().findViewById(R.id.etheight);
        EditText etWeight=source.getActivity().findViewById(R.id.etweight);
        EditText etWaist=source.getActivity().findViewById(R.id.etwaist);
        EditText etHip=source.getActivity().findViewById(R.id.ethip);
        EditText etNeck=source.getActivity().findViewById(R.id.etneck);
        TextView resultTv=source.getActivity().findViewById(R.id.tvcalc);

        float height=Float.valueOf(etHeight.getText().toString());
        float weight=Float.valueOf(etWeight.getText().toString());
        float waist=Float.valueOf(etWaist.getText().toString());
        float hip=Float.valueOf(etHip.getText().toString());
        float neck=Float.valueOf(etNeck.getText().toString());

        RadioGroup group=source.getActivity().findViewById(R.id.genderRadioGroup);
        if(group.getCheckedRadioButtonId()==-1){
            Toast.makeText(context, "Please choose your gender", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            int selectedId = group.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            RadioButton selectedRadioButton = (RadioButton) source.getActivity().findViewById(selectedId);
            double result=0.0;
            if(selectedRadioButton.getText().toString().equals("male")){
                result=495/(1.0324 - 0.19077*java.lang.Math.log10(waist-neck) + 0.15456*java.lang.Math.log10(height))-450;
            }
            else{
                result=495/(1.29579  - 0.35004*java.lang.Math.log10(waist-neck) + 0.22100*java.lang.Math.log10(height))-450;
            }
            resultTv.setText(String.format("BodyFat: %.2f "+"%%\n " +
                    "Body Fat Mass: %.2f kg\n "+
                    "Lean Body Mass: %.2f kg\n ",result,result/100*weight,(100-result)/100*weight));
        }





    }
}
