package com.e.mycomicreader.Common;

import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.Retrofit.RetrofitClient;
import com.e.mycomicreader.models.Comic;

import java.util.*;


public class Common {
    public static IComicAPI getAPI(){
        return RetrofitClient.getInstance().create(IComicAPI.class);
    }

    public static List<Comic> sortRating(List<Comic> comics){
        List<Comic> temp = new ArrayList<>();
        temp.addAll(comics);
        Collections.sort(temp, new Comparator<Comic>() {
            @Override
            public int compare(Comic o1, Comic o2) {
                return (Double.parseDouble(o1.rating) < Double.parseDouble(o2.rating))? 1 : -1;
            }
        });
        return temp;
    }


}
