package com.midtrans.api.service.impl;

import com.midtrans.api.Config;
import com.midtrans.api.httpclient.APIHttpClient;
import com.midtrans.api.httpclient.CoreApi;
import com.midtrans.api.httpclient.error.ErrorUtils;
import com.midtrans.api.service.MidtransCoreApi;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class MidtransCoreApiImpl implements MidtransCoreApi {
    private static final Logger LOGGER = Logger.getLogger(MidtransCoreApi.class.getName());

    private CoreApi coreApi;
    private Config config;

    public MidtransCoreApiImpl(Config config) {
        this.config = config;
        APIHttpClient httpClient = new APIHttpClient(config);
        this.coreApi = httpClient.getClient().create(CoreApi.class);
    }

    //Http Handle Method for handle HTTP Response
    private JSONObject httpHandle(Call<ResponseBody> call) {
        ErrorUtils errorUtils = new ErrorUtils();
        JSONObject object = new JSONObject();
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                try {
                    if (response.body() != null) {
                        object = new JSONObject(response.body().string());
                        if (!config.isProduction()) {
                            LOGGER.info("Midtrans response: " + object);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                errorUtils.httpErrorHandle(response.code(), response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public Config apiConfig() {
        return config;
    }


    @Override
    public JSONObject chargeTransaction(Map<String, Object> body) {
        Call<ResponseBody> call = coreApi.chargeTransaction(Optional.ofNullable(body).orElse(new HashMap<>()));
        return httpHandle(call);
    }

    @Override
    public JSONObject checkTransaction(String orderId) {
        Call<ResponseBody> call = coreApi.checkTransaction(Optional.ofNullable(orderId).orElse("null"));
        return httpHandle(call);
    }

    @Override
    public JSONObject approveTransaction(String orderId) {
        Call<ResponseBody> call = coreApi.approveTransaction(Optional.ofNullable(orderId).orElse("null"));
        return httpHandle(call);
    }

    @Override
    public JSONObject cancelTransaction(String orderId) {
        Call<ResponseBody> call = coreApi.cancelTransaction(Optional.ofNullable(orderId).orElse("null"));
        return httpHandle(call);
    }

    @Override
    public JSONObject expireTransaction(String orderId) {
        Call<ResponseBody> call = coreApi.expireTransaction(Optional.ofNullable(orderId).orElse("null"));
        return httpHandle(call);
    }

    @Override
    public JSONObject refundTransaction(String orderId, Map<String, String> body) {
        Call<ResponseBody> call = coreApi.refundTransaction(Optional.ofNullable(orderId).orElse("null"), Optional.ofNullable(body).orElse(new HashMap<>()));
        return httpHandle(call);
    }

    @Override
    public JSONObject cardToken(Map<String, String> params) {
        Call<ResponseBody> call = coreApi.cardToken(params);
        return httpHandle(call);
    }

    @Override
    public JSONObject registerCard(Map<String, String> params) {
        Call<ResponseBody> call = coreApi.registerCard(params);
        return httpHandle(call);
    }

    @Override
    public JSONObject cardPointInquiry(String tokenId) {
        Call<ResponseBody> call = coreApi.cardPointInquiry(tokenId);
        return httpHandle(call);
    }

    @Override
    public JSONObject captureTransaction(Map<String, String> params) {
        Call<ResponseBody> call = coreApi.captureTransaction(params);
        return httpHandle(call);
    }

    @Override
    public JSONObject getTransactionStatusB2B(String orderId) {
        Call<ResponseBody> call = coreApi.getStatusB2B(orderId);
        return httpHandle(call);
    }

    @Override
    public JSONObject directRefundTransaction(String orderId, Map<String, String> params) {
        Call<ResponseBody> call = coreApi.directRefundTransaction(orderId, params);
        return httpHandle(call);
    }

    @Override
    public JSONObject denyTransaction(String orderId) {
        Call<ResponseBody> call = coreApi.denyTransaction(orderId);
        return httpHandle(call);
    }
}
