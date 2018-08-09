/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the TMDB servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
    //TODO fill the KEYAPI variable
    private static final String KEYAPI = "";
    private final static String API_KEY_PARAM = "api_key";
    private final static String API_VIDEO_PARAM = "/videos";
    private final static String API_REVIEW_PARAM = "/reviews";

    /**
     * Builds the URL used to talk to the themoviedb server using a sort.
     *
     * @param sortBy The sort that will be queried for.
     * @return The URL to use to query the themoviedb server.
     */
    public static URL buildGlobalUrl(String sortBy) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL + sortBy).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, KEYAPI)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built global URI " + url);
        return url;
    }

    /**
     * Builds the URL used to talk to the themoviedb server using a movie id.
     *
     * @param movieId The id of the movie to request
     * @return The URL to use to query the videos of the movie.
     */
    public static URL buildVideoUrl(int movieId) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL + movieId + API_VIDEO_PARAM).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, KEYAPI)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built video URI " + url);
        return url;
    }

    /**
     * Builds the URL used to talk to the themoviedb server using a movie id.
     *
     * @param movieId The id of the movie to request
     * @return The URL to use to query the videos of the movie.
     */
    public static URL buildReviewUrl(int movieId) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL + movieId + API_REVIEW_PARAM).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, KEYAPI)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built review URI " + url);
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}