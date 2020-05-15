package app.lefevre.popularmoviesstage1.data;

import android.content.Context;

public class MoviePreferences {

    final static String apiKey = "";
    final static String searchBy = "popularity.desc";

    public static String getApiKey(Context context) {
        return apiKey;
    }

    public static String getSearchBy(Context context) {
        return searchBy;
    }

}
