package design.ivan.app.trakt.repo;

import android.support.annotation.NonNull;
import design.ivan.app.trakt.model.Movie;
import design.ivan.app.trakt.model.SearchResult;

public class MemRepositories {
    private static IMemRepository<Movie> topMoviesRepo = null;
    private static IMemRepository<SearchResult> searchRepo = null;
    public synchronized static IMemRepository<Movie> getInMemoryRepoInstance(@NonNull IMemService<Movie> memRepoService) {
        if (null == topMoviesRepo) {
            //initialize a new repository
            topMoviesRepo = new InMemoryMoviesRepo(memRepoService);
        }
        return topMoviesRepo;
    }

    public synchronized static IMemRepository<SearchResult> getInMemSearchInstance(@NonNull IMemService<SearchResult> memService){
        if(searchRepo == null){
            searchRepo = new InMemorySearchRepo(memService);
        }
        return searchRepo;
    }
}
