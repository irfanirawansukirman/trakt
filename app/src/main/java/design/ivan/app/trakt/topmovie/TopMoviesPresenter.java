package design.ivan.app.trakt.topmovie;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import design.ivan.app.trakt.R;
import design.ivan.app.trakt.Utility;
import design.ivan.app.trakt.main.MainActivity;
import design.ivan.app.trakt.main.MainPresenter;
import design.ivan.app.trakt.model.Movie;
import design.ivan.app.trakt.network.ITraktAPIService;
import design.ivan.app.trakt.repo.IMemRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TopMoviesPresenter implements ITopMoviesContract.ActionListener, Callback<List<Movie>>
        , MainPresenter.NetworkChangedHandler, TopMoviesAdapter.OnLoadMoreListener {

    private static final String TAG = "TopMoviesPresenter";
    ITopMoviesContract.TopMoviesView topMoviesView;
    IMemRepository<Movie> topMoviesRepo;
    private ITraktAPIService traktAPIService;
    private Call<List<Movie>> call;
    private int pageCounter = 0;

    public TopMoviesPresenter(IMemRepository<Movie> topMoviesRepo, ITopMoviesContract.TopMoviesView topMoviesView) {
        this.topMoviesView = topMoviesView;
        this.topMoviesRepo = topMoviesRepo;
    }

    // *** ITopMoviesContract.ActionListener implementation ***

    @Override
    public void setupListeners(Activity main) {
        ((MainActivity)main).getActionListener().registerNetworkHandler(this);
    }

    @Override
    public void clearListeners(Activity main) {
        ((MainActivity)main).getActionListener().unregisterNetworkHandler();
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
                //TODO load to recycler view adapter
                topMoviesView.loadData(itemsSparseArray);
            }
        });
    }

    @Override
    public void initConnection() {
        if(traktAPIService == null)
            traktAPIService = ITraktAPIService.retrofit.create(ITraktAPIService.class);
    }

    @Override
    public void doWebRequest() {
        topMoviesView.showSnackbar(R.string.syncing, true);
        call = traktAPIService.getTopMovies(pageCounter + 1);
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

            if(pageCounter == 0){
                //first time loading pages
                topMoviesRepo.saveArrayItem(movieSparseArray, new IMemRepository.SaveItemArrayCallback() {
                    @Override
                    public void onSavedArray(boolean saved) {
                        Log.d(TAG, "onSavedArray: Forecasts cached");
                        pageCounter += 1; //update page counter
                        loadMovies();
                    }
                });
                return;
            }

            topMoviesRepo.removeItem(topMoviesView.adapterItemCount() - 1);
            topMoviesView.notifyItemRemoved();
            //add items one by one
            for (int i = 0; i < 10; i++) {
                topMoviesRepo.saveItem(movieSparseArray.valueAt(i));
                topMoviesView.notifyItemInserted();
            }
            topMoviesView.setLoaded();

        }
    }

    @Override
    public void onFailure(Call<List<Movie>> call, Throwable t) {
        Log.d(TAG, "onFailure: t = " + t);
    }
    // +++ End Retrofit 2 callback implementation +++

    // *** MainPresenter.NetworkChangedHandler implementation +++
    @Override
    public void onNetworkChange(boolean connected) {
        getTopMovies(false);
    }

    // *** TopMoviesAdapter.OnLoadMoreListener implementation +++
    @Override
    public void onLoadMore() {
        topMoviesRepo.saveItem(null);
        topMoviesView.notifyItemInserted();
        getTopMovies(true);
    }
}
