package com.midtrans.httpclient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * HttpClient for midtrans CoreAPI
 */
public interface CoreApi {

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @GET("token")
    Call<ResponseBody> cardToken(@QueryMap Map<String, String> params);

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @GET("card/register")
    Call<ResponseBody> registerCard(@QueryMap Map<String, String> params);

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @GET("point_inquiry/{tokenId}")
    Call<ResponseBody> cardPointInquiry(@Path("tokenId") String tokenId);

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @POST("charge")
    Call<ResponseBody> chargeTransaction(@Body Map<String, Object> body);

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @GET("{orderId}/status")
    Call<ResponseBody> checkTransaction(@Path("orderId") String orderId);

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @POST("{orderId}/approve")
    Call<ResponseBody> approveTransaction(@Path("orderId") String orderId);

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @POST("{orderId}/deny")
    Call<ResponseBody> denyTransaction(@Path("orderId") String orderId);

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @POST("{orderId}/cancel")
    Call<ResponseBody> cancelTransaction(@Path("orderId") String orderId);

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @POST("{orderId}/expire")
    Call<ResponseBody> expireTransaction(@Path("orderId") String orderId);

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @POST("{orderId}/refund")
    Call<ResponseBody> refundTransaction(@Path("orderId") String orderId, @Body Map<String, String> body);

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @POST("{orderId}/refund/online/direct")
    Call<ResponseBody> directRefundTransaction(@Path("orderId") String orderId, @Body Map<String, String> body);

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @POST("capture")
    Call<ResponseBody> captureTransaction(@Body Map<String, String> body);

    @Headers({"Content-Type: application/json", "User-Agent: Midtrans-Java-Lib"})
    @GET("{orderId}/status/b2b")
    Call<ResponseBody> getStatusB2B(@Path("orderId") String orderId);

}
