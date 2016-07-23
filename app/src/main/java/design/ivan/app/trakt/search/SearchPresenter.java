package design.ivan.app.trakt.search;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.util.List;

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
    public void doSearch(String searchString) {
        //Log.d(TAG, "doSearch: ");
        if(call != null)
            call.cancel();
        call = traktAPIService.searchMovie(searchString);
        call.enqueue(this);


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
        doSearch(editable.toString());
    }

    // +++ End TextWatcher implementation +++

    // *** Retrofit callback implementation ***

    @Override
    public void onResponse(Call<List<SearchResult>> call, Response<List<SearchResult>> response) {
        Log.d(TAG, "onResponse: response = " + response.body());
    }

    @Override
    public void onFailure(Call<List<SearchResult>> call, Throwable t) {
        Log.d(TAG, "onFailure: t = " + t);
    }

    // +++ End Retrofit callback implementation +++
}
