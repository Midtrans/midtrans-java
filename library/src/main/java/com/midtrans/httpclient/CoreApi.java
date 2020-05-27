package com.midtrans.httpclient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * HttpClient for midtrans CoreAPI
 */
public interface CoreApi {

    String API_VERSION = "v2/";

    @GET(API_VERSION + "token")
    Call<ResponseBody> cardToken(@QueryMap Map<String, String> params);

    @GET(API_VERSION + "card/register")
    Call<ResponseBody> registerCard(@QueryMap Map<String, String> params);

    @GET(API_VERSION + "point_inquiry/{tokenId}")
    Call<ResponseBody> cardPointInquiry(@Path("tokenId") String tokenId);

    @Headers({"Content-Type: application/json"})
    @POST(API_VERSION + "charge")
    Call<ResponseBody> chargeTransaction(@Body Map<String, Object> body);

    @GET(API_VERSION + "{orderId}/status")
    Call<ResponseBody> checkTransaction(@Path("orderId") String orderId);

    @POST(API_VERSION + "{orderId}/approve")
    Call<ResponseBody> approveTransaction(@Path("orderId") String orderId);

    @POST(API_VERSION + "{orderId}/deny")
    Call<ResponseBody> denyTransaction(@Path("orderId") String orderId);

    @POST(API_VERSION + "{orderId}/cancel")
    Call<ResponseBody> cancelTransaction(@Path("orderId") String orderId);

    @POST(API_VERSION + "{orderId}/expire")
    Call<ResponseBody> expireTransaction(@Path("orderId") String orderId);

    @POST(API_VERSION + "{orderId}/refund")
    Call<ResponseBody> refundTransaction(@Path("orderId") String orderId, @Body Map<String, String> body);

    @POST(API_VERSION + "{orderId}/refund/online/direct")
    Call<ResponseBody> directRefundTransaction(@Path("orderId") String orderId, @Body Map<String, String> body);

    @POST(API_VERSION + "capture")
    Call<ResponseBody> captureTransaction(@Body Map<String, String> body);

    @GET(API_VERSION + "{orderId}/status/b2b")
    Call<ResponseBody> getStatusB2B(@Path("orderId") String orderId);

    @GET("v1/bins/{bin}")
    Call<ResponseBody> getBIN(@Path("bin") String binNumber);

}
