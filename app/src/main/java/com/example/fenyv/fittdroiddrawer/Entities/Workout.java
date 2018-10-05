package com.example.fenyv.fittdroiddrawer.Entities;

import java.util.List;

public class Workout {
    int id;
    public String name;
    public List<WorkoutExercise> workoutExercises;

    @Override
    public String toString() {
        return name;
    }

    public Workout(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
