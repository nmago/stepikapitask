package ru.nmago.stepikapitest.model;

import java.util.List;

public class StepicResponse {

    private Meta meta;
    private List<Course> courses;

    public List<Course> getCourses(){
        return courses;
    }

    public Meta getMeta(){
        return meta;
    }

}
