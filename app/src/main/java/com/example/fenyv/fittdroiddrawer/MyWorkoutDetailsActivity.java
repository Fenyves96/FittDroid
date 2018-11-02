package com.example.fenyv.fittdroiddrawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fenyv.fittdroiddrawer.Entities.Workout;
import com.example.fenyv.fittdroiddrawer.Entities.WorkoutExercise;
import com.google.android.gms.common.util.Strings;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyWorkoutDetailsActivity extends AppCompatActivity implements SetsAndRepsDialogForActivity.SetAndRepsDialogListenerForActivity {

    private ListView myWorkoutList;
    ArrayList<WorkoutExercise>exercises;
    ArrayList<String>keys;
    WorkoutExerciseAdapter adapter;
    DatabaseReference reference;
    FirebaseDatabase database;
    public static Workout workout;
    FloatingActionButton fabAdd;
    private String actualExerciseName;


    public void setActualExerciseName(String actualExerciseName) {
        this.actualExerciseName = actualExerciseName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_workout_details);
        initalizeComponents();
        setTitle(workout.getName());


    }

    private void initalizeComponents() {
        exercises=new ArrayList<>();

        myWorkoutList=findViewById(R.id.myWorkoutList);
        workout=(Workout) getIntent().getSerializableExtra("workout");
        //Toast.makeText(getApplicationContext(),"workout name: "+workout.getName(),Toast.LENGTH_SHORT).show();

        //Workout Exercise-ok összegyűjtése a workouton belül (
        exercises=new ArrayList<>();
        database=FirebaseDatabase.getInstance();
        adapter=new WorkoutExerciseAdapter(exercises,getApplicationContext());
        adapter.setActivity(this);
        myWorkoutList.setAdapter(adapter);
        fabAdd=findViewById(R.id.fabAddExercise);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExercises();
            }
        });
        keys=new ArrayList<>();
        getDataFromDataBase();
    }

    private void addExercises() {
        Intent myIntent = new Intent(MyWorkoutDetailsActivity.this, ExercisesForAddingActivity.class);
        myIntent.putExtra("workout",workout);
        startActivity(myIntent);
    }

    private void getDataFromDataBase() {
        reference=database.getReference(SignInController.mUserId+"/MyWorkouts/"+workout.getName()+"/WorkoutExercises");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                WorkoutExercise exercise;
                exercise=dataSnapshot.getValue(WorkoutExercise.class);
                String key= dataSnapshot.getKey();
                keys.add(key);
                exercises.add(exercise);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                WorkoutExercise exercise;
                exercise=dataSnapshot.getValue(WorkoutExercise.class);
                String key= dataSnapshot.getKey();
                int index = keys.indexOf(key);
                exercises.set(index,exercise);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                WorkoutExercise exercise;
                exercise=dataSnapshot.getValue(WorkoutExercise.class);
                String key= dataSnapshot.getKey();
                int index = keys.indexOf(key);
                exercises.remove(index);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Visszatérünk az előző menüponthoz.
    @Override
    public boolean navigateUpTo(Intent upIntent) {
        super.onBackPressed();
        return true;
    }
    @Override
    public void applyDatas(int sets, int reps) {
        DatabaseReference myRef = database.getReference(SignInController.mUserId)
                .child("MyWorkouts").child(workout.getName())
                .child("WorkoutExercises").child(actualExerciseName);
        myRef.child("set").setValue(sets);
        myRef.child("rep").setValue(reps);
    }
}

class WorkoutExerciseAdapter extends ArrayAdapter<WorkoutExercise> implements View.OnClickListener{

    private ArrayList<WorkoutExercise>dataSet;

    public void setActivity(MyWorkoutDetailsActivity activity) {
        this.activity = activity;
    }

    MyWorkoutDetailsActivity activity;
    Context mContext;
    private static class ViewHolder {
        TextView exerciseNameTV;
        TextView setAndRepsTV;
    }

    WorkoutExerciseAdapter(ArrayList<WorkoutExercise> data,Context context){
        super(context,R.layout.content_my_workout_details,data);
        this.dataSet=data;
        this.mContext=context;
    }
    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        WorkoutExercise workoutExercise=(WorkoutExercise) object;
        Toast.makeText(mContext, workoutExercise.getExerciseName(), Toast.LENGTH_SHORT).show();

        //TODO: Exercise megnyitása
        Intent myIntent = new Intent(getContext(), ExerciseDetailsActivityOfWorkout.class);
        myIntent.putExtra("workoutExercise",workoutExercise);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(myIntent);
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final WorkoutExercise workoutExercise = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.content_my_workout_details, parent, false);
            viewHolder.exerciseNameTV = (TextView) convertView.findViewById(R.id.workoutDetailsActivityExerciseName);
            viewHolder.setAndRepsTV = (TextView) convertView.findViewById(R.id.workoutDetailsActivitySetsAndReps);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.exerciseNameTV.setText(workoutExercise.getExerciseName());
        viewHolder.setAndRepsTV.setText(workoutExercise.getSet()+" X "+workoutExercise.getRep());
        viewHolder.exerciseNameTV.setOnClickListener(this);
        viewHolder.exerciseNameTV.setTag(position);

        result.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(R.string.delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference reference=database.getReference(SignInController.mUserId+"/MyWorkouts/"+MyWorkoutDetailsActivity.workout.getName()+"/WorkoutExercises/"+workoutExercise.getExerciseName());
                        reference.removeValue();
                        return true;
                    }
                });
                menu.add(R.string.modify).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Dialog meghívása
                        activity.setActualExerciseName(viewHolder.exerciseNameTV.getText().toString());
                        openDialog();
                        return true;
                    }
                });
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }
    private void openDialog() {
        SetsAndRepsDialogForActivity dialog =new SetsAndRepsDialogForActivity();
        dialog.show(activity.getFragmentManager(),"example");
    }
}
