package design.ivan.app.trakt.repo;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.SparseArray;

import design.ivan.app.trakt.Utility;
import design.ivan.app.trakt.model.Movie;

/**
 * Created by ivanm on 7/21/16.
 */
public class InMemoryMoviesService implements IMemService<Movie> {

    private static ArrayMap<Integer, Movie> MOVIE_SERVICE_DATA = new ArrayMap<>();


    @Override
    public void getAllSavedItems(MemServiceCallback<SparseArray<Movie>> callback) {
        callback.onLoaded(Utility.arrayMapToSparseArray(MOVIE_SERVICE_DATA));
    }

    @Override
    public void getItem(Integer itemId, MemServiceCallback<Movie> callback) {
        callback.onLoaded(MOVIE_SERVICE_DATA.get(itemId));
    }

    @Override
    public void saveItem(Movie item) {
        MOVIE_SERVICE_DATA.put(item.getIds().getTrakt(), item);
    }

    @Override
    public void saveItemsArray(@NonNull SparseArray<Movie> itemSparseArray, @NonNull SaveSparseArrayCallback callback) {
        Movie movie;
        int counter = 0;
        for (int i = 0; i < itemSparseArray.size(); i++) {
            movie = itemSparseArray.valueAt(i);
            MOVIE_SERVICE_DATA.put(movie.getIds().getTrakt(), movie);
            counter++;
        }
        if(counter == itemSparseArray.size()){
            callback.savedSparseArray(true);
        } else{
            callback.savedSparseArray(false);
        }
    }
}
