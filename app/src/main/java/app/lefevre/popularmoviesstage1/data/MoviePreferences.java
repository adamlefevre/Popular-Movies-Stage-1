package app.lefevre.popularmoviesstage1.data;

import android.content.Context;

public class MoviePreferences {

    final static String apiKey = "041c6eed18b182419301192512ecd44c";
    final static String searchBy = "popular";

    public static String getApiKey(Context context) {
        return apiKey;
    }

    public static String getSearchBy(Context context) {
        return searchBy;
    }

}
