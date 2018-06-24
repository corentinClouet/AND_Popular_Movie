package com.example.android.popularmovies.activities;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.entities.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageView mWallpaperImageView;
    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteTextView;
    private TextView mSynopsisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //display up action button
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //init views
        mWallpaperImageView = findViewById(R.id.iv_detail_wallpaper);
        mTitleTextView = findViewById(R.id.tv_detail_title);
        mReleaseDateTextView = findViewById(R.id.tv_detail_release_date);
        mVoteTextView = findViewById(R.id.tv_detail_vote);
        mSynopsisTextView = findViewById(R.id.tv_detail_synopsis);

        //get the object pass in extra
        Movie movie = getIntent().getExtras().getParcelable("movie");

        //fill the details views
        displayDetail(movie);
    }

    private void displayDetail(Movie movie){
        //load wallpaper image
        Picasso.with(this)
                .load(movie.getWallpaperUrl())
                .into(mWallpaperImageView);

        //init text for all textViews
        mTitleTextView.setText(movie.getTitle());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mVoteTextView.setText(String.valueOf(movie.getVoteAverage()));
        mSynopsisTextView.setText(movie.getSynopsis());
    }
}
