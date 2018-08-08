package com.movies.popularmovies.model;

/**
 * Created by root on 05/08/18.
 */

public class Movie extends PosterMovie {

    private String synopsis;
    private double rate;
    private String date;

    public Movie() {
        super();
    }

    public Movie(int id, String title, String posterUrl, String synopsis, double rate, String date) {
        super(id, title, posterUrl);
        this.synopsis = synopsis;
        this.rate = rate;
        this.date = date;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
