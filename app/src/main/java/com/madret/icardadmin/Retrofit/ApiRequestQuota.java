package com.madret.icardadmin.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tigani on 11/12/2017.
 */

public interface ApiRequestQuota
{
    @GET("ICards/RequestQuoat.php")
    Call<RequestPojo> getRequest(@Query("quoata") String quoata,@Query("phone")String phone);
}
