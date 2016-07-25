package design.ivan.app.trakt;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.util.ArrayMap;
import android.util.SparseArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import design.ivan.app.trakt.model.Movie;
import design.ivan.app.trakt.model.SearchResult;

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

    public static <T> SparseArray<T> prepareSparseArray(Context context, ArrayList<T> movieArrayList){
        T currentItem;
        Movie movie;
        SparseArray<T> sparseArray = new SparseArray<>();
        for (int i = 0; i < movieArrayList.size(); i++) {
            currentItem = movieArrayList.get(i);
            if(currentItem instanceof SearchResult){
                movie = ((SearchResult) currentItem).getMovie();
            } else {
                movie = (Movie) currentItem;
            }
            formatDate(context, movie);
            sparseArray.put(i, currentItem);
        }
        return sparseArray;
    }

    public static void formatDate(Context context, Movie movie) {
        if(movie.getReleased() == null)
            return;

        String dateFormatted = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd", Locale.US);
        Calendar forecastCal = Calendar.getInstance();
        try {
            forecastCal.setTime(sdf.parse(movie.getReleased()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //get year, day, month
        int year = forecastCal.get(Calendar.YEAR);
        int day = forecastCal.get(Calendar.DAY_OF_MONTH);
        String monthName = new SimpleDateFormat("MMM", Locale.US).format(forecastCal.getTime());

        dateFormatted = context.getString(R.string.format_date, monthName, day, year);
        movie.setReleased(dateFormatted);
    }
}
