package com.movies.popularmovies.model;

/**
 * Created by Serge Pessokho on 11/08/2018.
 */

public class Review {

    private String id;
    private String author;
    private String content;

    public Review(){

    }

    public Review(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
