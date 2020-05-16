package app.lefevre.popularmoviesstage1.data;

import android.content.Context;

public class MoviePreferences {

    final static String apiKey = "";
    final static String sortBy = "popularity.desc";

    public static String getApiKey(Context context) {
        return apiKey;
    }

    public static String getSortBy(Context context) {
        return sortBy;
    }

}
