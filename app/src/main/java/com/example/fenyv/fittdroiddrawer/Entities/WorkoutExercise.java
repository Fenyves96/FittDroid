package com.example.fenyv.fittdroiddrawer.Entities;

import java.io.Serializable;

public class WorkoutExercise implements Serializable {


    int exerciseId;
    String exerciseName;
    int set;
    int rep;

    public WorkoutExercise(){}
    public WorkoutExercise(String exerciseName, int exerciseId,int set, int rep) {
        this.exerciseId=exerciseId;
        this.exerciseName = exerciseName;
        this.set = set;
        this.rep = rep;
    }

    //region setterek,getterek
    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public int getRep() {
        return rep;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }


    //endregion


}
