package com.example.fenyv.fittdroiddrawer.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fenyv.fittdroiddrawer.AddNewWorkoutDialog;
import com.example.fenyv.fittdroiddrawer.Entities.Workout;
import com.example.fenyv.fittdroiddrawer.Interfaces.OnListFragmentInteractionListener;
import com.example.fenyv.fittdroiddrawer.MyWorkoutDetailsActivity;
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
    private ArrayList<String>keys;



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
       // if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            //recyclerView.setAdapter(new WorkoutAdapter(WorkoutContent.ITEMS, mListener));
//            workouts=new ArrayList<>();
//            adapter=new WorkoutAdapter(workouts,mListener);

        //}
        workouts=new ArrayList<>();
        database=FirebaseDatabase.getInstance();
        keys=new ArrayList<>();
        getDataFromDataBase();
        recyclerView = (RecyclerView) view.findViewById(R.id.relist);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new WorkoutAdapter(workouts,mListener);
        recyclerView.setAdapter(adapter);
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
                String key= dataSnapshot.getKey();
                keys.add(key);
                int index = keys.indexOf(key);
                workout.setId(index+1);
                workouts.add(workout);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Workout workout;
                workout=dataSnapshot.getValue(Workout.class);
                String key= dataSnapshot.getKey();
                int index = keys.indexOf(key);
                workouts.set(index,workout);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Workout workout;
                workout=dataSnapshot.getValue(Workout.class);
                workouts.remove(workout);
                String key= dataSnapshot.getKey();
                int index = keys.indexOf(key);
                keys.remove(key);
                workouts.remove(index);
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
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadWorkout(holder.workout);
                }
            });


            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
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
                    mView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                            menu.add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    reference.child(workoutNameTextView.getText().toString()).removeValue();
                                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();

                                    return true;
                                }
                            });
                        }
                    });
                }
            }
        }



    private void loadWorkout(Workout workout) {
        Intent myIntent = new Intent(getActivity(), MyWorkoutDetailsActivity.class);
        myIntent.putExtra("workout",workout);
        getActivity().startActivity(myIntent);
    }


    private void initializeComponents(View view) {
        fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.workout_option_menu_delete:
//                Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();
//                return true;
//                default: return super.onContextItemSelected(item);
//        }
//
//
//    }

    private void openDialog() {
        AddNewWorkoutDialog addNewWorkoutDialog =new AddNewWorkoutDialog();
        addNewWorkoutDialog.show(getActivity().getSupportFragmentManager(),"example");
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
