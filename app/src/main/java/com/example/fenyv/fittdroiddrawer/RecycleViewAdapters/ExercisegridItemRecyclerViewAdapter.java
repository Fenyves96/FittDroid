package com.example.fenyv.fittdroiddrawer.RecycleViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.fenyv.fittdroiddrawer.Entities.Exercise;
import com.example.fenyv.fittdroiddrawer.Interfaces.OnListFragmentInteractionListener;
import com.example.fenyv.fittdroiddrawer.R;
import com.example.fenyv.fittdroiddrawer.dummy.DummyContent3.DummyItem;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ExercisegridItemRecyclerViewAdapter extends RecyclerView.Adapter<ExercisegridItemRecyclerViewAdapter.ViewHolder> {

    private final List<Exercise> mExercises;
    private final OnListFragmentInteractionListener mListener;

    public ExercisegridItemRecyclerViewAdapter(List<Exercise> items, OnListFragmentInteractionListener listener) {
        mExercises = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_exercisegriditem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mExercises.get(position);
       // holder.mIdView.setText(mExercises.get(position).id);
        //holder.mContentView.setText(mExercises.get(position).content);
       // holder.mIdView.setVisibility(View.INVISIBLE);
        holder.mContentView.setBackgroundResource(R.drawable.bench_press);
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    Fragment fragment;
//                    fragment = new ExerciseDescriptionFragment();
//                    if (fragment != null) {
//                        AppCompatActivity activity = (AppCompatActivity) holder.mView.getContext();
//                        Fragment myFragment = new ExerciseDescriptionFragment();
//                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, myFragment).addToBackStack(null).commit();
//                    }
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                   // mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mExercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mIdView;
        public final TextView mContentView;
        public Exercise mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
        //  mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
