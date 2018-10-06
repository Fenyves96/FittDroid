package com.example.fenyv.fittdroiddrawer.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fenyv.fittdroiddrawer.Contents.WorkoutContent;
import com.example.fenyv.fittdroiddrawer.Entities.Workout;
import com.example.fenyv.fittdroiddrawer.Interfaces.OnListFragmentInteractionListener;
import com.example.fenyv.fittdroiddrawer.R;
import com.example.fenyv.fittdroiddrawer.SignInController;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MyWorkoutsFragment extends Fragment {


    FloatingActionButton fab;
    List<Workout> workouts;
    FirebaseDatabase database;
    DatabaseReference reference;
    RecyclerView recyclerView;
    WorkoutAdapter adapter;
    WorkoutAdapter.WorkoutViewHolder workoutholder;


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyWorkoutsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyWorkoutsFragment newInstance(int columnCount) {
        MyWorkoutsFragment fragment = new MyWorkoutsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myworkouts_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //recyclerView.setAdapter(new WorkoutAdapter(WorkoutContent.ITEMS, mListener));
            workouts=new ArrayList<>();
            adapter=new WorkoutAdapter(workouts,mListener);

        }
        database=FirebaseDatabase.getInstance();
        getDataFromDataBase();
        adapter=new WorkoutAdapter(workouts,mListener);
        getActivity().setTitle("My Workouts");
        initializeComponents(view);
        return view;
    }

    private void getDataFromDataBase() {
        reference=database.getReference(SignInController.mUserId+"/MyWorkouts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Workout workout;
                workout=dataSnapshot.getValue(Workout.class);
                if(workout!=null) {
//                    Toast.makeText(getContext(), exercise.toString(), Toast.LENGTH_SHORT).show();
                    workouts.add(workout);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Workout workout;
                workout=dataSnapshot.getValue(Workout.class);
                workouts.remove(workout);
                workouts.add(workout);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Workout workout;
                workout=dataSnapshot.getValue(Workout.class);
                //Toast.makeText(getContext(), dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                workouts.remove(workout);
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

    public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>
    {
        List<Workout> workouts;
        OnListFragmentInteractionListener mlistener;

        public WorkoutAdapter(List<Workout> workouts, OnListFragmentInteractionListener mListener) {
            this.workouts=workouts;
            this.mlistener=mListener;
        }

        @Override
        public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_myworkoutsitem,parent,false);
            workoutholder=new WorkoutViewHolder(view);
            return  new WorkoutViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return workouts.size();
        }

        @Override
        public void onBindViewHolder(final WorkoutAdapter.WorkoutViewHolder holder, int position) {
            holder.workout = workouts.get(position);
            holder.workoutNameTextView.setText(holder.workout.getName());
            holder.workoutIdTextView.setText(String.valueOf(holder.workout.getId()));
            if (holder.workout != null) {

//                new ImageLoadAsync(holder).execute(holder.exercise.getImageUrl()); //képek betöltése
//                GetUrlForImage("");
                //Toast.makeText(getContext(), GetUrlForImage(""), Toast.LENGTH_SHORT).show();
            } //else {
//                holder.exerciseName.setBackgroundResource(R.drawable.bench_press);
//            }

//            holder.mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    loadExercise(holder.exercise);
//                    //Toast.makeText(v.getContext(), holder.exerciseName.getText(), Toast.LENGTH_SHORT).show();
//                }
//
//            });
        }
            public class WorkoutViewHolder extends RecyclerView.ViewHolder{
                TextView workoutNameTextView;
                TextView workoutIdTextView;
                Workout workout;
                public final View mView;
                public WorkoutViewHolder(View itemView){
                    super(itemView);
                    mView=itemView;
                    workoutNameTextView =itemView.findViewById(R.id.myWorkoutName);
                    workoutIdTextView=itemView.findViewById(R.id.myWorkoutId);
                }
            }
        }


    private void initializeComponents(View view) {
//        fab=view.findViewById(R.id.fabMyWorkouts);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(),"adfafsd",Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
