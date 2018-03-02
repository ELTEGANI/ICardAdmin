package com.madret.icardadmin.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tigani on 11/13/2017.
 */

public interface ApiRequestCards
{
    @GET("ICards/GetCard.php")
    Call<Cardsresponse> getRequestedCards(@Query("phone")String phone);
}
