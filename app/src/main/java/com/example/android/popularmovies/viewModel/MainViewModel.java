package com.example.android.popularmovies.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.entities.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> movies;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        movies = database.movieDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
