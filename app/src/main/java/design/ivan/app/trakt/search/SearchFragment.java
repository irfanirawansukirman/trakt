package design.ivan.app.trakt.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import design.ivan.app.trakt.R;

public class SearchFragment extends Fragment implements ISearchContract.SearchView{

    private static final String TAG = "SearchFragment";
    private Unbinder unbinder;
    private SearchPresenter actionListener;
    @BindView(R.id.search_input)
    TextInputEditText searchInput;

    public static SearchFragment newInstance()
    {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View rootView = inflater.inflate(R.layout.frag_search, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        //initListUi();
        actionListener = new SearchPresenter(this);
        searchInput.addTextChangedListener(actionListener);
        actionListener.setupSearchRequest();
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
        searchInput.removeTextChangedListener(actionListener);
        actionListener.clearSearchRequest();
        unbinder.unbind();
    }

    // *** SearchView implementation ***

    @Override
    public void initListUi() {

    }

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

    }

    @Override
    public void showMessage(@StringRes int message) {

    }

    @Override
    public void enableUI(boolean activate) {

    }

    // +++ End SearchView implementation +++

}
