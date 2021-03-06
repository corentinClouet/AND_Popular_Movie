package com.example.android.popularmovies.activities;

import android.app.LoaderManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.MovieAdapter;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.entities.Movie;
import com.example.android.popularmovies.loader.MovieLoader;
import com.example.android.popularmovies.utilities.AppExecutors;
import com.example.android.popularmovies.viewModel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, MovieAdapter.MovieAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private AppDatabase mDb;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FAVORITE_SETTING = "favorite";
    private static final int TMDB_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG, "onCreate");

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_movie);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_empty_view);

        //set the layoutManager attached to our recyclerView
        int nbColumns = calculateNoOfColumns(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), nbColumns);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        /*
         * The MovieAdapter is responsible for linking our movies data with the Views that
         * will end up displaying our movies data.
         */
        mMovieAdapter = new MovieAdapter(this, getApplicationContext());

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mMovieAdapter);

        //verify internet connection
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            if(getSortByPreference().equals(FAVORITE_SETTING)){
                mDb = AppDatabase.getInstance(getApplicationContext());
                setupViewModel();
            }else{
                // Get a reference to the LoaderManager, in order to interact with loaders.
                LoaderManager loaderManager = getLoaderManager();
                // Initialize the loader. Pass in the int ID constant defined above and pass in null for the bundle.
                loaderManager.initLoader(TMDB_LOADER_ID, null, this);
            }
        }
        else
        {
            // Hide loading indicator because there is not internet connection
            mLoadingIndicator.setVisibility(View.GONE);
            // Set empty state view to display "No internet connection."
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mErrorMessageDisplay.setText(R.string.no_internet_connection);
        }
    }

    @Override
    // This method initialize the contents of the Activity's options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // This method is called whenever an item in the options menu is selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        String sortByPref = getSortByPreference();
        return new MovieLoader(this, sortByPref);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.d(TAG, "onLoadFinished");

        // Hide loading indicator because the data has been loaded
        mLoadingIndicator.setVisibility(View.GONE);

        // Set empty state view to display "No data found." in case of we have no data
        mErrorMessageDisplay.setText(R.string.no_data);

        // If there is a valid list of {@link Movie}s, then add them to the adapter's
        // data set. This will trigger the RecyclerView to update.
        if (data != null && !data.isEmpty()) {
            mMovieAdapter.refreshData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        Log.d(TAG, "onLoaderReset");
        mMovieAdapter.refreshData(null);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(getBaseContext(), DetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    //get the value of the preference with sort_by key
    public String getSortByPreference(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sortByPref = sharedPref.getString(getString(R.string.settings_sort_by_key), getString(R.string.settings_sort_by_default));
        return  sortByPref;
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mMovieAdapter.refreshData(movies);
            }
        });
        // Hide loading indicator because the data are update
        mLoadingIndicator.setVisibility(View.GONE);
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 100;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }
}


