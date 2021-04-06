package com.midtrans.service.impl;

import com.midtrans.Config;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.TransactionApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import org.json.JSONObject;

import java.util.Map;

/**
 * Implements from {@link MidtransCoreApi MidtransCoreApi}
 */
public class MidtransCoreApiImpl implements MidtransCoreApi {

    private Config config;

    /**
     * CoreAPI constructor
     *
     * @param config Midtrans {@link Config configuration}
     */
    public MidtransCoreApiImpl(Config config) {
        this.config = config;
    }

    @Override
    public Config apiConfig() {
        return config;
    }

    @Override
    public JSONObject chargeTransaction(Map<String, Object> body) throws MidtransError {
        return CoreApi.chargeTransaction(body, config);
    }

    @Override
    public JSONObject checkTransaction(String param) throws MidtransError {
        return TransactionApi.checkTransaction(param, config);
    }

    @Override
    public JSONObject approveTransaction(String param) throws MidtransError {
        return TransactionApi.approveTransaction(param, config);
    }

    @Override
    public JSONObject cancelTransaction(String param) throws MidtransError {
        return TransactionApi.cancelTransaction(param, config);
    }

    @Override
    public JSONObject expireTransaction(String param) throws MidtransError {
        return TransactionApi.expireTransaction(param, config);
    }

    @Override
    public JSONObject refundTransaction(String param, Map<String, String> body) throws MidtransError {
        return TransactionApi.refundTransaction(param, body, config);
    }

    @Override
    public JSONObject cardToken(Map<String, String> params) throws MidtransError {
        return CoreApi.cardToken(params, config);
    }

    @Override
    public JSONObject registerCard(Map<String, String> params) throws MidtransError {
        return CoreApi.registerCard(params, config);
    }

    @Override
    public JSONObject cardPointInquiry(String tokenId) throws MidtransError {
        return CoreApi.cardPointInquiry(tokenId, config);
    }

    @Override
    public JSONObject captureTransaction(Map<String, String> params) throws MidtransError {
        return TransactionApi.captureTransaction(params, config);
    }

    @Override
    public JSONObject getTransactionStatusB2B(String param) throws MidtransError {
        return TransactionApi.getStatusB2b(param, config);
    }

    @Override
    public JSONObject directRefundTransaction(String param, Map<String, String> requestBody) throws MidtransError {
        return TransactionApi.directRefundTransaction(param, requestBody, config);
    }

    @Override
    public JSONObject denyTransaction(String param) throws MidtransError {
        return TransactionApi.denyTransaction(param, config);
    }

    @Override
    public JSONObject getBIN(String binNumber) throws MidtransError {
        return CoreApi.getBin(binNumber, config);
    }
}
