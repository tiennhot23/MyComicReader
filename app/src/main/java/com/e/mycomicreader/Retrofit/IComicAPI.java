package com.e.mycomicreader.Retrofit;

import com.e.mycomicreader.models.Chapter;
import com.e.mycomicreader.models.Comic;
import com.e.mycomicreader.models.DetailComic;
import com.e.mycomicreader.models.Genre;
import io.reactivex.Observable;
import retrofit2.http.*;

import java.util.List;

public interface IComicAPI {
    @GET("genres/{genre}/{query}")
    Observable<List<Comic>> filterComics(@Path("genre")String genre,@Path("query")String query);

    @GET("genres/{genre}")
    Observable<List<Comic>> filterComics(@Path("genre")String genre);

    @GET("comics")
    Observable<List<Comic>> getComics();

    @GET("genres")
    Observable<List<Genre>> getGenres();

    @GET("search/{query}")
    Observable<List<Comic>> getSearchComic(@Path("query")String query);

    @GET("{endpoint}")
    Observable<List<DetailComic>> getDetailComic(@Path("endpoint")String endpoint);

    @GET("chapter/{chapter_endpoint}")
    Observable<List<Chapter>> getChapter(@Path("chapter_endpoint")String chapter_endpoint);

    @POST("chapter")
    @FormUrlEncoded
    Observable<List<Chapter>> getListChapter(@Field("data") String data);

    @POST("filter_genres")
    @FormUrlEncoded
    Observable<List<Comic>> filterGenres(@Field("data") String data);
}
