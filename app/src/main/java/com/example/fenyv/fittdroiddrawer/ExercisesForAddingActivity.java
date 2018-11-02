package com.example.fenyv.fittdroiddrawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.example.fenyv.fittdroiddrawer.Entities.Exercise;
import com.example.fenyv.fittdroiddrawer.Entities.Workout;
import com.example.fenyv.fittdroiddrawer.Entities.WorkoutExercise;
import com.example.fenyv.fittdroiddrawer.Fragments.ExerscisesFragment;
import com.example.fenyv.fittdroiddrawer.Interfaces.OnListFragmentInteractionListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ExercisesForAddingActivity extends AppCompatActivity implements SetsAndRepsDialog.SetAndRepsDialogListener {
    List<Exercise> exercises;
    ExercisesForAddingActivity.ExerciseAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    RecyclerView recyclerView;
    Workout workout;
    private Exercise actExercise;

    ExercisesForAddingActivity.ExerciseAdapter.ExerciseViewHolder exerciseholder;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1; //hány oszlopban jelenjenek meg a gyakorlatok
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExercisesForAddingActivity() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ExerscisesFragment newInstance(int columnCount) {
        ExerscisesFragment fragment = new ExerscisesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_exercises_for_adding_items);
        mColumnCount = 1;

            recyclerView = (RecyclerView) findViewById(R.id.fragment_exercises_for_adding_item_recycle_view);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), mColumnCount));
            }
            exercises=new ArrayList<>();
            adapter=new ExercisesForAddingActivity.ExerciseAdapter(exercises, mListener);

        database=FirebaseDatabase.getInstance();
        getDataFromDataBase();
        this.setTitle("Exercises");
        workout=(Workout) getIntent().getSerializableExtra("workout");
    }
    //Visszatérünk az előző menüponthoz.
    @Override
    public boolean navigateUpTo(Intent upIntent) {
        super.onBackPressed();
        return true;
    }
    /**
     * Összeszedjük az összes gyakorlatot az adatbázisból és itt kezeljük le,
     * hogy mi történjen, ha bármiféle módosítás történne az adatbázisban.
     */
    void getDataFromDataBase() {
//        Map<String, Exercise> exerciseMap = new HashMap<>();
//        exerciseMap.put("bench press3",new Exercise("benchpress","description","biceps"));
        reference = database.getReference("Exercises");
//        reference.setValue(exerciseMap);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Exercise exercise;
                exercise = dataSnapshot.getValue(Exercise.class);
                if (exercise != null)
//                    Toast.makeText(getContext(), exercise.toString(), Toast.LENGTH_SHORT).show();
                    exercises.add(exercise);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Exercise exercise;
                exercise = dataSnapshot.getValue(Exercise.class);
                exercises.remove(exercise);
                exercises.add(exercise);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Exercise exercise;
                exercise = dataSnapshot.getValue(Exercise.class);
                //Toast.makeText(getContext(), dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                exercises.remove(exercise);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void applyDatas(int sets, int reps) {
        //megérkezett a sets és reps
        if(sets!=0 && reps!=0)
        {
            //Az adott ID-ű felhasználó, adott workoutjához hozzáadjuk a az adott gyakorlatot adott sets-el és repssel
            DatabaseReference myRef = database.getReference(SignInController.mUserId)
                    .child("MyWorkouts").child(workout.getName())
                    .child("WorkoutExercises").child(actExercise.getName());
            WorkoutExercise workoutExercise=new WorkoutExercise(actExercise.getName(),actExercise.getId(),sets,reps);
            myRef.setValue(workoutExercise);
//            myRef.child("set").setValue(sets);
//            myRef.child("rep").setValue(reps);
//            myRef.child("exerciseId").setValue(actExercise.getId());
//            myRef.child("exerciseName").setValue(actExercise.getName());
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Set and rep: "+Integer.toString(sets)+" "+Integer.toString(reps), Toast.LENGTH_SHORT).show();
    }

    //region ExerciseAdapter
    public class ExerciseAdapter extends RecyclerView.Adapter<ExercisesForAddingActivity.ExerciseAdapter.ExerciseViewHolder>
    {
        List<Exercise> exercises;
        OnListFragmentInteractionListener mlistener;

        public ExerciseAdapter(List<Exercise> exercises, OnListFragmentInteractionListener mListener) {
            this.exercises=exercises;
            this.mlistener=mListener;
        }

        @Override
        public ExercisesForAddingActivity.ExerciseAdapter.ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_exercises_for_adding_item,parent,false);
            exerciseholder=new ExercisesForAddingActivity.ExerciseAdapter.ExerciseViewHolder(view);
            return  new ExercisesForAddingActivity.ExerciseAdapter.ExerciseViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return exercises.size();
        }

        @Override
        public void onBindViewHolder(final ExercisesForAddingActivity.ExerciseAdapter.ExerciseViewHolder holder, int position) {
            holder.exercise=exercises.get(position);
            holder.exerciseName.setText(holder.exercise.getName());
            holder.exerciseMuscles.setText(holder.exercise.getMuscle1());
            if(holder.exercise.getMuscle2()!="")
            {
                holder.exerciseMuscles.append(","+holder.exercise.getMuscle2());
                if(holder.exercise.getMuscle3()!="")
                {
                    holder.exerciseMuscles.append(","+holder.exercise.getMuscle3());
                }
            }
            if(holder.exercise!=null){

                new ExercisesForAddingActivity.ImageLoadAsync(holder).execute(holder.exercise.getImageUrl()); //képek betöltése
                GetUrlForImage("");
                //Toast.makeText(getContext(), GetUrlForImage(""), Toast.LENGTH_SHORT).show();
            }
            else{
                holder.exerciseName.setBackgroundResource(R.drawable.bench_press);}

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadExercise(holder.exercise);
                    //Toast.makeText(v.getContext(), holder.exerciseName.getText(), Toast.LENGTH_SHORT).show();
                }

            });
        }

        private void loadExercise(Exercise exercise)
        {
            Intent myIntent = new Intent(ExercisesForAddingActivity.this, ExerciseDetailsActivity.class);
            myIntent.putExtra("exercise",exercise);
            //TODO: Ha lesz muscle3, akkor azt is adjuk át
            startActivity(myIntent);
        }


        public class ExerciseViewHolder extends RecyclerView.ViewHolder{
            TextView exerciseName;
            TextView exerciseMuscles;
            ImageView exerciseImage;
            Button exerciseAddButton;
            Exercise exercise;
            public final View mView;
            public ExerciseViewHolder(View itemView){
                super(itemView);
                mView=itemView;
                exerciseAddButton=itemView.findViewById(R.id.fragment_exercises_for_adding_item_addbutton);
                exerciseName=itemView.findViewById(R.id.fragment_exercises_for_adding_item_name);
                exerciseMuscles=itemView.findViewById(R.id.fragment_exercises_for_adding_item_muscles);
                exerciseImage=itemView.findViewById(R.id.exercise_imageInExercisesforAddingFragment);
                exerciseAddButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actExercise=exercise;
                        openDialog();
                    }
                });
            }
        }

        private void openDialog() {
            SetsAndRepsDialog setsAndRepsDialog =new SetsAndRepsDialog();
            setsAndRepsDialog.show(getSupportFragmentManager(),"example");
        }
    }

    private void GetUrlForImage(String name) {
        // Reference to an image file in Cloud Storage
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference pathref= storageReference.child("Exercises/18-10.jpg");
        pathref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(ExercisesForAddingActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                //new ImageLoadAsync(holder).execute(holder.exercise.getImageUrl());
            }
        });
        //Glide.with(getActivity().getApplicationContext()).load(storageReference).into(exerciseholder.exerciseName.getBackground());
    }
    //endregion


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */



    //Képek betöltése
    private class ImageLoadAsync extends AsyncTask<String, Void, Bitmap> {

        ExercisesForAddingActivity.ExerciseAdapter.ExerciseViewHolder holder;
        public ImageLoadAsync(ExercisesForAddingActivity.ExerciseAdapter.ExerciseViewHolder holder){
            this.holder=holder;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
//            if(isDetached()){
//                cancel(true);
//                return null;
//            }
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
//            if(isDetached())
//                return;
            if(bmp != null){
                super.onPostExecute(bmp);
                Drawable d = new BitmapDrawable(getResources(), bmp);
                if(holder != null)
                    holder.exerciseImage.setImageDrawable(d);
//                    holder.exerciseName.setBackground(d);
            }
        }

    }
}
