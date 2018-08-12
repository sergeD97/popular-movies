package com.movies.popularmovies.model;

/**
 * Created by Serge Pessokho on 11/08/2018.
 */

public class Trailer {
    private String key;
    private String name;

    public Trailer(){

    }

    public Trailer(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
