package design.ivan.app.trakt.main;

import android.app.Activity;
import android.support.annotation.StringRes;

public interface IMainContract {
    interface MainView
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
        void registerNetworkHandler(MainPresenter.NetworkChangedHandler networkChangedHandler);
        void unregisterNetworkHandler();
    }

}
