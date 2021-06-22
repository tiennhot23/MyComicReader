package com.e.mycomicreader.Common;

import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.Retrofit.RetrofitClient;


public class Common {
    public static IComicAPI getAPI(){
        return RetrofitClient.getInstance().create(IComicAPI.class);
    }

//    public static String convertListToString(List<T>);
}
