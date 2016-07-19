package design.ivan.app.trakt.topmovie;

import android.app.Activity;
import android.support.annotation.StringRes;

public interface ITopMoviesContract {
    interface TopMoviesView
    {
        void showSnackbar(@StringRes int resMessage);
        void showSnackbar(@StringRes int resMessage, boolean alwaysOn);
        void hideSnackbar();
        void setProgressIndicator(boolean active);
        void hideMessage();
        void showMessage(@StringRes int message);
        void enableUI(boolean activate);
    }
    interface ActionListener
    {
        void setupListeners(Activity main);
        void clearListeners(Activity main);
    }
}
