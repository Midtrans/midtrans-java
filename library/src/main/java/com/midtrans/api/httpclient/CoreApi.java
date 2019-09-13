package com.midtrans.api.httpclient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface CoreApi {

    @GET("token")
    Call<ResponseBody> cardToken(@QueryMap Map<String, String> params);

    @GET("card/register")
    Call<ResponseBody> registerCard(@QueryMap Map<String, String> params);

    @GET("point_inquiry/{tokenId}")
    Call<ResponseBody> cardPointInquiry(@Path("tokenId") String tokenId);

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

    @POST("{orderId}/refund/online/direct")
    Call<ResponseBody> directRefundTransaction(@Path("orderId") String orderId, @Body Map<String, String> body);

    @POST("capture")
    Call<ResponseBody> captureTransaction(@Body Map<String, String> body);

    @GET("{orderId}/status/b2b")
    Call<ResponseBody> getStatusB2B(@Path("orderId") String orderId);




}
