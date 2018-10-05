package com.example.fenyv.fittdroiddrawer.Entities;

import java.io.Serializable;

public  class Exercise implements Serializable {
    private int id;
    private String description="";
    private String imageUrl ="";
    private String name="";
    private String muscle1="";
    private String muscle2="";
    private String muscle3="";

    //region konstruktorok
    public Exercise(String name,String description,String muscle)
        {
            this.name = name;
            this.description = description;
            this.muscle1=muscle;
        }

        public Exercise(String name,String description,String muscle,String imgageUrl)
        {
            this.name = name;
            this.description = description;
            this.muscle1=muscle;
            this.imageUrl =imgageUrl;
        }
        //NoSql adatbázis json-átalakításos cucc miatt van rá szükség
        public Exercise() {

        }
    //endregion

    //region setterek,getterek
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
        public String getImageUrl() {
            return imageUrl;
        }

        public String getMuscle2() {
            return muscle2;
        }

        public String getMuscle3() {
            return muscle3;
        }
        @Override
        public String toString() {
            return name+" "+ muscle1+" "+description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getMuscle1() {
            return muscle1;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setImg(String img) {
            this.imageUrl = img;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMuscle1(String muscle1) {
            this.muscle1 = muscle1;
        }

        public void setMuscle2(String muscle2) {
            this.muscle2 = muscle2;
        }

        public void setMuscle3(String muscle3) {
            this.muscle3 = muscle3;
        }
    //endregion

    /**Két Exercise akkor egyenlő, ha egynlő az idjük. */
    @Override
    public boolean equals(Object obj) {
            if(obj.getClass()==this.getClass()){
                Exercise exercise=(Exercise)obj;
                return ((Exercise) obj).getId()==(id);
            }
        return false;
    }
}

