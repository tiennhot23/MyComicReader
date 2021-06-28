package com.e.mycomicreader.Retrofit;

import com.e.mycomicreader.models.Comic;
import com.e.mycomicreader.models.DetailComic;
import com.e.mycomicreader.models.Genre;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

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
}
