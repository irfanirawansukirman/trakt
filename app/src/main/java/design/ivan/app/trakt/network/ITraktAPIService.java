package design.ivan.app.trakt.network;

import java.util.List;

import design.ivan.app.trakt.model.Movie;
import design.ivan.app.trakt.model.SearchResult;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ITraktAPIService {

    @Headers({
            "Content-Type:application/json",
            "trakt-api-version: 2",
            "trakt-api-key: ad005b8c117cdeee58a1bdb7089ea31386cd489b21e14b19818c91511f12a086"
    })
    @GET("movies/popular")
    Call<List<Movie>> getTopMovies();

    @Headers({
            "Content-Type:application/json",
            "trakt-api-version: 2",
            "trakt-api-key: ad005b8c117cdeee58a1bdb7089ea31386cd489b21e14b19818c91511f12a086"
    })
    @GET("search/movie")
    Call<List<SearchResult>> searchMovie(@Query("query") String movieName);


     final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.trakt.tv/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build();
}
