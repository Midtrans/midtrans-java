package com.midtrans.service.impl;

import com.midtrans.Config;
import com.midtrans.httpclient.APIHttpClient;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.ErrorUtils;
import com.midtrans.service.MidtransSnapApi;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Implements from {@link com.midtrans.service.MidtransSnapApi MidtransSnapApi}
 */
public class MidtransSnapApiImpl implements MidtransSnapApi {
    private static final Logger LOGGER = Logger.getLogger(MidtransSnapApi.class.getName());

    private Config config;

    /**
     * SnapAPI constructor
     *
     * @param config Midtrans {@link com.midtrans.Config configuration}
     */
    public MidtransSnapApiImpl(Config config) {
        this.config = config;
    }

    // Snap http request method with handle error exception
    private JSONObject snapHttpRequest(Map<String, Object> params) {

        JSONObject rawResult = new JSONObject();
        APIHttpClient httpClient = new APIHttpClient(config);
        ErrorUtils errorUtils = new ErrorUtils();

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
                        if (!config.isProduction()) {
                            LOGGER.info("Midtrans Snap Response : " + rawResult.toString());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                errorUtils.catchHttpErrorMessage(response.code(), response);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public Config apiConfig() {
        return config;
    }

    public JSONObject createTransaction(Map<String, Object> params) {
        return snapHttpRequest(params);
    }

    public String createTransactionToken(Map<String, Object> params) {
        return getValueFromRawJSON(snapHttpRequest(params), "token");
    }

    public String createTransactionRedirectUrl(Map<String, Object> params) {
        return getValueFromRawJSON(snapHttpRequest(params), "redirect_url");
    }
}
