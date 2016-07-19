package design.ivan.app.trakt.main;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import design.ivan.app.trakt.R;
import design.ivan.app.trakt.network.NetworkChangeReceiver;

/**
 * Created by ivanm on 7/19/16.
 */
public class MainPresenter implements IMainContract.ActionListener,
        NetworkChangeReceiver.NetworkChangeListener {

    IMainContract.MainView mainView;
    private NetworkChangeReceiver networkChangeReceiver;

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

    // +++ End ActionListener implementation +++//

    // *** NetworkChangeListener implementation *** //

    @Override
    public void onNetworkChange(boolean connected) {
        if(connected) {
            mainView.showSnackbar(R.string.online);
            //getRSSFeed(false);
        } else {
            mainView.showSnackbar(R.string.offline, true);
        }
    }

    // +++ End NetworkChangeListener implementation +++ //
}
