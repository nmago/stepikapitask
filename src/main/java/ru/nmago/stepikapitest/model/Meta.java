package ru.nmago.stepikapitest.model;

import com.google.gson.annotations.SerializedName;

public class Meta{
    private int page;
    @SerializedName("has_next")
    private boolean hasNext;

    public int getPage(){
        return page;
    }

    public boolean isHasNext() {
        return hasNext;
    }
}
