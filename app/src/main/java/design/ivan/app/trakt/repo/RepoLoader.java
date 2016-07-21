package design.ivan.app.trakt.repo;

import design.ivan.app.trakt.model.Movie;

public class RepoLoader {
    public static IMemRepository<Movie> loadMemMovieRepository(){
        return MemRepositories.getInMemoryRepoInstance(new InMemoryMoviesService());
    }
}
