package app.lefevre.popularmoviesstage1.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class TheMovieDatabaseJsonUtils {

    public static String[] getSimpleMovieStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String TMDB_RESULTS = "results";

        final String TMDB_TITLE = "title";

        final String TMDB_MESSAGE_CODE = "An error occurred. Please wait and try again later.";

        String[] parsedMovieData = null;

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

        parsedMovieData = new String[movieArray.length()];

        for (int i=0; i < movieArray.length(); i++) {
            String title;

            JSONObject movie = movieArray.getJSONObject(i);

            title = movie.optString(TMDB_TITLE);

            parsedMovieData[i] = title;
        }

        return parsedMovieData;

    }
}
