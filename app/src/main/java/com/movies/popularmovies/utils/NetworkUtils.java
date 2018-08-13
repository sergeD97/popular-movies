package com.movies.popularmovies.utils;

import android.net.Uri;

import com.movies.popularmovies.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by root on 05/08/18.
 */

public final class NetworkUtils {


    private static final String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3";
    private static final String PARAM_API_KEY = "api_key";
    public static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";
    public static final String posterType = "w185";
    public static  final String REVIEW = "/reviews";
    public static final String TRAILER = "/videos";
    public static final String YOUTUBE_WATCH = "https://www.youtube.com/watch?v=";

    private NetworkUtils(){}

    public static URL buildMovieUrl(String query) {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL+"/movie/"+query).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.MOVIEDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        BufferedReader reader ;


        HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
        try {
            reader = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            String l, ligne= "";

            while ((l = reader.readLine()) != null) {
                ligne += l;
            }

            return ligne;

        }finally {
            connexion.disconnect();
        }
    }
}
