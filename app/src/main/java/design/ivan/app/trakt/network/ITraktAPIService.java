package design.ivan.app.trakt.network;

import java.io.IOException;
import java.util.List;

import design.ivan.app.trakt.model.Movie;
import design.ivan.app.trakt.model.SearchResult;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ITraktAPIService {
    @GET("movies/popular?extended=images,full&limit=10")
    Call<List<Movie>> getTopMovies(@Query("page") int page);

    @GET("search/movie?extended=images,full&limit=10")
    Call<List<SearchResult>> searchMovie(@Query("query") String movieName, @Query("page") int page);

    OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(
                    new Interceptor() {
                        @Override
                        public Response intercept(Interceptor.Chain chain) throws IOException {
                            Request original = chain.request();

                            Request request = original.newBuilder()
                                    .header("Content-Type", "application/json")
                                    .header("trakt-api-version", "2")
                                    .header("trakt-api-key", "ad005b8c117cdeee58a1bdb7089ea31386cd489b21e14b19818c91511f12a086")
                                    .method(original.method(), original.body())
                                    .build();

                            return chain.proceed(request);
                        }
                    }
            )
            .build();

     final Retrofit retrofit = new Retrofit.Builder()
             .baseUrl("https://api.trakt.tv/")
             .addConverterFactory(MoshiConverterFactory.create())
             .client(httpClient)
             .build();
}
