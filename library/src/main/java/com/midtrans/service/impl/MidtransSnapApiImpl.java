package com.midtrans.service.impl;

import com.midtrans.Config;
import com.midtrans.httpclient.APIHttpClient;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import org.json.JSONObject;

import java.util.Map;

/**
 * Implements from {@link MidtransSnapApi MidtransSnapApi}
 */
public class MidtransSnapApiImpl implements MidtransSnapApi {
    private final Config config;
    private final APIHttpClient httpClient;

    /**
     * SnapAPI constructor
     *
     * @param config Midtrans {@link Config configuration}
     */
    public MidtransSnapApiImpl(Config config) {
        this.config = config;
        this.httpClient = new APIHttpClient(config);
    }

    @Override
    public Config apiConfig() {
        return config;
    }

    @Override
    public JSONObject createTransaction(Map<String, Object> params) throws MidtransError {
        return new JSONObject((String) httpClient.request("POST", apiConfig().getBASE_URL() + "snap/v1/transactions", params));
    }

    @Override
    public String createTransactionToken(Map<String, Object> params) throws MidtransError {
        return createTransaction(params).getString("token");
    }

    @Override
    public String createTransactionRedirectUrl(Map<String, Object> params) throws MidtransError {
        return createTransaction(params).getString("redirect_url");
    }
}
