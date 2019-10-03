package com.midtrans.service.impl;

import com.midtrans.Config;
import com.midtrans.httpclient.APIHttpClient;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.httpclient.error.ErrorUtils;
import com.midtrans.service.MidtransCoreApi;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Implements from {@link MidtransCoreApi MidtransCoreApi}
 */
public class MidtransCoreApiImpl implements MidtransCoreApi {
    private static final Logger LOGGER = Logger.getLogger(MidtransCoreApi.class.getName());

    private CoreApi coreApi;
    private Config config;

    /**
     * CoreAPI constructor
     *
     * @param config Midtrans {@link Config configuration}
     */
    public MidtransCoreApiImpl(Config config) {
        this.config = config;
        APIHttpClient httpClient = new APIHttpClient(config);
        this.coreApi = httpClient.getClient().create(CoreApi.class);
    }

    private JSONObject httpHandle(Call<ResponseBody> call) throws MidtransError {
        ErrorUtils errorUtils = new ErrorUtils();
        JSONObject object = new JSONObject();
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                try {
                    if (response.body() != null) {
                        object = new JSONObject(response.body().string());
                        if (config.isEnabledLog()) {
                            LOGGER.info("Midtrans response: " + object);
                        }
                    }
                } catch (JSONException je) {
                    throw new MidtransError(je);
                }
            } else {
                errorUtils.catchHttpErrorMessage(response.code(), response);
            }
        } catch (Exception e) {
            throw new MidtransError(e);
        }
        return object;
    }

    @Override
    public Config apiConfig() {
        return config;
    }

    @Override
    public JSONObject chargeTransaction(Map<String, Object> body) throws MidtransError {
        JSONObject result;
        Call<ResponseBody> call = coreApi.chargeTransaction(Optional.ofNullable(body).orElse(new HashMap<>()));
        result = httpHandle(call);
        return result;
    }

    @Override
    public JSONObject checkTransaction(String orderId) throws MidtransError {
        Call<ResponseBody> call = coreApi.checkTransaction(Optional.ofNullable(orderId).orElse("null"));
        return httpHandle(call);
    }

    @Override
    public JSONObject approveTransaction(String orderId) throws MidtransError {
        Call<ResponseBody> call = coreApi.approveTransaction(Optional.ofNullable(orderId).orElse("null"));
        return httpHandle(call);
    }

    @Override
    public JSONObject cancelTransaction(String orderId) throws MidtransError {
        Call<ResponseBody> call = coreApi.cancelTransaction(Optional.ofNullable(orderId).orElse("null"));
        return httpHandle(call);
    }

    @Override
    public JSONObject expireTransaction(String orderId) throws MidtransError {
        Call<ResponseBody> call = coreApi.expireTransaction(Optional.ofNullable(orderId).orElse("null"));
        return httpHandle(call);
    }

    @Override
    public JSONObject refundTransaction(String orderId, Map<String, String> body) throws MidtransError {
        Call<ResponseBody> call = coreApi.refundTransaction(Optional.ofNullable(orderId).orElse("null"), Optional.ofNullable(body).orElse(new HashMap<>()));
        return httpHandle(call);
    }

    @Override
    public JSONObject cardToken(Map<String, String> params) throws MidtransError {
        Call<ResponseBody> call = coreApi.cardToken(params);
        return httpHandle(call);
    }

    @Override
    public JSONObject registerCard(Map<String, String> params) throws MidtransError {
        Call<ResponseBody> call = coreApi.registerCard(params);
        return httpHandle(call);
    }

    @Override
    public JSONObject cardPointInquiry(String tokenId) throws MidtransError {
        Call<ResponseBody> call = coreApi.cardPointInquiry(tokenId);
        return httpHandle(call);
    }

    @Override
    public JSONObject captureTransaction(Map<String, String> params) throws MidtransError {
        Call<ResponseBody> call = coreApi.captureTransaction(params);
        return httpHandle(call);
    }

    @Override
    public JSONObject getTransactionStatusB2B(String orderId) throws MidtransError {
        Call<ResponseBody> call = coreApi.getStatusB2B(orderId);
        return httpHandle(call);
    }

    @Override
    public JSONObject directRefundTransaction(String orderId, Map<String, String> params) throws MidtransError {
        Call<ResponseBody> call = coreApi.directRefundTransaction(orderId, params);
        return httpHandle(call);
    }

    @Override
    public JSONObject denyTransaction(String orderId) throws MidtransError {
        Call<ResponseBody> call = coreApi.denyTransaction(orderId);
        return httpHandle(call);
    }
}
