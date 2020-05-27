package com.midtrans.service.impl;

import com.midtrans.Config;
import com.midtrans.httpclient.APIHttpClient;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Map;

/**
 * Implements from {@link MidtransSnapApi MidtransSnapApi}
 */
public class MidtransSnapApiImpl implements MidtransSnapApi {
    private Config config;

    /**
     * SnapAPI constructor
     *
     * @param config Midtrans {@link Config configuration}
     */
    public MidtransSnapApiImpl(Config config) {
        this.config = config;
    }

    // Snap http request method with handle error exception
    private JSONObject snapHttpRequest(Map<String, Object> params) throws MidtransError {
        JSONObject rawResult = new JSONObject();
        APIHttpClient httpClient = new APIHttpClient(config);

        // Initialize Retrofit http client
        SnapApi snapApi = httpClient.getClient().create(SnapApi.class);
        //get to SnapAPI with retrofit return ResponseBody
        Call<ResponseBody> call = snapApi.createTransactions(params);
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                try {
                    if (response.body() != null) {
                        rawResult = new JSONObject(response.body().string());
                    }
                } catch (JSONException je) {
                    throw new MidtransError(je);
                }
            } else {
                if (response.errorBody() != null) {
                    rawResult = new JSONObject(response.errorBody().string());
                }
            }
        } catch (Exception e) {
            throw new MidtransError(e);
        }
        return rawResult;
    }

    // JSONRaw Handle error if jsonKey not found
    private String getValueFromRawJSON(JSONObject rawResult, String jsonKey) {
        String value = "";
        try {
            value = rawResult.getString(jsonKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public Config apiConfig() {
        return config;
    }

    @Override
    public JSONObject createTransaction(Map<String, Object> params) throws MidtransError {
        return snapHttpRequest(params);
    }

    @Override
    public String createTransactionToken(Map<String, Object> params) throws MidtransError {
        return getValueFromRawJSON(snapHttpRequest(params), "token");
    }

    @Override
    public String createTransactionRedirectUrl(Map<String, Object> params) throws MidtransError {
        return getValueFromRawJSON(snapHttpRequest(params), "redirect_url");
    }
}
