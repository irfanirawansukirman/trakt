package design.ivan.app.trakt.search;

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
import design.ivan.app.trakt.model.SearchResult;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{
    public interface HandlerSearchOnClick{
        void OnClickItem(String id);
    }

    private HandlerSearchOnClick handlerSearchOnClick;
    private SparseArray<SearchResult> searchResultSparseArray;
    private String urlThumb;

    public SearchAdapter(HandlerSearchOnClick handlerSearchOnClick) {
        this.handlerSearchOnClick = handlerSearchOnClick;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_top_movie, viewGroup, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder viewHolder, int position) {
        Movie movie = searchResultSparseArray.valueAt(position).getMovie();
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

    @Override
    public int getItemCount() {
        if(searchResultSparseArray == null) return 0;
        return searchResultSparseArray.size();
    }

    public void loadDataSet(SparseArray<SearchResult> searchResult){
        this.searchResultSparseArray = searchResult;
        notifyDataSetChanged();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.top_movie_item_thumb)
        ImageView itemThumb;
        @BindView(R.id.top_movie_item_title)
        TextView itemTitle;
        @BindView(R.id.top_movie_item_year)
        TextView itemYear;
        @BindView(R.id.top_movie_item_released)
        TextView itemReleased;
        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
