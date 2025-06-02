package com.example.salledesport.model.ApiResponse;


import com.example.salledesport.model.Slides;

import java.util.List;

public class SlideResponse {
    private int count;
    private String next;
    private String previous;
    private List<Slides> results;

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<Slides> getResults() {
        return results;
    }
}
