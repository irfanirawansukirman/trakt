package design.ivan.app.trakt.repo;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import design.ivan.app.trakt.model.Movie;

public class InMemoryMoviesRepo implements IMemRepository<Movie> {

    private final IMemService<Movie> memRepoService;
    SparseArray<Movie> cachedTopMovies;

    public InMemoryMoviesRepo(IMemService<Movie> memRepoService) {
        this.memRepoService = memRepoService;
    }

    @Override
    public void getItemList(@NonNull final LoadItemsCallback<Movie> callback) {
        if(cachedTopMovies == null){
            memRepoService.getAllSavedItems(new IMemService.MemServiceCallback<SparseArray<Movie>>() {
                @Override
                public void onLoaded(SparseArray<Movie> forecastSparseArray) {
                    cachedTopMovies = forecastSparseArray;
                    callback.onItemsLoaded(cachedTopMovies);
                }
            });
        } else {
            callback.onItemsLoaded(cachedTopMovies);
        }
    }

    @Override
    public void getItem(@NonNull Integer itemId, @NonNull final GetItemCallback<Movie> callback) {
        memRepoService.getItem(itemId, new IMemService.MemServiceCallback<Movie>() {
            @Override
            public void onLoaded(Movie movie) {
                callback.onItemLoaded(movie);
            }
        });
    }

    @Override
    public void saveItem(@NonNull Movie item) {
        memRepoService.saveItem(item);
        refreshData();
    }

    @Override
    public void saveArrayItem(@NonNull SparseArray<Movie> itemSparseArray, @NonNull final SaveItemArrayCallback callback) {
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
        cachedTopMovies = null;
    }

    @Override
    public int arrayItemCount() {
        if(cachedTopMovies == null)
            return 0;
        return cachedTopMovies.size();
    }
}
