package design.ivan.app.trakt.main;

import android.app.Activity;
import android.support.annotation.StringRes;

import design.ivan.app.trakt.model.Movie;

public interface IMainContract {
    interface MainView
    {
        void showSnackbar(@StringRes int resMessage);
        void showSnackbar(@StringRes int resMessage, boolean alwaysOn);
        void showBottomSheet(Movie movie);
        void hideBottomSheet();
    }
    interface ActionListener
    {
        void setupListeners(Activity main);
        void clearListeners(Activity main);
        void registerNetworkHandler(MainPresenter.NetworkChangedHandler networkChangedHandler);
        void unregisterNetworkHandler();
    }

}
