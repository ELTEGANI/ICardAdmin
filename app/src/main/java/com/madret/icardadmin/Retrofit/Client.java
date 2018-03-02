package com.madret.icardadmin.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by altigani Gabir on 19/09/17.
 */

public class Client
{
        public static final String BASE_URL = "http://madret.com";
        private static Retrofit retrofit = null;
        public static int unique_id;

        public static Retrofit getClient()
        {
            if (retrofit==null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
}