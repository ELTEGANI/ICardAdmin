package com.madret.icardadmin.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by tigani on 11/20/2017.
 */

public interface ApiCreateQrcode
{
    @Multipart
    @POST("/ICards/CreateQrCode.php")
    Call<CreateQrcodePojo> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name,
                                              @Part("identificationcode") RequestBody identificationcode,
                                              @Part("persname") RequestBody persname,
                                              @Part("job") RequestBody job,
                                              @Part("companyname") RequestBody companyname,
                                              @Part("link") RequestBody link,
                                              @Part("email") RequestBody email,
                                              @Part("phone") RequestBody phone);
}
