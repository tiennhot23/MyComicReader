package com.e.mycomicreader.Retrofit;

import com.e.mycomicreader.models.Comic;
import com.e.mycomicreader.models.DetailComic;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface IComicAPI {
    @GET("genres/{endpoint}")
    Observable<DetailComic> getMangaDetail(@Path("endpoint")String endpoint);

    @GET("comics")
    Observable<List<Comic>> getComics();
}
