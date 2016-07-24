package design.ivan.app.trakt.search;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.util.SparseArray;

import design.ivan.app.trakt.model.SearchResult;

public interface ISearchContract {
    interface SearchView{
        void initListUi();
        void showSnackbar(@StringRes int resMessage);
        void showSnackbar(@StringRes int resMessage, boolean alwaysOn);
        void hideSnackbar();
        void setProgressIndicator(boolean active);
        void hideMessage();
        void showMessage(@StringRes int message);
        void enableUI(boolean activate);
        void loadData(SparseArray<SearchResult> searchSparse);
        int adapterItemCount();
    }
    interface ActionListener{
        void setupListeners(Activity main);
        void clearListeners(Activity main);
        void setupSearchRequest();
        void clearSearchRequest();
        void loadSearch();
        void doWebSearch(String searchString);
        void cancelWebRequest();
    }
}
