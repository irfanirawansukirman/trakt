package design.ivan.app.trakt.search;

import android.support.annotation.StringRes;
import android.util.SparseArray;

import design.ivan.app.trakt.model.SearchResult;

public interface ISearchContract {
    interface SearchView{
        void initListUi();
        void showSnackbar(@StringRes int resMessage);
        void showSnackbar(@StringRes int resMessage, boolean alwaysOn);
        void hideSnackbar();
        void loadData(SparseArray<SearchResult> searchSparse);
        int adapterItemCount();
        void notifyItemInserted();
        void notifyItemRemoved();
        void setLoaded();
        boolean isRestarting();
        void clearRestarting();
    }
    interface ActionListener
    {
        void setupSearchRequest();
        void clearSearchRequest();
        void loadSearch();
        void doWebSearch(String searchString, boolean isNewSearch);
        void doWebSearch(String searchString);
        void showInBottomSheet(Integer itemId);
    }
}
