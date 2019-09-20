package com.midtrans.httpclient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.Map;

/**
 * HttpClient for midtrans SnapAPI
 */
public interface SnapApi {

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @POST("transactions")
    Call<ResponseBody> createTransactions(@Body Map<String, Object> objectMap);

}
