package app.lefevre.popularmoviesstage1.data;

import android.content.Context;

public class MoviePreferences {

    final static String apiKey = "";
    final static String endpoint = "popular";

    public static String getApiKey(Context context) {
        return apiKey;
    }

    public static String getEndpoint(Context context) {
        return endpoint;
    }

}
