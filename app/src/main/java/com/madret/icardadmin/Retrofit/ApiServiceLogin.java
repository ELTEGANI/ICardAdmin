package com.madret.icardadmin.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by tigani on 11/5/2017.
 */

public interface ApiServiceLogin
{
    @GET("ICards/login.php")
    Call<loginpojo> getlogin(@Query("phone") String phone, @Query("password")String password);
}
