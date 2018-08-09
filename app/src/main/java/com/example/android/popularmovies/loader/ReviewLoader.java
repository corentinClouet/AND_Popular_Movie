package com.example.android.popularmovies.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.entities.Movie;
import com.example.android.popularmovies.entities.Review;
import com.example.android.popularmovies.entities.Video;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReviewLoader extends AsyncTaskLoader<List<Review>>{

    private static final String LOG_TAG = ReviewLoader.class.getName(); //tag for log messages
    private int mMovieId; //movie's id
    private static final int ID_ERROR = -1;

    /**
     * Constructs a new {@link ReviewLoader}.
     *
     * @param context of the activity
     * @param movieId to know what the movie to request
     */
    public ReviewLoader(Context context, int movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Review> loadInBackground() {

        Log.d(LOG_TAG, "loadInBackground");

        String resultJson = null;
        List<Review> result = new ArrayList<>();

        //if we don't have a movieId parameter then return
        if (mMovieId == ID_ERROR) {
            return null;
        }

        //init the query
        URL query = NetworkUtils.buildReviewUrl(mMovieId);
        try {
            //get the JSON results
            resultJson = NetworkUtils.getResponseFromHttpUrl(query);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //if we have a JSON result
        if (resultJson != null) {
            try {
                //parse the JSON et return an ArrayList of reviews
                result = MovieJsonUtils.parseReviewJsonResponse(getContext(),resultJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //return an ArrayList of reviews (empty if we have an error)
        return result;
    }
}
