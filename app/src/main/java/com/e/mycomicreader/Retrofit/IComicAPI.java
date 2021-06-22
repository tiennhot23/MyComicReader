package com.e.mycomicreader.Retrofit;

import com.e.mycomicreader.models.DetailComic;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IComicAPI {
    @GET("manga/detail/{endpoint}")
    Observable<DetailComic> getMangaDetail(@Path("endpoint")String endpoint);
}
