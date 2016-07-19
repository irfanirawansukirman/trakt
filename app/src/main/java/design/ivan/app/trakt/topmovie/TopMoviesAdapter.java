package design.ivan.app.trakt.topmovie;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ivanm on 7/19/16.
 */
public class TopMoviesAdapter extends RecyclerView.Adapter<TopMoviesAdapter.TopMoviesViewHolder>{
    public static interface HandlerTopMoviesOnClick{
        void OnClickItem(String id);
    }

    private HandlerTopMoviesOnClick handlerOnClick;

    public TopMoviesAdapter(HandlerTopMoviesOnClick handler) {
        handlerOnClick = handler;
    }

    @Override
    public TopMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TopMoviesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class TopMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TopMoviesViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
