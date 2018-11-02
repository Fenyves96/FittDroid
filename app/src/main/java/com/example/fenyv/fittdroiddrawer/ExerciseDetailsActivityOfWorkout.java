package com.example.fenyv.fittdroiddrawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fenyv.fittdroiddrawer.Entities.Exercise;
import com.example.fenyv.fittdroiddrawer.Entities.WorkoutExercise;
import com.example.fenyv.fittdroiddrawer.Fragments.ExerscisesFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ExerciseDetailsActivityOfWorkout extends AppCompatActivity {

    //region elemek
    private ImageView exImage;
    private TextView exTitle;
    private TextView exDescription;
    private TextView exMuscles;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);
        WorkoutExercise workoutExerciseToShow=(WorkoutExercise) getIntent().getSerializableExtra("workoutExercise");
        exImage=findViewById(R.id.exercise_image);
        exTitle=findViewById(R.id.title_exercise);
        exDescription=findViewById(R.id.desc_exercise);
        exDescription.setMovementMethod(new ScrollingMovementMethod());
        exMuscles=findViewById(R.id.exerciseMuscles);
        loadExercise(workoutExerciseToShow);
        //TODO: Exercise adatok lekérése név alapján

    }

    private void loadExercise(final WorkoutExercise workoutExerciseToShow) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("Exercises");
        reference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Exercise exercise;
                exercise=dataSnapshot.getValue(Exercise.class);
                if(exercise.getId()==(workoutExerciseToShow.getExerciseId())){
                    showExerciseDetails(exercise);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Exercise exercise;
                exercise=dataSnapshot.getValue(Exercise.class);
                if(exercise.getId()==(workoutExerciseToShow.getExerciseId())){
                    showExerciseDetails(exercise);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Exercise exercise;
                exercise=dataSnapshot.getValue(Exercise.class);
                if(exercise.getId()==(workoutExerciseToShow.getExerciseId())){
                    showExerciseDetails(exercise);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showExerciseDetails(Exercise exerciseToShow) {
        exTitle.setText(exerciseToShow.getName());
        exDescription.setText(exerciseToShow.getDescription());
        String muscle1=exerciseToShow.getMuscle1();
        String muscle2=exerciseToShow.getMuscle2();

        exMuscles.setText(String.format("%s: %s\n%s: %s  ",
                getResources().getString(R.string.muscle1),
                muscle1,
                !(muscle2.equals(""))? getResources().getString(R.string.muscle2):"",
                muscle2));
        new ImageLoadAsync().execute(exerciseToShow.getImageUrl());
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "asd", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    //Visszatérünk az előző menüponthoz.
    @Override
    public boolean navigateUpTo(Intent upIntent) {
        super.onBackPressed();
        return true;
    }

    private class ImageLoadAsync extends AsyncTask<String, Void, Bitmap> {

        ExerscisesFragment.ExerciseAdapter.ExerciseViewHolder holder;
        public ImageLoadAsync(){

        }
        @Override
        protected Bitmap doInBackground(String... params) {
            // your background code fetch InputStream
            try {
                URL url=new URL(params[0]);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Bitmap bmp) {
            if(bmp != null){
                super.onPostExecute(bmp);
                Drawable d = new BitmapDrawable(getResources(), bmp);
                exImage.setImageDrawable(d);
            }
        }

    }

}
