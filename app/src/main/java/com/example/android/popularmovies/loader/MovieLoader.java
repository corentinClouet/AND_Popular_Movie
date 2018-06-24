package com.example.android.popularmovies.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmovies.entities.Movie;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>>{

    private static final String LOG_TAG = MovieLoader.class.getName(); //tag for log messages
    private String mSortBy; //sort parameter

    /**
     * Constructs a new {@link MovieLoader}.
     *
     * @param context of the activity
     * @param sortBy to know how to sort data
     */
    public MovieLoader(Context context, String sortBy) {
        super(context);
        mSortBy = sortBy;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Movie> loadInBackground() {

        String resultJson = null;
        List<Movie> result = new ArrayList<>();

        //if we don't have a sort parameter then return
        if (mSortBy == null) {
            return null;
        }

        //init the query
        URL query = NetworkUtils.buildUrl(mSortBy);
        try {
            //get the JSON results
            resultJson = NetworkUtils.getResponseFromHttpUrl(query);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //if we have a JSON result
        if (resultJson != null) {
            try {
                //parse the JSON et return an ArrayList of movies
                result = MovieJsonUtils.parseMovieJsonResponse(getContext(),resultJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //return an ArrayList of movies (empty if we have an error)
        return result;
    }
}
