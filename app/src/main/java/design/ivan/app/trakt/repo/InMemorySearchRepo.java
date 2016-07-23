package design.ivan.app.trakt.repo;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import design.ivan.app.trakt.model.SearchResult;

public class InMemorySearchRepo implements IMemRepository<SearchResult>{
    SparseArray<SearchResult> cachedSearchResult;
    private final IMemService<SearchResult> memRepoService;

    public InMemorySearchRepo(IMemService<SearchResult> memRepoService) {
        this.memRepoService = memRepoService;
    }

    @Override
    public void getItemList(@NonNull final LoadItemsCallback<SearchResult> callback) {
        if(cachedSearchResult == null){
            memRepoService.getAllSavedItems(new IMemService.MemServiceCallback<SparseArray<SearchResult>>() {
                @Override
                public void onLoaded(SparseArray<SearchResult> forecastSparseArray) {
                    cachedSearchResult = forecastSparseArray;
                    callback.onItemsLoaded(cachedSearchResult);
                }
            });
        } else {
            callback.onItemsLoaded(cachedSearchResult);
        }
    }

    @Override
    public void getItem(@NonNull Integer itemId, @NonNull final GetItemCallback<SearchResult> callback) {
        memRepoService.getItem(itemId, new IMemService.MemServiceCallback<SearchResult>() {
            @Override
            public void onLoaded(SearchResult result) {
                callback.onItemLoaded(result);
            }
        });
    }

    @Override
    public void saveItem(SearchResult item) {

    }

    @Override
    public void removeItem(int position) {

    }

    @Override
    public void saveArrayItem(@NonNull SparseArray<SearchResult> itemSparseArray, @NonNull final SaveItemArrayCallback callback) {
        memRepoService.saveItemsArray(itemSparseArray, new IMemService.SaveSparseArrayCallback() {
            @Override
            public void savedSparseArray(boolean saved) {
                if(saved){
                    refreshData();
                    callback.onSavedArray(saved);
                }
            }
        });
    }

    @Override
    public void refreshData() {
        cachedSearchResult = null;
    }

    @Override
    public int arrayItemCount() {
        if (cachedSearchResult == null)
            return 0;
        return cachedSearchResult.size();
    }
}
