package design.ivan.app.trakt.search;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import design.ivan.app.trakt.R;
import design.ivan.app.trakt.Utility;
import design.ivan.app.trakt.model.SearchResult;
import design.ivan.app.trakt.network.ITraktAPIService;
import design.ivan.app.trakt.repo.IMemRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter implements ISearchContract.ActionListener,
        TextWatcher, Callback<List<SearchResult>> {

    private static final String TAG = "SearchPresenter";
    private static final String EXTENDED_FULL = "images,full";
    ISearchContract.SearchView searchView;
    private ITraktAPIService traktAPIService;
    private Call<List<SearchResult>> call;
    private IMemRepository<SearchResult> searchRepo;

    public SearchPresenter(IMemRepository<SearchResult> searchRepo, ISearchContract.SearchView searchView) {
        this.searchRepo = searchRepo;
        this.searchView = searchView;
    }

    @Override
    public void setupListeners(Activity main) {

    }

    @Override
    public void clearListeners(Activity main) {

    }

    @Override
    public void setupSearchRequest() {
        traktAPIService = ITraktAPIService.retrofit.create(ITraktAPIService.class);
    }

    @Override
    public void clearSearchRequest() {
        if(call != null)
            call.cancel();
        call = null;
        traktAPIService = null;
    }

    @Override
    public void loadSearch() {
        searchRepo.getItemList(new IMemRepository.LoadItemsCallback<SearchResult>(){
            @Override
            public void onItemsLoaded(SparseArray<SearchResult> itemsSparseArray) {
                Log.d(TAG, "onItemsLoaded: " + itemsSparseArray);
                searchView.loadData(itemsSparseArray);
            }
        });
    }

    @Override
    public void doWebSearch(String searchString) {
        if(call != null)
            call.cancel();
        //TODO show something on UI that we are doing a search
        searchView.showSnackbar(R.string.searching, true);
        call = traktAPIService.searchMovie(searchString);
        call.enqueue(this);
    }

    @Override
    public void cancelWebRequest() {

    }

    // *** TextWatcher implementation ***

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String editString = editable.toString();
        if(editString.isEmpty())
            return;
        doWebSearch(editable.toString());
    }

    // +++ End TextWatcher implementation +++

    // *** Retrofit callback implementation ***

    @Override
    public void onResponse(Call<List<SearchResult>> call, Response<List<SearchResult>> response) {
        Log.d(TAG, "onResponse: response = " + response.body());
        ((Fragment)searchView).getActivity().runOnUiThread(new Runnable() {
            public void run() {
                searchView.hideSnackbar();
            }
        });
        if(response.isSuccessful()){
            ArrayList<SearchResult> searchResults = (ArrayList<SearchResult>) response.body();
            if(searchResults.size()<=0)
                return;
            SparseArray<SearchResult> searchResultSparseArray = Utility.prepareSparseArray(searchResults);
            searchRepo.saveArrayItem(searchResultSparseArray, new IMemRepository.SaveItemArrayCallback() {
                @Override
                public void onSavedArray(boolean saved) {
                    Log.d(TAG, "onSavedArray: Forecasts cached");
                    loadSearch();
                }
            });
        }
    }

    @Override
    public void onFailure(Call<List<SearchResult>> call, Throwable t) {
        Log.d(TAG, "onFailure: t = " + t);
        ((Fragment)searchView).getActivity().runOnUiThread(new Runnable() {
            public void run() {
                searchView.showSnackbar(R.string.something_went_wrong);
            }
        });
    }

    // +++ End Retrofit callback implementation +++
}
