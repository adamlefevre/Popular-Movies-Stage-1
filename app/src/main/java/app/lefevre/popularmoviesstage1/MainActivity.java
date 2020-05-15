package app.lefevre.popularmoviesstage1;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import app.lefevre.popularmoviesstage1.MovieAdapter.MovieAdapterOnClickHandler;
import app.lefevre.popularmoviesstage1.data.MoviePreferences;
import app.lefevre.popularmoviesstage1.utilities.NetworkUtils;
import app.lefevre.popularmoviesstage1.utilities.TheMovieDatabaseJsonUtils;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler {

    private RecyclerView recyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private String searchBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_posters);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        int numberOfColumns = 3;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mMovieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        searchBy = MoviePreferences.getSearchBy(this);
        loadMovieData(searchBy);
    }

    private void loadMovieData(String searchBy) {
        showMovieDataView();

        String apiKey = MoviePreferences.getApiKey(this);
        new FetchMovieTask().execute(apiKey, searchBy);
    }

    @Override
    public void onClick(Movie movieDetails) {
        Context context = this;
        Toast.makeText(context, movieDetails.id, Toast.LENGTH_SHORT).show();
    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        recyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        recyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {

            /* If there are no movies, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String apiKey = params[0];
            String searchBy = params[1];

            URL movieRequestUrl = NetworkUtils.buildUrl(apiKey, searchBy);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                Movie[] movieData = TheMovieDatabaseJsonUtils
                        .getMoviesFromJson(MainActivity.this, jsonMovieResponse);

                return movieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_results_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_results) {
            sortHelper(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortHelper(MenuItem item) {
        if (item.getTitle().equals("Popular")) {
            item.setTitle("Top Rated");
            searchBy = "vote_average.desc";
        } else {
            item.setTitle("Popular");
            searchBy = "popularity.desc";
        }

        mMovieAdapter.notifyDataSetChanged();
    }
}