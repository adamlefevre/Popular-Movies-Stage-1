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
    String backdropUrl;
    String tagline;
    String title;
    String overview;
    Double voteAverage;
    String releaseDate;

    public Movie(Integer id, String posterUrl) {
        this.id = id;
        this.posterUrl = posterUrl;
    }

    public Movie(String poster, String backdrop, String tagline, String title, String overview, Double voteAverage, String releaseDate) {
        this.posterUrl = poster;
        this.backdropUrl = backdrop;
        this.tagline = tagline;
        this.title = title;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }
}
