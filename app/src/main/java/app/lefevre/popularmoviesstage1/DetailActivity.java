package app.lefevre.popularmoviesstage1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import app.lefevre.popularmoviesstage1.data.MoviePreferences;
import app.lefevre.popularmoviesstage1.utilities.NetworkUtils;
import app.lefevre.popularmoviesstage1.utilities.TheMovieDatabaseJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "PopularMovies";

    private Integer mMovieId;

    private ImageView mPoster;
    private ImageView mBackdrop;
    private TextView mTagline;
    private TextView mTitle;
    private TextView mOverview;
    private TextView mVoteAverage;
    private TextView mReleaseDate;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPoster = findViewById(R.id.movie_poster);
        mBackdrop = findViewById(R.id.movie_backdrop);
        mTagline = findViewById(R.id.tv_movie_tagline);
        mTitle = findViewById(R.id.tv_movie_title);
        mOverview = findViewById(R.id.tv_movie_overview);
        mVoteAverage = findViewById(R.id.tv_movie_vote_average);
        mReleaseDate = findViewById(R.id.tv_movie_release_date);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("MovieId")) {
                mMovieId = intentThatStartedThisActivity.getIntExtra("MovieId", 475557);
                loadMovieData(mMovieId);
            }
        }

    }

    private void loadMovieData(Integer movieId) {
        showMovieDataView();

        String apiKey = MoviePreferences.getApiKey(this);
        QueryParams myQueryParams = new QueryParams(apiKey, movieId);

        new FetchMovieTask().execute(myQueryParams);
    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
        mPoster.setVisibility(View.VISIBLE);
        mBackdrop.setVisibility(View.VISIBLE);
        mTagline.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.VISIBLE);
        mOverview.setVisibility(View.VISIBLE);
        mVoteAverage.setVisibility(View.VISIBLE);
        mReleaseDate.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie data
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mPoster.setVisibility(View.INVISIBLE);
        mBackdrop.setVisibility(View.INVISIBLE);
        mTagline.setVisibility(View.INVISIBLE);
        mTitle.setVisibility(View.INVISIBLE);
        mOverview.setVisibility(View.INVISIBLE);
        mVoteAverage.setVisibility(View.INVISIBLE);
        mReleaseDate.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    public class FetchMovieTask extends AsyncTask<QueryParams, Void, Movie> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie doInBackground(QueryParams... params) {

            /* If there are no movies, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String apiKey = params[0].myApiKey;
            Integer id = params[0].myMovieId;

            //Log.i(TAG, apiKey);

            URL movieRequestUrl = NetworkUtils.buildUrl(apiKey, id);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                Movie movie = TheMovieDatabaseJsonUtils
                        .getFullMovieDataFromJson(DetailActivity.this, jsonMovieResponse);

                return movie;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie movie) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (movie != null) {
                showMovieDataView();

                Picasso.get().load("https://image.tmdb.org/t/p/original" + movie.backdropUrl).into(mBackdrop);
                Picasso.get().load("https://image.tmdb.org/t/p/w185" + movie.posterUrl).into(mPoster);
                mTagline.append("\n" + movie.tagline);
                mTitle.append(" " + movie.title);
                mOverview.append(" " + movie.overview);
                mVoteAverage.append(" " + movie.voteAverage.toString());
                mReleaseDate.append(" " + movie.releaseDate);

                getSupportActionBar().setTitle(movie.title);

            } else {
                showErrorMessage();
            }
        }
    }

    private static class QueryParams {
        String myApiKey;
        Integer myMovieId;

        QueryParams(String apiKey, Integer id) {
            this.myApiKey = apiKey;
            this.myMovieId = id;
        }
    }
}
