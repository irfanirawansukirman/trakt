package design.ivan.app.trakt.topmovie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import design.ivan.app.trakt.R;


public class TopMoviesFragment extends Fragment implements TopMoviesAdapter.HandlerTopMoviesOnClick,
        ITopMoviesContract.TopMoviesView{

    private static final String TAG = "TopMoviesFragment";
    private TopMoviesAdapter topMoviesAdapter;
    private Unbinder unbinder;
    TopMoviesPresenter actionListener;
    @BindView(R.id.top_movies_list)
    RecyclerView listTopMovies;
    @BindView(R.id.top_movies_message)
    TextView messageTopMovies;

    public static TopMoviesFragment newIstance()
    {
        return new TopMoviesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View rootView = inflater.inflate(R.layout.frag_top_movie, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        listTopMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        topMoviesAdapter = new TopMoviesAdapter(this);
        listTopMovies.setAdapter(topMoviesAdapter);
        actionListener = new TopMoviesPresenter(this);
        showMessage(R.string.no_data);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
        unbinder.unbind();
    }

    // *** HandlerTopMoviesOnClick implementation ***
    @Override
    public void OnClickItem(String id) {

    }

    //*** TopMoviesView implementation ***

    @Override
    public void showSnackbar(@StringRes int resMessage) {

    }

    @Override
    public void showSnackbar(@StringRes int resMessage, boolean alwaysOn) {

    }

    @Override
    public void hideSnackbar() {

    }

    @Override
    public void setProgressIndicator(boolean active) {

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

    // +++ End TopMoviesView implementation +++
}
