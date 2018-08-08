package com.movies.popularmovies.utils;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by root on 05/08/18.
 */

public class NetworkUtils {

    private static final String API_KEY = "PUT_YOUR_API_KEY_HERE";

    private static final String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3";
    private static final String PARAM_API_KEY = "api_key";
    public static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";
    public static final String posterType = "w185";

    public static URL buildMovieUrl(String query) {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL+"/movie/"+query).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
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