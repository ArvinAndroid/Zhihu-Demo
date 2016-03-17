package com.cn.jason.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jun on 3/2/16.
 */
public class LatestNews {
    private String date;
    private List<Story> stories = new ArrayList<>();
    private List<Story> top_sotries = new ArrayList<>();

    public List<Story> getStories() {
        return stories;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public void setTop_sotries(List<Story> top_sotries) {
        this.top_sotries = top_sotries;
    }

    public List<Story> getTop_sotries() {
        return top_sotries;
    }

    public String getDate() {
        return date;
    }
}
