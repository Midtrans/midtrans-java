package com.midtrans.service.impl;

import com.midtrans.Config;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import org.json.JSONObject;

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

    @Override
    public Config apiConfig() {
        return config;
    }

    @Override
    public JSONObject createTransaction(Map<String, Object> params) throws MidtransError {
        return SnapApi.createTransaction(params, config);
    }

    @Override
    public String createTransactionToken(Map<String, Object> params) throws MidtransError {
        return SnapApi.createTransactionToken(params, config);
    }

    @Override
    public String createTransactionRedirectUrl(Map<String, Object> params) throws MidtransError {
        return SnapApi.createTransactionRedirectUrl(params, config);
    }
}
