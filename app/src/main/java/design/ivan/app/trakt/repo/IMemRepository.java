package design.ivan.app.trakt.repo;

import android.support.annotation.NonNull;
import android.util.SparseArray;

public interface IMemRepository<T> {
    interface LoadItemsCallback<T> {
        void onItemsLoaded(SparseArray<T> itemsSparseArray);
    }

    /**
     * Used to return back one item from the MemService api object.
     */
    interface GetItemCallback<T> {
        void onItemLoaded(T item);
    }

    /**
     * Callback that returns a boolean that confirms if data was saved or not.
     */
    interface SaveItemArrayCallback {
        void onSavedArray(boolean saved);
    }

    void getItemList(@NonNull LoadItemsCallback<T> callback);
    void getItem(@NonNull Integer itemId, @NonNull GetItemCallback<T> callback);
    void saveItem(T item);
    void removeItem(int position);
    void saveArrayItem(@NonNull SparseArray<T> itemSparseArray, @NonNull SaveItemArrayCallback callback);
    void refreshData();
    int arrayItemCount();
}
