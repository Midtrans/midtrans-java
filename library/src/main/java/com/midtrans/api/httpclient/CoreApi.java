package com.midtrans.api.httpclient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Map;

public interface CoreApi {

    @POST("charge")
    Call<ResponseBody> chargeTransaction(@Body Map<String, Object> body);

    @GET("{orderId}/status")
    Call<ResponseBody> checkTransaction(@Path("orderId") String orderId);

    @POST("{orderId}/approve")
    Call<ResponseBody> approveTransaction(@Path("orderId") String orderId);

    @POST("{orderId}/deny")
    Call<ResponseBody> denyTransaction(@Path("orderId") String orderId);

    @POST("{orderId}/cancel")
    Call<ResponseBody> cancelTransaction(@Path("orderId") String orderId);

    @POST("{orderId}/expire")
    Call<ResponseBody> expireTransaction(@Path("orderId") String orderId);

    @POST("{orderId}/refund")
    Call<ResponseBody> refundTransaction(@Path("orderId") String orderId, @Body Map<String, String> body);


}
