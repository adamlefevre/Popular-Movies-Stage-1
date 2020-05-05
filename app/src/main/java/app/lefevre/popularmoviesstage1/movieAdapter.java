package app.lefevre.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class movieAdapter extends RecyclerView.Adapter<movieAdapter.posterViewHolder> {

    private String[] mMovieData;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class posterViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public posterViewHolder(View posterView) {
            super(posterView);
            textView = (TextView) posterView.findViewById(R.id.info_text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public movieAdapter(String[] myDataset) {
        mMovieData = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public posterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_posters;
        LayoutInflater inflater = LayoutInflater.from(context);

        // create a new view
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        posterViewHolder viewHolder = new posterViewHolder(view);

        //viewHolder.textView.setText("Movie: " + textView.getTitle());
        //...
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(posterViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mMovieData[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }

//    public void setMovieData(String[] movieData) {
//        mMovieData = movieData;
//        notifyDataSetChanged();
//    }
}
