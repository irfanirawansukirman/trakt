package design.ivan.app.trakt.repo;

import android.support.annotation.NonNull;
import design.ivan.app.trakt.model.Movie;

public class MemRepositories {
    private static IMemRepository<Movie> topMoviesRepo = null;
    public synchronized static IMemRepository<Movie> getInMemoryRepoInstance(@NonNull IMemService<Movie> memRepoService) {
        if (null == topMoviesRepo) {
            //initialize a new repository
            topMoviesRepo = new InMemoryMoviesRepo(memRepoService);
        }
        return topMoviesRepo;
    }
}
