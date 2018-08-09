package com.example.android.popularmovies.utilities;

import android.content.Context;

import com.example.android.popularmovies.entities.Movie;
import com.example.android.popularmovies.entities.Review;
import com.example.android.popularmovies.entities.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public final class MovieJsonUtils {

    private static final int NB_MOVIE = 12; //for this exercise, I only get the first 12 movies
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
            tmpMovie.setMovieId(movieObject.getInt("id"));
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

    /**
     * This method parses JSON from a web response and returns an array of videos
     * describing the movie.
     * @param videoJsonStr JSON response from server
     *
     * @return Array of Video about the movie
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<Video> parseVideoJsonResponse(Context context, String videoJsonStr) throws JSONException {

        /* String array to hold each video */
        ArrayList<Video> lstVideo = new ArrayList<>();

        JSONObject readerJson = new JSONObject(videoJsonStr);
        JSONArray videoArray = readerJson.getJSONArray("results");

        for (int i = 0; i < videoArray.length(); i++) {

            //temporary variable to store video's data
            Video tmpVideo = new Video();

            //Get the JSON object representing the current video
            JSONObject videoObject = videoArray.getJSONObject(i);

            //set the temporary objects with returned values
            tmpVideo.setId(videoObject.getString("id"));
            tmpVideo.setIso6391(videoObject.getString("iso_639_1"));
            tmpVideo.setIso31661(videoObject.getString("iso_3166_1"));
            tmpVideo.setKey(videoObject.getString("key"));
            tmpVideo.setName(videoObject.getString("name"));
            tmpVideo.setSite(videoObject.getString("site"));
            tmpVideo.setSize(videoObject.getInt("size"));
            tmpVideo.setType(videoObject.getString("type"));

            //add the video object in the list
            lstVideo.add(tmpVideo);
        }

        return lstVideo;
    }

    /**
     * This method parses JSON from a web response and returns an array of reviews
     * describing the movie.
     * @param reviewJsonStr JSON response from server
     *
     * @return Array of Review about the movie
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<Review> parseReviewJsonResponse(Context context, String reviewJsonStr) throws JSONException {

        /* String array to hold each review */
        ArrayList<Review> lstReview = new ArrayList<>();

        JSONObject readerJson = new JSONObject(reviewJsonStr);
        JSONArray reviewArray = readerJson.getJSONArray("results");

        for (int i = 0; i < reviewArray.length(); i++) {

            //temporary variable to store review's data
            Review tmpReview = new Review();

            //Get the JSON object representing the current review
            JSONObject reviewObject = reviewArray.getJSONObject(i);

            //set the temporary objects with returned values
            tmpReview.setAuthor(reviewObject.getString("author"));
            tmpReview.setContent(reviewObject.getString("content"));
            tmpReview.setId(reviewObject.getString("id"));
            tmpReview.setUrl(reviewObject.getString("url"));

            //add the review object in the list
            lstReview.add(tmpReview);
        }

        return lstReview;
    }
}
