package app.lefevre.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.posterViewHolder> {

    private Movie[] mMovieData;

    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movieDetails);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) { mClickHandler = clickHandler; }

    public class posterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final ImageView mPosterImageView;

        public posterViewHolder(View view) {
            super(view);

            mPosterImageView = (ImageView) view.findViewById(R.id.movie_poster);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovieData[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public posterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_posters;
        LayoutInflater inflater = LayoutInflater.from(context);

        // create a new view
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new posterViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(posterViewHolder holder, int position) {
        Movie singleMovie = mMovieData[position];

        Picasso.get().load("http://image.tmdb.org/t/p/w185" + singleMovie.posterUrl).into(holder.mPosterImageView);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }

    public void setMovieData(Movie[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

}
