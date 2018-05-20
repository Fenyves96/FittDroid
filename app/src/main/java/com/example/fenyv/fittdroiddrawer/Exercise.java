package com.example.fenyv.fittdroiddrawer;

import java.util.Objects;

public  class Exercise {
        String description;
        String imageUrl ="";
        String name;
        String muscle1;
        String muscle2;
        String muscle3;

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

        public Exercise() {

        }
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

    @Override
    public boolean equals(Object obj) {
            if(obj.getClass()==this.getClass()){
                Exercise exercise=(Exercise)obj;
                return (((Exercise) obj).getImageUrl().equals(imageUrl) || ((Exercise) obj).getName().equals(name));
            }
        return false;
    }
}

