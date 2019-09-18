package com.midtrans.service;

import com.midtrans.Config;
import org.json.JSONObject;
import java.util.Map;

/**
 * Gateway for midtrans SnapAPI
 */
public interface MidtransSnapApi {

    /**
     * Do re-setting config Class like clientKey, serverKey, isProduction
     *
     * @return Config.class
     */
    Config apiConfig();

    /**
     * Do `/transactions` API request to SNAP API return RAW JSON with JSONObject
     *
     * @param params {Map Object} object of Core API JSON body as parameter, will be converted to JSON (more detail refer to: https://snap-docs.midtrans.com)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     */
    JSONObject createTransaction(Map<String, Object> params);

    /**
     * Do `/transactions` API request to SNAP API return String token
     * @param params {Map Object} object of Core API JSON body as parameter, will be converted to JSON (more detail refer to: https://snap-docs.midtrans.com)
     * @return {String} return tokenId to processing on frontend with Snap.js for transaction with SNAP PopUp
     */
    String createTransactionToken(Map<String, Object> params);

    /**
     * Do `/transactions` API request to SNAP API return String redirectURL
     * @param params {Map Object} object of Core API JSON body as parameter, will be converted to JSON (more detail refer to: https://snap-docs.midtrans.com)
     * @return {String} -  return redirectURL for processing transaction with SNAP Redirect
     */
    String createTransactionRedirectUrl(Map<String, Object> params);
}