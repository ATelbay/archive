package newsapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("top-headlines")
    Call<Newsapi> getTopHeadlines(@Query("country") String country, @Query("apiKey") String apiKey);

    @GET("top-headlines")
    Call<Newsapi> getTopHeadlines(@Query("country") String country, @Query("apiKey") String apiKey,
                                  @Query("pageSize") Integer page);

    @GET("everything")
    Call<Newsapi> getEverything(@Query("language") String language, @Query("apiKey") String apiKey,
                                @Query("pageSize") Integer page);

    @GET("everything")
    Call<Newsapi> getEverything(@Query("q") String query, @Query("language") String language, @Query("apiKey") String apiKey,
                                @Query("pageSize") Integer page);
}
