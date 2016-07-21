package design.ivan.app.trakt.repo;

import android.support.annotation.NonNull;
import android.util.SparseArray;

public interface IMemService<T>{
    interface MemServiceCallback<T>{
        void onLoaded(T forecast);
    }

    interface SaveSparseArrayCallback{
        void savedSparseArray(boolean saved);
    }

    void getAllSavedItems(MemServiceCallback<SparseArray<T>> callback);

    void getItem(Integer itemId, MemServiceCallback<T> callback);

    void saveItem(T item);

    void saveItemsArray(@NonNull SparseArray<T> itemSparseArray,
                           @NonNull SaveSparseArrayCallback callback);
}
