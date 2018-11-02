package com.example.fenyv.fittdroiddrawer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.app.AlertDialog;

public class AddNewWorkoutDialog extends AppCompatDialogFragment {

    private EditText editTextWorkoutName;
    private AddNewWorkoutListener listener;

    @Override
    public void onAttach(Context context) {
        try {
            listener=(AddNewWorkoutListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement SetAndRepsDialogListener");
        }
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_new_workout,null);

        editTextWorkoutName =view.findViewById(R.id.layout_add_new_workout_editText);
        builder.setView(view)
                .setTitle(getContext().getString(R.string.newWorkoutName))
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String workoutName= editTextWorkoutName.getText().toString();
                listener.applyDatas(workoutName);
            }
        });

        return builder.create();
    }

    public interface AddNewWorkoutListener{
        void applyDatas(String WorkoutName);
    }
}