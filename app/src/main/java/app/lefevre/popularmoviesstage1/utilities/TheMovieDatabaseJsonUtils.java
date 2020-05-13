package app.lefevre.popularmoviesstage1.utilities;

import android.content.Context;

import app.lefevre.popularmoviesstage1.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class TheMovieDatabaseJsonUtils {

    public static Movie[] getMovieFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String TMDB_RESULTS = "results";

        final String TMDB_ID = "id";
        final String TMDB_TITLE = "title";
        final String TMDB_POSTER = "poster_path";

        final String TMDB_MESSAGE_CODE = "An error occurred. Please wait and try again later.";

        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        if (movieJson.has(TMDB_MESSAGE_CODE)) {
            int errorCode = movieJson.getInt(TMDB_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray movieArray = movieJson.getJSONArray(TMDB_RESULTS);

        Movie[] movieListFromJSON = new Movie[movieArray.length()];

        for (int i=0; i < movieArray.length(); i++) {
            Integer id;
            String poster;

            JSONObject movie = movieArray.getJSONObject(i);

            id = movie.optInt(TMDB_ID);
            poster = movie.optString(TMDB_POSTER);

            movieListFromJSON[i] = new Movie(id, poster);

        }

        return movieListFromJSON;

    }
}
