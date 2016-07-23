package design.ivan.app.trakt.main;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import design.ivan.app.trakt.R;
import design.ivan.app.trakt.network.NetworkChangeReceiver;

public class MainPresenter implements IMainContract.ActionListener,
        NetworkChangeReceiver.NetworkChangeListener {
    public interface NetworkChangedHandler{
        void onNetworkChange(boolean connected);
    }
    IMainContract.MainView mainView;
    private NetworkChangeReceiver networkChangeReceiver;
    private static final String TAG = "MainPresenter";
    private NetworkChangedHandler networkChangedHandler;

    public MainPresenter(IMainContract.MainView mainView) {
        this.mainView = mainView;
    }

    // *** ActionListener implementation *** //

    @Override
    public void setupListeners(Activity main) {
        //start listening for network changes
        if(networkChangeReceiver == null){
            networkChangeReceiver = new NetworkChangeReceiver();
            networkChangeReceiver.addListener(this);
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
            main.registerReceiver(networkChangeReceiver, filter);
        }

    }

    @Override
    public void clearListeners(Activity main) {
        //stop listening for network changes
        if(networkChangeReceiver != null){
            main.unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
    }

    @Override
    public void registerNetworkHandler(NetworkChangedHandler networkChangedHandler) {
        this.networkChangedHandler = networkChangedHandler;
    }

    @Override
    public void unregisterNetworkHandler() {
        networkChangedHandler = null;
    }

    // +++ End ActionListener implementation +++//

    // *** NetworkChangeListener implementation *** //

    @Override
    public void onNetworkChange(boolean connected) {
        if(connected) {
            mainView.showSnackbar(R.string.online);
            //TODO tell fragment is time to search for movies if it is necessary
            if(networkChangedHandler != null)networkChangedHandler.onNetworkChange(connected);
        } else {
            mainView.showSnackbar(R.string.offline, true);
        }
    }

    // +++ End NetworkChangeListener implementation +++ //
}
