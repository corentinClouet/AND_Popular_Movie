package com.example.android.popularmovies.activities;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.entities.Movie;
import com.example.android.popularmovies.utilities.AppExecutors;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private final static String TAG = DetailActivity.class.getSimpleName();
    private final static String FAVORITE = "favorite";
    private final static String UNFAVORITE = "unfavorite";

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mYearTextView;
    private TextView mRateTextView;
    private TextView mSynopsisTextView;
    private Button mFavoriteButton;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate");

        //display up action button
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //get database
        mDb = AppDatabase.getInstance(getApplicationContext());

        //init views
        mPosterImageView = findViewById(R.id.poster_imageView);
        mTitleTextView = findViewById(R.id.title_textView);
        mYearTextView = findViewById(R.id.year_textView);
        mRateTextView = findViewById(R.id.rate_textView);
        mSynopsisTextView = findViewById(R.id.synopsis_textView);
        mFavoriteButton = findViewById(R.id.favorite_button);

        //get the object pass in extra
        final Movie movie = getIntent().getExtras().getParcelable("movie");
        Log.d(TAG, "Current movie " + movie.getTitle());

        //fill the details views
        displayDetail(movie);
        initFavoriteButton(movie.getMovieId());

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase(movie);
            }
        });
    }

    //display common information about the movie
    private void displayDetail(Movie movie){
        //load wallpaper image
        Picasso.with(this)
                .load(movie.getPosterUrl())
                .into(mPosterImageView);

        //init text for all textViews
        mTitleTextView.setText(movie.getTitle());
        mYearTextView.setText(movie.getReleaseDate());
        mRateTextView.setText(String.valueOf(movie.getVoteAverage()));
        mSynopsisTextView.setText(movie.getSynopsis());
    }

    //update the button's text if the movie is a favorite movie or not
    private void initFavoriteButton(final int id){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mDb.movieDao().getNbMovie(id) > 0){
                    Log.d(TAG, "Favorite movie");
                    initStyleFavoriteButton(FAVORITE);
                }else{
                    Log.d(TAG, "Unfavorite movie");
                    initStyleFavoriteButton(UNFAVORITE);
                }
            }
        });
    }

    //update the style of favorite button
    private void initStyleFavoriteButton(String style){
        if (style.equals(FAVORITE)){
            mFavoriteButton.setText(R.string.favorite_label);
            mFavoriteButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else{
            mFavoriteButton.setText(R.string.unfavorite_label);
            mFavoriteButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        }
    }

    //update the database by deleting or inserting the movie in favorite table
    private void updateDatabase(Movie movie){
        if (mFavoriteButton.getText().equals(getString(R.string.favorite_label))){
            deleteMovie(movie);
            initStyleFavoriteButton(UNFAVORITE);
        }
        else{
            insertMovie(movie);
            initStyleFavoriteButton(FAVORITE);
        }
    }

    //delete movie from the database
    private void deleteMovie(final Movie movie){
        Log.d(TAG, "deleteMovie");
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().deleteMovie(movie);
            }
        });
    }

    //insert movie in the database
    private void insertMovie(final Movie movie){
        Log.d(TAG, "insertMovie");
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insertMovie(movie);
            }
        });
    }
}
