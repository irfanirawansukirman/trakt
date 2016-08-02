package design.ivan.app.trakt.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import design.ivan.app.trakt.R;
import design.ivan.app.trakt.model.SearchResult;
import design.ivan.app.trakt.repo.RepoLoader;
import design.ivan.app.trakt.ui.VertialSpaceDecorator;

public class SearchFragment extends Fragment implements ISearchContract.SearchView
        , SearchAdapter.HandlerSearchOnClick{
    private static final String TAG = "SearchFragment";
    private static final String KEY_RESTARTING = "restarting_search";
    private boolean restarting = false;
    private Unbinder unbinder;
    private SearchPresenter actionListener;
    private SearchAdapter searchAdapter;
    @BindView(R.id.search_input)
    TextInputEditText searchInput;
    @BindView(R.id.search_recycler)
    RecyclerView searchList;
    private View rootView;
    private Snackbar snackbar;

    public static SearchFragment newInstance()
    {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        rootView = inflater.inflate(R.layout.frag_search, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            //scrollPosition = savedInstanceState.getInt(KEY_SCROLL, 0);
            restarting = savedInstanceState.getBoolean(KEY_RESTARTING, false);
        }
        actionListener = new SearchPresenter(RepoLoader.loadMemSearchRepository(), this);
        initListUi();
        searchInput.addTextChangedListener(actionListener);
        actionListener.setupSearchRequest();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
        searchInput.removeTextChangedListener(actionListener);
        actionListener.clearSearchRequest();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        restarting = true;
        outState.putBoolean(KEY_RESTARTING, restarting);
        super.onSaveInstanceState(outState);
    }

    // *** SearchView implementation ***

    @Override
    public void initListUi() {
        searchList.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchList.addItemDecoration(new VertialSpaceDecorator(15));
        searchAdapter = new SearchAdapter(searchList, this);
        searchList.setAdapter(searchAdapter);
        searchAdapter.setOnLoadMoreListener(actionListener);
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
    public void loadData(SparseArray<SearchResult> searchSparse) {
        if(searchAdapter == null){
            Log.d(TAG, "loadData: adapter is not found");
            return;
        }
        searchAdapter.loadDataSet(searchSparse);
    }

    @Override
    public int adapterItemCount() {
        if(searchAdapter != null){
            return searchAdapter.getItemCount();
        }
        return 0;
    }

    @Override
    public void notifyItemInserted() {
        searchAdapter.notifyItemInserted(searchAdapter.getItemCount() - 1);
    }

    @Override
    public void notifyItemRemoved() {
        searchAdapter.notifyItemRemoved(searchAdapter.getItemCount());
    }

    @Override
    public void setLoaded() {
        searchAdapter.setLoaded();
    }

    @Override
    public boolean isRestarting() {
        return restarting;
    }

    @Override
    public void clearRestarting() {
        restarting = false;
    }

    // +++ End SearchView implementation +++

    // *** SearchAdapter.HandlerSearchOnClick impelementation +++
    @Override
    public void OnClickItem(Integer id) {
        Log.d(TAG, "OnClickItem: id = " + id);
        actionListener.showInBottomSheet(id);
    }


}
