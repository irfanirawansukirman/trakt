package design.ivan.app.trakt.topmovie;

import android.app.Activity;
import android.util.Log;

import java.util.List;

import design.ivan.app.trakt.model.Movie;
import design.ivan.app.trakt.network.ITraktAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TopMoviesPresenter implements ITopMoviesContract.ActionListener, Callback<List<Movie>> {

    private static final String TAG = "TopMoviesPresenter";
    ITopMoviesContract.TopMoviesView topMoviesView;


    public TopMoviesPresenter(ITopMoviesContract.TopMoviesView topMoviesView) {
        this.topMoviesView = topMoviesView;
    }

    // *** ITopMoviesContract.ActionListener implementation ***

    @Override
    public void setupListeners(Activity main) {

    }

    @Override
    public void clearListeners(Activity main) {

    }

    @Override
    public void getTopMovies() {
        Log.d(TAG, "getTopMovies: ");
        ITraktAPIService traktAPIService = ITraktAPIService.retrofit.create(ITraktAPIService.class);
        Call<List<Movie>> call = traktAPIService.getTopMovies();
        call.enqueue(this);
    }

    // +++ End ITopMoviesContract.ActionListener implementation +++

    // *** Retrofit 2 callback implementation ***

    @Override
    public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
        Log.d(TAG, "onResponse: response = " + response.body());
    }

    @Override
    public void onFailure(Call<List<Movie>> call, Throwable t) {
        Log.d(TAG, "onFailure: t = " + t);
    }

    // +++ End Retrofit 2 callback implementation +++
}
