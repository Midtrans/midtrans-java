package com.midtrans.api.httpclient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.Map;

/**
 * HttpClient for midtrans SnapAPI
 */
public interface SnapApi {

    @POST("transactions")
    Call<ResponseBody> createTransactions(@Body Map<String, Object> objectMap);

}
