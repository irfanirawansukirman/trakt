package design.ivan.app.trakt.search;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import design.ivan.app.trakt.R;
import design.ivan.app.trakt.model.Movie;
import design.ivan.app.trakt.model.SearchResult;
import design.ivan.app.trakt.topmovie.TopMoviesAdapter;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public interface HandlerSearchOnClick{
        void OnClickItem(Integer id);
    }
    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROG = 0;
    private int visibleThreshold = 2;
    private int totalItemCount;
    private int lastVisibleItem;
    private boolean loading;
    private TopMoviesAdapter.OnLoadMoreListener onLoadMoreListener;
    private HandlerSearchOnClick handlerSearchOnClick;
    private SparseArray<SearchResult> searchResultSparseArray;
    private String urlThumb;

    public SearchAdapter(RecyclerView recyclerView, HandlerSearchOnClick handlerSearchOnClick) {
        this.handlerSearchOnClick = handlerSearchOnClick;
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
        return searchResultSparseArray.valueAt(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        if(viewType == VIEW_ITEM) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_top_movie, viewGroup, false);
            viewHolder = new TopMoviesAdapter.TopMoviesViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_progress_bar, viewGroup, false);
            viewHolder = new TopMoviesAdapter.ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position)
    {
        if(holder.getItemViewType() == VIEW_ITEM){
            final TopMoviesAdapter.TopMoviesViewHolder viewHolder = (TopMoviesAdapter.TopMoviesViewHolder)holder;
            Movie movie = searchResultSparseArray.valueAt(position).getMovie();
            viewHolder.itemTitle.setText(movie.getTitle());
            viewHolder.itemReleased.setText(movie.getReleased());
            viewHolder.itemOverview.setText(movie.getOverview());
            urlThumb = movie.getImages().getPoster().getThumb();
            Glide.with(viewHolder.itemThumb.getContext())
                    .load(urlThumb)
                    .placeholder(ContextCompat.getDrawable(viewHolder.itemThumb.getContext(), R.drawable.ic_landscape_black_60dp))
                    .error(ContextCompat.getDrawable(viewHolder.itemThumb.getContext(), R.drawable.ic_filler_drawable_60dp))
                    .into(viewHolder.itemThumb);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    if(RecyclerView.NO_POSITION != position){
                        Movie movie = searchResultSparseArray.valueAt(position).getMovie();
                        handlerSearchOnClick.OnClickItem(movie.getIds().getTrakt());
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(searchResultSparseArray == null) return 0;
        return searchResultSparseArray.size();
    }

    public void loadDataSet(SparseArray<SearchResult> searchResult){
        this.searchResultSparseArray = searchResult;
        notifyDataSetChanged();
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(TopMoviesAdapter.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


}
