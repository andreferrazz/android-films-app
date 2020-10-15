package com.example.appmovies.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String IMG_URL = "https://image.tmdb.org/t/p/w500";
    private static final String API_KEY_V3 = "3d277a1081b6c8ea430c6473bedcdf42";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static String getImgUrl(){
        return IMG_URL;
    }

    public static String getApiKey() {
        return API_KEY_V3;
    }
}
