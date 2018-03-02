package com.madret.icardadmin.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by altigani Gabir on 19/09/17.
 */

public interface ApiServiceCreateCard
{
    @Multipart
    @POST("/ICards/createcard.php")
    Call<NewCardPojo> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name,
                                    @Part("fullname") RequestBody fullname,
                                    @Part("profession") RequestBody profession,
                                    @Part("companyname") RequestBody companyname,
                                    @Part("age") RequestBody age,
                                    @Part("address") RequestBody address,
                                    @Part("issuedate") RequestBody issuedate,
                                    @Part("expiredate") RequestBody expiredate,
                                    @Part("identificationcode") RequestBody identificationcode,
                                    @Part("companylink") RequestBody companylink,
                                    @Part("adminphone") RequestBody adminphone);
}