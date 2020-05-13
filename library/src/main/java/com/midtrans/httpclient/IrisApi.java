package com.midtrans.httpclient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * HttpClient for midtrans IrisAPI
 */
public interface IrisApi {

    String API_VERSION = "api/v1/";

    @GET("ping")
    Call<ResponseBody> ping();

    @GET(API_VERSION + "balance")
    Call<ResponseBody> getAggregatorBalance();

    @POST(API_VERSION + "beneficiaries")
    Call<ResponseBody> createBeneficiaries(@Body Map<String, String> params);

    @PATCH(API_VERSION + "beneficiaries/{aliasName}")
    Call<ResponseBody> updateBeneficiaries(@Path("aliasName") String aliasName, @Body Map<String, String> params);

    @GET(API_VERSION + "beneficiaries")
    Call<ResponseBody> getListBeneficiaries();

    @POST(API_VERSION + "payouts")
    Call<ResponseBody> createPayouts(@Body Map<String, Object> params);

    @POST(API_VERSION + "payouts/approve")
    Call<ResponseBody> approvePayouts(@Body Map<String, Object> params);

    @POST(API_VERSION + "payouts/reject")
    Call<ResponseBody> rejectPayouts(@Body Map<String, Object> params);

    @GET(API_VERSION + "payouts/{referenceNo}")
    Call<ResponseBody> getPayoutDetails(@Path("referenceNo") String referenceNo);

    @GET(API_VERSION + "statements")
    Call<ResponseBody> getTransactionHistory(@Query("from_date") String fromDate, @Query("to_Date") String toDate);

    @GET(API_VERSION + "channels")
    Call<ResponseBody> getTopUpChannels();

    @GET(API_VERSION + "bank_accounts")
    Call<ResponseBody> getBankAccount();

    @GET(API_VERSION + "bank_account/{bankAccountId}/balance")
    Call<ResponseBody> getFacilitatorBalance(@Path("bankAccountId") String bankAccountId);

    @GET(API_VERSION + "beneficiary_banks")
    Call<ResponseBody> getListBanks();

    @GET(API_VERSION + "account_validation")
    Call<ResponseBody> validateBankAccount(@Query("bank") String bank, @Query("account") String account);
}
