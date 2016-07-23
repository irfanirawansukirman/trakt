package design.ivan.app.trakt.topmovie;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.util.SparseArray;

import design.ivan.app.trakt.model.Movie;

public interface ITopMoviesContract {
    interface TopMoviesView
    {
        void initListUi();
        void getVisiblePosition();
        void goToPostion();
        void showSnackbar(@StringRes int resMessage);
        void showSnackbar(@StringRes int resMessage, boolean alwaysOn);
        void hideSnackbar();
        void setProgressIndicator(boolean active);
        void hideMessage();
        void showMessage(@StringRes int message);
        void enableUI(boolean activate);
        void loadData(SparseArray<Movie> movieSparseArray);
        int adapterItemCount();
        void notifyItemInserted();
        void notifyItemRemoved();
        void setLoaded();
    }
    interface ActionListener
    {
        void setupListeners(Activity main);
        void clearListeners(Activity main);
        void getTopMovies(boolean forced);
        void loadMovies();
        void initConnection();
        void doWebRequest();
    }
}
