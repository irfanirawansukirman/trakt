package design.ivan.app.trakt.search;

import android.app.Activity;
import android.support.annotation.StringRes;

/**
 * Created by ivanm on 7/20/16.
 */
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
    }
    interface ActionListener{
        void setupListeners(Activity main);
        void clearListeners(Activity main);
        void setupSearchRequest();
        void clearSearchRequest();
        void doSearch(String searchString);
    }
}
