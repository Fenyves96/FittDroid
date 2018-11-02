package com.example.fenyv.fittdroiddrawer.Entities;

import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.List;

public class Workout implements Serializable {
    private int id;
    public String name;
    public List<WorkoutExercise> workoutExercises;

    @Override
    public String toString() {
        return name;
    }

    public Workout(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name=name;
    }



    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
