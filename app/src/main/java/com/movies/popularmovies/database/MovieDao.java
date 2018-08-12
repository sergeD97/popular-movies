package com.movies.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.movies.popularmovies.model.Movie;
import java.util.List;

/**
 * Created by Serge Pessokho on 11/08/2018.
 */

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies ")
    LiveData<List<Movie>> selectAll();

    @Delete
    void delete(Movie movie);

    @Insert
    void insert(Movie movie);

    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<Movie> select(int id);
}
