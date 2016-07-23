package design.ivan.app.trakt.repo;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.SparseArray;

import design.ivan.app.trakt.Utility;
import design.ivan.app.trakt.model.SearchResult;

public class InMemorySearchService implements IMemService<SearchResult> {
    private static ArrayMap<Integer, SearchResult> MOVIE_SERVICE_DATA = new ArrayMap<>();

    @Override
    public void getAllSavedItems(MemServiceCallback<SparseArray<SearchResult>> callback) {
        callback.onLoaded(Utility.arrayMapToSparseArray(MOVIE_SERVICE_DATA));
    }

    @Override
    public void getItem(Integer itemId, MemServiceCallback<SearchResult> callback) {
        callback.onLoaded(MOVIE_SERVICE_DATA.get(itemId));
    }

    @Override
    public void saveItem(SearchResult item) {
        MOVIE_SERVICE_DATA.put(item.getMovie().getIds().getTrakt(), item);
    }

    @Override
    public void removeItem(SearchResult item) {

    }

    @Override
    public void saveItemsArray(@NonNull SparseArray<SearchResult> itemSparseArray, @NonNull SaveSparseArrayCallback callback) {
        SearchResult result;
        int counter = 0;
        for (int i = 0; i < itemSparseArray.size(); i++) {
            result = itemSparseArray.valueAt(i);
            MOVIE_SERVICE_DATA.put(result.getMovie().getIds().getTrakt(), result);
            counter++;
        }
        if(counter == itemSparseArray.size()){
            callback.savedSparseArray(true);
        } else{
            callback.savedSparseArray(false);
        }
    }
}
