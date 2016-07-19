package design.ivan.app.trakt;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ivanm on 7/19/16.
 */
public class Utility {
    public static boolean isAppOnline(Context context){
        //get network info
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        //send boolean if it is connected or not
        return  activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
