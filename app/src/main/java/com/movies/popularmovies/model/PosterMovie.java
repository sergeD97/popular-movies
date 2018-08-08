package com.movies.popularmovies.model;

/**
 * Created by root on 05/08/18.
 */

public class PosterMovie {


    private int id;
    private String title;
    private String posterUrl;

    public PosterMovie() {
    }

    public PosterMovie(int id, String title, String posterUrl) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
