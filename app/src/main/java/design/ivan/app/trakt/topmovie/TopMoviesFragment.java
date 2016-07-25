package design.ivan.app.trakt.topmovie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import design.ivan.app.trakt.R;
import design.ivan.app.trakt.model.Movie;
import design.ivan.app.trakt.repo.RepoLoader;


public class TopMoviesFragment extends Fragment implements TopMoviesAdapter.HandlerTopMoviesOnClick,
        ITopMoviesContract.TopMoviesView{

    private static final String TAG = "TopMoviesFragment";
    private static final String KEY_SCROLL = "trakt_scroll_position";
    private TopMoviesAdapter topMoviesAdapter;
    private Unbinder unbinder;
    private int scrollPosition;
    public TopMoviesPresenter actionListener;
    @BindView(R.id.top_movies_list)
    RecyclerView listTopMovies;
    @BindView(R.id.top_movies_message)
    TextView messageTopMovies;
    private Snackbar snackbar;
    View rootView;

    public static TopMoviesFragment newIstance()
    {
        return new TopMoviesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        rootView = inflater.inflate(R.layout.frag_top_movie, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            scrollPosition = savedInstanceState.getInt(KEY_SCROLL, 0);
        }
        actionListener = new TopMoviesPresenter(RepoLoader.loadMemMovieRepository(), this);
        initListUi();
        return rootView;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showMessage(R.string.no_data);
        actionListener.getTopMovies(false);
        actionListener.setupListeners(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        goToPostion();
    }

    @Override
    public void onStop() {
        super.onStop();
        actionListener.cancelWebRequest();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        actionListener.clearListeners(getActivity());
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getVisiblePosition();
        if(scrollPosition > 0)
            outState.putInt(KEY_SCROLL, scrollPosition );
        super.onSaveInstanceState(outState);
    }

    // *** HandlerTopMoviesOnClick implementation ***
    @Override
    public void OnClickItem(Integer id) {
        Log.d(TAG, "OnClickItem: id = " + id);
        actionListener.showInBottomSheet(id);
    }

    //*** TopMoviesView implementation ***

    @Override
    public void initListUi() {
        listTopMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        topMoviesAdapter = new TopMoviesAdapter(listTopMovies, this);
        listTopMovies.setAdapter(topMoviesAdapter);
        topMoviesAdapter.setOnLoadMoreListener(actionListener);
    }

    @Override
    public void getVisiblePosition() {
        if (listTopMovies.getLayoutManager() != null) {
            if(adapterItemCount() > 0)
            {
                scrollPosition = ((LinearLayoutManager) listTopMovies.getLayoutManager())
                        .findFirstCompletelyVisibleItemPosition();
            }
        }
    }

    @Override
    public void goToPostion() {
        if (listTopMovies.getLayoutManager() != null) {
            if (adapterItemCount() > 0 && scrollPosition > 0) {
                Log.d(TAG, "goToPostion: ");
                listTopMovies.scrollToPosition(scrollPosition);
            }
        }
    }

    @Override
    public void showSnackbar(@StringRes int resMessage) {
        showSnackbar(resMessage, false);
    }

    @Override
    public void showSnackbar(@StringRes int resMessage, boolean alwaysOn) {
        if(alwaysOn){
            snackbar = Snackbar.make(rootView, resMessage, Snackbar.LENGTH_INDEFINITE);
        } else {
            snackbar = Snackbar.make(rootView, resMessage, Snackbar.LENGTH_LONG);
        }
        snackbar.show();
    }

    @Override
    public void hideSnackbar() {
        if(snackbar == null)
            return;

        snackbar.dismiss();
    }

    @Override
    public void hideMessage() {
        enableUI(true);
    }

    @Override
    public void showMessage(@StringRes int message) {
        enableUI(false);
        messageTopMovies.setText(message);
    }

    @Override
    public void enableUI(boolean activate) {
        if(activate){
            messageTopMovies.setVisibility(View.GONE);
            listTopMovies.setVisibility(View.VISIBLE);
        } else {
            messageTopMovies.setVisibility(View.VISIBLE);
            listTopMovies.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadData(SparseArray<Movie> movieSparseArray) {
        if(topMoviesAdapter == null){
            Log.d(TAG, "loadData: adapter is not found");
            return;
        }
        hideMessage();
        topMoviesAdapter.loadDataSet(movieSparseArray);
    }

    @Override
    public int adapterItemCount() {
        if(topMoviesAdapter != null){
            return topMoviesAdapter.getItemCount();
        }
        return 0;
    }

    @Override
    public void notifyItemInserted() {
        topMoviesAdapter.notifyItemInserted(topMoviesAdapter.getItemCount() - 1);
    }

    @Override
    public void notifyItemRemoved() {
        topMoviesAdapter.notifyItemRemoved(topMoviesAdapter.getItemCount());
    }

    @Override
    public void setLoaded() {
        topMoviesAdapter.setLoaded();
    }

    // +++ End TopMoviesView implementation +++
}
