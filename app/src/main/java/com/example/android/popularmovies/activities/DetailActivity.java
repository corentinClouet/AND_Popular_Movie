package com.example.android.popularmovies.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.MovieAdapter;
import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.adapter.VideoAdapter;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.entities.Movie;
import com.example.android.popularmovies.entities.Review;
import com.example.android.popularmovies.entities.Video;
import com.example.android.popularmovies.loader.MovieLoader;
import com.example.android.popularmovies.loader.ReviewLoader;
import com.example.android.popularmovies.loader.VideoLoader;
import com.example.android.popularmovies.utilities.AppExecutors;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickHandler{

    private final static String TAG = DetailActivity.class.getSimpleName();
    private final static String FAVORITE = "favorite";
    private final static String UNFAVORITE = "unfavorite";
    private final static int VIDEO_LOADER_ID = 1;
    private final static int REVIEW_LOADER_ID = 2;

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mYearTextView;
    private TextView mRateTextView;
    private TextView mSynopsisTextView;
    private Button mFavoriteButton;

    private RecyclerView mVideoRecyclerView;
    private VideoAdapter mVideoAdapter;
    private TextView mVideoErrorMessageDisplay;
    private ProgressBar mVideoProgressBar;

    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private TextView mReviewErrorMessageDisplay;
    private ProgressBar mReviewProgressBar;

    private AppDatabase mDb;
    private Movie mMovie;

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

        //init global views
        mPosterImageView = findViewById(R.id.poster_imageView);
        mTitleTextView = findViewById(R.id.title_textView);
        mYearTextView = findViewById(R.id.year_textView);
        mRateTextView = findViewById(R.id.rate_textView);
        mSynopsisTextView = findViewById(R.id.synopsis_textView);
        mFavoriteButton = findViewById(R.id.favorite_button);

        //set the layoutManagers attached to ours recyclerViews
        LinearLayoutManager videoLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(getApplicationContext());

        //init videos views
        mVideoRecyclerView = findViewById(R.id.video_recyclerView);
        mVideoRecyclerView.setLayoutManager(videoLinearLayoutManager);
        mVideoRecyclerView.setHasFixedSize(true);
        mVideoProgressBar = findViewById(R.id.video_progress_bar);
        mVideoErrorMessageDisplay = findViewById((R.id.video_error_textView));

        /* The VideoAdapter is responsible for linking our videos data with the Views that
        * will end up displaying our videos data. */
        mVideoAdapter = new VideoAdapter(this, getApplicationContext());
        mVideoRecyclerView.setAdapter(mVideoAdapter);

        //init reviews views
        mReviewRecyclerView = findViewById(R.id.review_recyclerView);
        mReviewRecyclerView.setLayoutManager(reviewLinearLayoutManager);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewProgressBar = findViewById(R.id.review_progress_bar);
        mReviewErrorMessageDisplay = findViewById((R.id.review_error_textView));

        /* The ReviewAdapter is responsible for linking our reviews data with the Views that
         * will end up displaying our reviews data. */
        mReviewAdapter = new ReviewAdapter(getApplicationContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mReviewRecyclerView.getContext(),
                reviewLinearLayoutManager.getOrientation());
        mReviewRecyclerView.addItemDecoration(dividerItemDecoration);
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        //get the object pass in extra
        mMovie = getIntent().getExtras().getParcelable("movie");
        Log.d(TAG, "Current movie " + mMovie.getTitle());

        //fill the details views
        displayDetail(mMovie);
        initFavoriteButton(mMovie.getMovieId());

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase(mMovie);
            }
        });

        //verify internet connection
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loaders. Pass in the int ID constant defined above and pass in null for the bundle.
            loaderManager.initLoader(VIDEO_LOADER_ID, null, videoLoaderCallbacks);
            loaderManager.initLoader(REVIEW_LOADER_ID, null, reviewLoaderCallbacks);
        }
        else
        {
            // Hide loading indicator because there is not internet connection
            mVideoProgressBar.setVisibility(View.GONE);
            mReviewProgressBar.setVisibility(View.GONE);
            // Set empty state view to display "No internet connection."
            mVideoErrorMessageDisplay.setVisibility(View.VISIBLE);
            mVideoErrorMessageDisplay.setText(R.string.no_internet_connection);
            mReviewErrorMessageDisplay.setVisibility(View.VISIBLE);
            mReviewErrorMessageDisplay.setText(R.string.no_internet_connection);
        }
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
        mRateTextView.setText(String.valueOf(movie.getVoteAverage()) + getResources().getString(R.string.global_note));
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

    private LoaderManager.LoaderCallbacks<List<Video>> videoLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<Video>>() {
        @Override
        public Loader<List<Video>> onCreateLoader(int id, Bundle args) {
            Log.d(TAG, "onCreateLoader");
            if (null == mMovie){
              return null;
            }
            return new VideoLoader(getApplicationContext(), mMovie.getMovieId());
        }

        @Override
        public void onLoadFinished(Loader<List<Video>> loader, List<Video> data) {
            Log.d(TAG, "onLoadFinished");

            // Hide loading indicator because the data has been loaded
            mVideoProgressBar.setVisibility(View.GONE);

            // If there is a valid list of {@link Video}s, then add them to the adapter's
            // data set. This will trigger the RecyclerView to update.
            if (data != null && !data.isEmpty()) {
                mVideoAdapter.refreshData(data);
            }else{
                // Set empty state view to display "No data"
                mVideoErrorMessageDisplay.setVisibility(View.VISIBLE);
                mVideoErrorMessageDisplay.setText(R.string.no_data);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Video>> loader) {
            Log.d(TAG, "onLoaderReset");
            mVideoAdapter.refreshData(null);
        }
    };

    //Needed because of he interface of VideoLoader, used to open a video Intent (Youtube for example)
    @Override
    public void onClick(Video video) {
        Log.d(TAG, "Video onClick");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
        startActivity(intent);
    }

    private LoaderManager.LoaderCallbacks<List<Review>> reviewLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<Review>>() {
        @Override
        public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
            Log.d(TAG, "onCreateLoader");
            if (null == mMovie){
                return null;
            }
            return new ReviewLoader(getApplicationContext(), mMovie.getMovieId());
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
            Log.d(TAG, "onLoadFinished");

            // Hide loading indicator because the data has been loaded
            mReviewProgressBar.setVisibility(View.GONE);

            // Set empty state view to display "No data found." in case of we have no data
            //mErrorMessageDisplay.setText(R.string.no_data);

            // If there is a valid list of {@link Review}s, then add them to the adapter's
            // data set. This will trigger the RecyclerView to update.
            if (data != null && !data.isEmpty()) {
                mReviewAdapter.refreshData(data);
            }else{
                // Set empty state view to display "No data"
                mReviewErrorMessageDisplay.setVisibility(View.VISIBLE);
                mReviewErrorMessageDisplay.setText(R.string.no_data);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {
            Log.d(TAG, "onLoaderReset");
            mReviewAdapter.refreshData(null);
        }
    };
}
