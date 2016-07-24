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
import design.ivan.app.trakt.topmovie.TopMoviesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter implements ISearchContract.ActionListener,
        TextWatcher, Callback<List<SearchResult>>, TopMoviesAdapter.OnLoadMoreListener {

    private static final String TAG = "SearchPresenter";
    ISearchContract.SearchView searchView;
    private ITraktAPIService traktAPIService;
    private Call<List<SearchResult>> call;
    private IMemRepository<SearchResult> searchRepo;
    private int pageCounter = 0;
    private String stringToSearch;

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
    public void doWebSearch(String searchString, boolean isNewSearch) {
        if(call != null)
            call.cancel();

        searchView.showSnackbar(R.string.searching, true);

        if(isNewSearch)
            pageCounter = 0;

        call = traktAPIService.searchMovie(searchString, pageCounter + 1);
        call.enqueue(this);
    }

    @Override
    public void doWebSearch(String searchString) {
        doWebSearch(searchString, false);
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
        stringToSearch = editable.toString();
        if(stringToSearch.isEmpty())
            return;
        doWebSearch(stringToSearch, true);
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

            if(pageCounter == 0){
                //first time loading pages
                searchRepo.saveArrayItem(searchResultSparseArray, new IMemRepository.SaveItemArrayCallback() {
                    @Override
                    public void onSavedArray(boolean saved) {
                        Log.d(TAG, "onSavedArray: searches cached");
                        loadSearch();
                    }
                });
            } else {
                searchRepo.removeItem(searchView.adapterItemCount() - 1);
                searchView.notifyItemRemoved();
                //add items one by one
                for (int i = 0; i < 10; i++) {
                    searchRepo.saveItem(searchResultSparseArray.valueAt(i));
                    searchView.notifyItemInserted();
                }
                searchView.setLoaded();
            }

            pageCounter += 1;
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

    @Override
    public void onLoadMore() {
        searchRepo.saveItem(null);
        searchView.notifyItemInserted();
        doWebSearch(stringToSearch);
    }

    // +++ End Retrofit callback implementation +++
}
