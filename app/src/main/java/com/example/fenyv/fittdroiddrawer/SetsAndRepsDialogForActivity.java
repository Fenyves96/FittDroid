package com.example.fenyv.fittdroiddrawer;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import  android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.fenyv.fittdroiddrawer.R;

public class SetsAndRepsDialogForActivity extends DialogFragment {
    private EditText editTextSets;
    private EditText editTextReps;
    private SetAndRepsDialogListenerForActivity listener;

    @Override
    public void onAttach(Context context) {
        try {
            listener=(SetAndRepsDialogListenerForActivity)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement SetAndRepsDialogListener");
        }
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_setandreps_dialog,null);

        editTextSets=view.findViewById(R.id.layout_setandreps_dialog_setsedittext);
        editTextReps=view.findViewById(R.id.layout_setandreps_dialog_repsedittext);
        builder.setView(view)
                .setTitle(getContext().getString(R.string.setandreps))
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int sets=Integer.parseInt(editTextSets.getText().toString());
                int reps=Integer.parseInt(editTextReps.getText().toString());
                listener.applyDatas(sets,reps);
            }
        });

        return builder.create();
    }


    public interface SetAndRepsDialogListenerForActivity{
        void applyDatas(int sets,int reps);
    }
}

