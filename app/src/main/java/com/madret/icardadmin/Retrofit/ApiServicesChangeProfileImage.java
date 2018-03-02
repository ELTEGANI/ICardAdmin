package com.madret.icardadmin.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by tigani on 11/15/2017.
 */

public interface ApiServicesChangeProfileImage
{
    @Multipart
    @POST("/ICards/UpdateProfileImage.php")
    Call<ChangeProfilePhoto> ChangeImageProfile(@Part MultipartBody.Part file, @Part("file") RequestBody name,
                                                @Part("id") RequestBody id);

}
