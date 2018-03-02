package com.madret.icardadmin.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tigani on 11/15/2017.
 */

public interface ApiDeleteCard
{
    @GET("ICards/DeleteCard.php")
    Call<DeleteResponse> DeleteCard(@Query("id")String id);

}
