package design.ivan.app.trakt.topmovie;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import design.ivan.app.trakt.R;
import design.ivan.app.trakt.model.Movie;


public class TopMoviesAdapter extends RecyclerView.Adapter<TopMoviesAdapter.TopMoviesViewHolder>{
    public static interface HandlerTopMoviesOnClick{
        void OnClickItem(String id);
    }

    private SparseArray<Movie> movieSparseArray;
    private HandlerTopMoviesOnClick handlerOnClick;
    private String urlThumb;
    public TopMoviesAdapter(HandlerTopMoviesOnClick handler) {
        handlerOnClick = handler;
    }

    @Override
    public TopMoviesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        if ( viewGroup instanceof RecyclerView )
        {
            int layoutId = R.layout.item_top_movie;
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            return new TopMoviesViewHolder(view);
        }
        throw  new RuntimeException("Something went wrong creating View Holder");
    }

    @Override
    public void onBindViewHolder(TopMoviesViewHolder holder, int position)
    {
        Movie movie = movieSparseArray.valueAt(position);
        holder.itemTitle.setText(movie.getTitle());
        holder.itemYear.setText(movie.getYear().toString());
        holder.itemReleased.setText(movie.getReleased());
        urlThumb = movie.getImages().getPoster().getThumb();
        Glide.with(holder.itemThumb.getContext())
                .load(urlThumb)
                .error(ContextCompat.getDrawable(holder.itemThumb.getContext(), R.drawable.ic_filler_drawable_60dp))
                .into(holder.itemThumb);

    }

    public void loadDataSet(SparseArray<Movie> movieSparseArray)
    {
        this.movieSparseArray = movieSparseArray;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        if(movieSparseArray == null) return 0;
        return movieSparseArray.size();
    }

    public class TopMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.top_movie_item_thumb)
        ImageView itemThumb;
        @BindView(R.id.top_movie_item_title)
        TextView itemTitle;
        @BindView(R.id.top_movie_item_year)
        TextView itemYear;
        @BindView(R.id.top_movie_item_released)
        TextView itemReleased;
        public TopMoviesViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
