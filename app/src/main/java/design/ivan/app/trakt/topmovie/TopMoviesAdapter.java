package design.ivan.app.trakt.topmovie;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
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


public class TopMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public interface OnLoadMoreListener {
        void onLoadMore();
    }
    public interface HandlerTopMoviesOnClick{
        void OnClickItem(String id);
    }
    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROG = 0;
    private int visibleThreshold = 2;
    private int totalItemCount;
    private int lastVisibleItem;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private SparseArray<Movie> movieSparseArray;
    private HandlerTopMoviesOnClick handlerOnClick;
    private String urlThumb;

    public TopMoviesAdapter(RecyclerView recyclerView, HandlerTopMoviesOnClick handler) {
        handlerOnClick = handler;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return movieSparseArray.valueAt(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View view;
        RecyclerView.ViewHolder viewHolder;
        if(viewType == VIEW_ITEM) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_top_movie, viewGroup, false);
            viewHolder = new TopMoviesViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_progress_bar, viewGroup, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(holder.getItemViewType() == VIEW_ITEM){
            TopMoviesViewHolder viewHolder = (TopMoviesViewHolder)holder;
            Movie movie = movieSparseArray.valueAt(position);
            viewHolder.itemTitle.setText(movie.getTitle());
            if(movie.getYear() != null)viewHolder.itemYear.setText(movie.getYear().toString());
            viewHolder.itemReleased.setText(movie.getReleased());
            urlThumb = movie.getImages().getPoster().getThumb();
            Glide.with(viewHolder.itemThumb.getContext())
                    .load(urlThumb)
                    .placeholder(ContextCompat.getDrawable(viewHolder.itemThumb.getContext(), R.drawable.ic_landscape_black_60dp))
                    .error(ContextCompat.getDrawable(viewHolder.itemThumb.getContext(), R.drawable.ic_filler_drawable_60dp))
                    .into(viewHolder.itemThumb);
        }
    }

    @Override
    public int getItemCount()
    {
        if(movieSparseArray == null) return 0;
        return movieSparseArray.size();
    }

    public void loadDataSet(SparseArray<Movie> movieSparseArray)
    {
        this.movieSparseArray = movieSparseArray;
        notifyDataSetChanged();
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class TopMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.top_movie_item_thumb)
        public ImageView itemThumb;
        @BindView(R.id.top_movie_item_title)
        public TextView itemTitle;
        @BindView(R.id.top_movie_item_year)
        public TextView itemYear;
        @BindView(R.id.top_movie_item_released)
        public TextView itemReleased;
        public TopMoviesViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        //public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
        }
    }
}
