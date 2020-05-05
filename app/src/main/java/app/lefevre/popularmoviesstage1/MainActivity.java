package app.lefevre.popularmoviesstage1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.lefevre.popularmoviesstage1.data.MoviePreferences;
import app.lefevre.popularmoviesstage1.utilities.NetworkUtils;
import app.lefevre.popularmoviesstage1.utilities.TheMovieDatabaseJsonUtils;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressBar mLoadingIndicator;

    String[] myDataset; //= {"0","1","2","3","4","5","6","7","8","9"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.rv_posters);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        int numberOfColumns = 3;

        // use a linear layout manager
        layoutManager = new GridLayoutManager(this, numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new movieAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMovieData();
    }

    private void loadMovieData() {
        showMovieDataView();

        String searchBy = MoviePreferences.getSearchBy(this);
        Toast toast = Toast.makeText(this, searchBy, Toast.LENGTH_LONG);
        toast.show();
        new FetchMovieTask().execute(searchBy);
    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        //mErrorMessageDisplay.setVisibility(View.INVISIBLE);
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
//    private void showErrorMessage() {
//        /* First, hide the currently visible data */
//        recyclerView.setVisibility(View.INVISIBLE);
//        /* Then, show the error */
//        //mErrorMessageDisplay.setVisibility(View.VISIBLE);
//    }

    public class FetchMovieTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String searchBy = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(searchBy);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                String[] simpleJsonMovieData = TheMovieDatabaseJsonUtils
                        .getSimpleMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

                return simpleJsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieDataset) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieDataset != null) {
                showMovieDataView();
                //MyAdapter.setMovieData(movieDataset);
            } else {
                //showErrorMessage();
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

    public static class SortByAlphabetical implements Comparator<String>{
        // Used for sorting in ascending order of
        // roll name
        public int compare(String a, String b)
        {
            return a.compareTo(b);
        }
    }

    public static class SortByDescAlphabetical implements Comparator<String>{
        // Used for sorting in descending order of
        // roll name
        public int compare(String a, String b)
        {
            return b.compareTo(a);
        }
    }

    private void sortHelper(MenuItem item) {
        if (item.getTitle().equals("Descending")) {
            item.setTitle("Ascending");
            List<String> list = Arrays.asList(myDataset);
            Collections.sort(list, new SortByDescAlphabetical());
        } else {
            item.setTitle("Descending");
            List<String> list = Arrays.asList(myDataset);
            Collections.sort(list, new SortByAlphabetical());
        }

        mAdapter.notifyDataSetChanged();
    }
}
