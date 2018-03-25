package com.example.fenyv.fittdroiddrawer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseDescriptionFragment extends Fragment {

    ImageView imageView;
    TextView title;
    TextView description;
    public ExerciseDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_description, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        imageView=getActivity().findViewById(R.id.exercise_image);
        title=getActivity().findViewById(R.id.title_exercise);
        description=getActivity().findViewById(R.id.desc_exercise);
        description.setMovementMethod(new ScrollingMovementMethod());
    }

}
