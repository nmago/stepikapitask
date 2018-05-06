package ru.nmago.stepikapitest.model;

import com.google.gson.annotations.SerializedName;

public class Course {
    private long id;
    private String title;
    private String summary;

    @SerializedName("learners_count")
    private int learnersCount;

    public String getTitle(){
        return title;
    }

    public int getLearnersCount() {
        return learnersCount;
    }
}
