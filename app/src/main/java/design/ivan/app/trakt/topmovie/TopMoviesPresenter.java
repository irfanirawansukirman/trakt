package design.ivan.app.trakt.topmovie;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import design.ivan.app.trakt.R;
import design.ivan.app.trakt.Utility;
import design.ivan.app.trakt.model.Movie;
import design.ivan.app.trakt.network.ITraktAPIService;
import design.ivan.app.trakt.repo.IMemRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TopMoviesPresenter implements ITopMoviesContract.ActionListener, Callback<List<Movie>> {

    private static final String TAG = "TopMoviesPresenter";
    ITopMoviesContract.TopMoviesView topMoviesView;
    IMemRepository<Movie> topMoviesRepo;
    private ITraktAPIService traktAPIService;
    private Call<List<Movie>> call;

    public TopMoviesPresenter(IMemRepository<Movie> topMoviesRepo, ITopMoviesContract.TopMoviesView topMoviesView) {
        this.topMoviesView = topMoviesView;
        this.topMoviesRepo = topMoviesRepo;
    }

    // *** ITopMoviesContract.ActionListener implementation ***

    @Override
    public void setupListeners(Activity main) {

    }

    @Override
    public void clearListeners(Activity main) {

    }

    @Override
    public void getTopMovies(boolean forced) {
        Log.d(TAG, "getTopMovies: ");
        if(!forced){
            if(topMoviesRepo.arrayItemCount() > 0){
                loadMovies();
                return;
            }
        }

        if(Utility.isAppOnline(((Fragment)topMoviesView).getActivity())){
            initConnection();
            doWebRequest();
        } else {
            topMoviesView.showSnackbar(R.string.offline, true);
        }
    }

    @Override
    public void loadMovies() {
        topMoviesRepo.getItemList(new IMemRepository.LoadItemsCallback<Movie>() {
            @Override
            public void onItemsLoaded(SparseArray<Movie> itemsSparseArray) {
                Log.d(TAG, "onItemsLoaded: " + itemsSparseArray);
            }
        });
    }

    @Override
    public void initConnection() {
        traktAPIService = ITraktAPIService.retrofit.create(ITraktAPIService.class);
    }

    @Override
    public void doWebRequest() {
        topMoviesView.showSnackbar(R.string.syncing, true);
        call = traktAPIService.getTopMovies();
        call.enqueue(this);
    }

    // +++ End ITopMoviesContract.ActionListener implementation +++

    // *** Retrofit 2 callback implementation ***

    @Override
    public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
        Log.d(TAG, "onResponse: response = " + response.body());
        ((Fragment)topMoviesView).getActivity().runOnUiThread(new Runnable() {
            public void run() {
                topMoviesView.hideSnackbar();
            }
        });

        if(response.isSuccessful()){
            ArrayList<Movie> forecastList = (ArrayList<Movie>) response.body();
            if(forecastList.size()<=0)
                return;
            SparseArray<Movie> movieSparseArray = Utility.prepareSparseArray(forecastList);

            topMoviesRepo.saveArrayItem(movieSparseArray, new IMemRepository.SaveItemArrayCallback() {
                @Override
                public void onSavedArray(boolean saved) {
                    Log.d(TAG, "onSavedArray: Forecasts cached");
                    loadMovies();
                }
            });
        }
    }

    @Override
    public void onFailure(Call<List<Movie>> call, Throwable t) {
        Log.d(TAG, "onFailure: t = " + t);
    }

    // +++ End Retrofit 2 callback implementation +++
}
