package com.madret.icardadmin.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by tigani on 11/4/2017.
 */

public interface ApiServiceRegisteration
{
    @GET("ICards/register.php")
    Call<ResgisterationPojo> getResponse(@Query("name") String name,
                                         @Query("phone")String phone,
                                         @Query("password")String password,
                                         @Query("companyname")String companyname);
}
