package com.example.fenyv.fittdroiddrawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fenyv.fittdroiddrawer.dummy.DummyContent3.DummyItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ExerscisesFragment extends Fragment {

    List<Exercise> exercises;
    ExerciseAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ExerciseAdapter.ExerciseViewHolder exerciseholder;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExerscisesFragment() {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercisegriditem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            exercises=new ArrayList<>();
            adapter=new ExerciseAdapter(exercises, mListener);
        }

        database=FirebaseDatabase.getInstance();
        getDataFromDataBase();
        getActivity().setTitle("Exercises");
        return view;
    }

    void getDataFromDataBase(){
//        Map<String, Exercise> exerciseMap = new HashMap<>();
//        exerciseMap.put("bench press3",new Exercise("benchpress","description","biceps"));
        reference=database.getReference("Exercises");
//        reference.setValue(exerciseMap);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Exercise exercise=new Exercise();
                exercise=dataSnapshot.getValue(Exercise.class);
                if(exercise!=null)
                    Toast.makeText(getContext(), exercise.toString(), Toast.LENGTH_SHORT).show();
                exercises.add(exercise);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Exercise exercise=new Exercise();
                exercise=dataSnapshot.getValue(Exercise.class);
                exercises.remove(exercise);
                exercises.add(exercise);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Exercise exercise=new Exercise();
                exercise=dataSnapshot.getValue(Exercise.class);
                Toast.makeText(getContext(), dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
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

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>
    {
        List<Exercise> exercises;
        OnListFragmentInteractionListener mlistener;

        public ExerciseAdapter(List<Exercise> exercises, OnListFragmentInteractionListener mListener) {
            this.exercises=exercises;
            this.mlistener=mListener;
        }

        @Override
        public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_exercisegriditem,parent,false);
               exerciseholder=new ExerciseViewHolder(view);
            return  new ExerciseViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return exercises.size();
        }

        @Override
        public void onBindViewHolder(final ExerciseAdapter.ExerciseViewHolder holder, int position) {
            holder.exercise=exercises.get(position);
            holder.exerciseName.setText(holder.exercise.getName());
            if(holder.exercise!=null){
                    new ImageLoadAsync(holder).execute(holder.exercise.getImageUrl());
            }
            else{
            holder.exerciseName.setBackgroundResource(R.drawable.bench_press);}

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), holder.exerciseName.getText(), Toast.LENGTH_SHORT).show();
            }

});
        }

        public class ExerciseViewHolder extends RecyclerView.ViewHolder{
            TextView exerciseName;
            Exercise exercise;
            public final View mView;
            public ExerciseViewHolder(View itemView){
                super(itemView);
                mView=itemView;
                exerciseName=itemView.findViewById(R.id.content);
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////


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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    private class ImageLoadAsync extends AsyncTask<String, Void, Bitmap> {
        ExerciseAdapter.ExerciseViewHolder holder;
        public ImageLoadAsync(ExerciseAdapter.ExerciseViewHolder holder){
            this.holder=holder;
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
            super.onPostExecute(bmp);
            if(bmp != null){
                Drawable d = new BitmapDrawable(getResources(), bmp);
                if(holder != null)
                    holder.exerciseName.setBackground(d);
            }
        }
    }
}
