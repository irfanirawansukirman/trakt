package design.ivan.app.trakt.repo;

import design.ivan.app.trakt.model.Movie;
import design.ivan.app.trakt.model.SearchResult;

public class RepoLoader {
    public static IMemRepository<Movie> loadMemMovieRepository(){
        return MemRepositories.getInMemoryRepoInstance(new InMemoryMoviesService());
    }

    public static IMemRepository<SearchResult> loadMemSearchRepository(){
        return MemRepositories.getInMemSearchInstance(new InMemorySearchService());
    }
}
