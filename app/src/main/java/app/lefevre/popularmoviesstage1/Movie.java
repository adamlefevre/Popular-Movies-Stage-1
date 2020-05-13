package app.lefevre.popularmoviesstage1;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

public class Movie implements Serializable {
    //initially pass the id and poster image to the object
    //Picasso to render the image
    //Pass the id to the click handler
    //Second activity can make another API call to get additional details
    Integer id;
    String posterUrl;

    String title;
    String posterThumbnail;
    String overview;
    Float voteAverage;
    Date releaseDate;

    public Movie() {

    }

    public Movie(Integer id, String posterUrl) {
        this.id = id;
        this.posterUrl = posterUrl;
    }
}
