package com.example.android.popularmovies.utilities;

import android.content.Context;

import com.example.android.popularmovies.entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public final class MovieJsonUtils {

    private static final int NB_MOVIE = 12; //for this exercise, I only get the first 10 movies
    private static final String BASE_THUMBNAIL_URL = "http://image.tmdb.org/t/p/w342/";
    private static final String BASE_WALLPAPER_URL = "http://image.tmdb.org/t/p/original/";

    /**
     * This method parses JSON from a web response and returns an array of movies
     * describing the movies.
     * @param movieJsonStr JSON response from server
     *
     * @return Array of Movie describing movies data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<Movie> parseMovieJsonResponse(Context context, String movieJsonStr) throws JSONException {

        /* String array to hold each movie */
        ArrayList<Movie> lstMovie = new ArrayList<>();

        JSONObject readerJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = readerJson.getJSONArray("results");

        for (int i = 0; i < NB_MOVIE; i++) {

            //temporary variable to store movie's data
            Movie tmpMovie = new Movie();

            //Get the JSON object representing the current movie
            JSONObject movieObject = movieArray.getJSONObject(i);

            //set the temporary objects with returned values
            tmpMovie.setTitle(movieObject.getString("title"));
            tmpMovie.setReleaseDate(movieObject.getString("release_date"));
            tmpMovie.setPosterUrl(BASE_THUMBNAIL_URL + movieObject.getString("poster_path"));
            tmpMovie.setWallpaperUrl(BASE_WALLPAPER_URL + movieObject.getString("backdrop_path"));
            tmpMovie.setVoteAverage(movieObject.getDouble("vote_average"));
            tmpMovie.setSynopsis(movieObject.getString("overview"));

            //add the movie object in the list
            lstMovie.add(tmpMovie);
        }

        return lstMovie;
    }
}
