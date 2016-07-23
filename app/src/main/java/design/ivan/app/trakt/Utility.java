package design.ivan.app.trakt;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.util.ArrayMap;
import android.util.SparseArray;

import java.util.ArrayList;

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

    public static <T> SparseArray<T> arrayMapToSparseArray(ArrayMap<Integer, T> incomingData){
        SparseArray<T> outputSparseArray = new SparseArray<>();
        T item;
        //faster for loop iteration if we stick to regular for loop
        for (int i = 0; i < incomingData.size(); i++) {
            item = incomingData.valueAt(i);
            outputSparseArray.append(i, item);
        }
        return outputSparseArray;
    }

    public static <T> SparseArray<T> prepareSparseArray(ArrayList<T> movieArrayList){
        T currentItem;
        SparseArray<T> sparseArray = new SparseArray<>();
        for (int i = 0; i < movieArrayList.size(); i++) {
            currentItem = movieArrayList.get(i);
            sparseArray.put(i, currentItem);
        }
        return sparseArray;
    }
}
