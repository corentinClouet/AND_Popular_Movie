package com.example.android.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.popularmovies.entities.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM favorite")
    LiveData<List<Movie>> loadAllMovies();

    @Insert
    void insertMovie(Movie movieEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movieEntry);

    @Delete
    void deleteMovie(Movie movieEntry);

    @Query("SELECT * FROM favorite WHERE id = :id")
    LiveData<Movie> loadTaskById(int id);

    @Query("SELECT COUNT(id) FROM favorite WHERE movieId = :id")
    int getNbMovie(int id);
}
