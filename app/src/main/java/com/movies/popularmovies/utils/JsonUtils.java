package com.movies.popularmovies.utils;

import com.movies.popularmovies.model.Movie;
import com.movies.popularmovies.model.Review;
import com.movies.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 05/08/18.
 */

public class JsonUtils {
    public static final String TITLE_KEY = "original_title";
    public static final String POSTER_KEY = "poster_path";
    public static final String SYNOPSIS_KEY = "overview";
    public static final String RATING_KEY = "vote_average";
    public static final String DATE_KEY = "release_date";
    public static final String ID_KEY = "id";
    public static final String LIST_KEY = "results";
    public static final String NAME_KEY = "name";
    public static final String KEY = "key";
    public static final String AUTHOR_KEY = "author";
    public static final String CONTENT_KEY = "content";




    public static Movie buildMovieFromJson(JSONObject jsonObject) throws JSONException{
        Movie movie = new Movie();
        movie.setId(jsonObject.getInt(ID_KEY));
        movie.setTitle(jsonObject.getString(TITLE_KEY));
        movie.setPosterUrl(jsonObject.getString(POSTER_KEY));
        movie.setSynopsis(jsonObject.getString(SYNOPSIS_KEY));
        movie.setRate(jsonObject.getDouble(RATING_KEY));
        movie.setDate(jsonObject.getString(DATE_KEY));

        return movie;
    }

    public static Trailer buildTrailerFromJson(JSONObject jsonObject) throws JSONException{
        Trailer trailer = new Trailer();
        trailer.setKey(jsonObject.getString(KEY));
        trailer.setName(jsonObject.getString(NAME_KEY));

        return trailer;
    }

    public static Review buildReviewFromJson(JSONObject jsonObject) throws JSONException{
        Review review = new Review();
        review.setId(jsonObject.getString(ID_KEY));
        review.setAuthor(jsonObject.getString(AUTHOR_KEY));
        review.setContent(jsonObject.getString(CONTENT_KEY));

        return review;
    }


    public static List<Review> buildReviewListFromJson(JSONArray jsonArray) throws JSONException{
        List<Review> list = new ArrayList<>();
        int lenght = jsonArray.length();
        for(int i = 0; i < lenght; i++){
            list.add(buildReviewFromJson(jsonArray.getJSONObject(i)));
        }

        return list;
    }

    public static List<Trailer> buildTrailerListFromJson(JSONArray jsonArray) throws JSONException{
        List<Trailer> list = new ArrayList<>();
        int lenght = jsonArray.length();
        for(int i = 0; i < lenght; i++){
            list.add(buildTrailerFromJson(jsonArray.getJSONObject(i)));
        }

        return list;
    }

    public static List<Movie> buildMovieListFromJson(JSONArray jsonArray) throws JSONException{
        List<Movie> list = new ArrayList<>();
        int lenght = jsonArray.length();
        for(int i = 0; i < lenght; i++){
            list.add(buildMovieFromJson(jsonArray.getJSONObject(i)));
        }

        return list;
    }


    public static JSONArray getMoviesListJson(JSONObject jso) throws JSONException{
        return jso.getJSONArray(LIST_KEY);
    }

}
