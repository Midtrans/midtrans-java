package com.midtrans.service;

import com.midtrans.Config;
import com.midtrans.httpclient.error.MidtransError;
import org.json.JSONObject;

import java.util.Map;

/**
 * Gateway for midtrans CoreAPI
 */
public interface MidtransCoreApi {

    /**
     * Do re-set config Class like clientKey, serverKey, isProduction
     * @return Config.class
     */
    Config apiConfig();

    /**
     * Do /charge API request to Core API
     * @param params Map Object parameter, object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://api-docs.midtrans.com)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject chargeTransaction(Map<String, Object> params) throws MidtransError;

    /**
     * Do /{orderId}/status API request to Core API
     * @param orderId {String} of the transaction (more detail refer to: https://api-docs.midtrans.com/#get-transaction-status)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject checkTransaction(String orderId) throws MidtransError;

    /**
     * Do `/{orderId}/approve` API request to Core API
     * @param  orderId {String} of the transaction (more detail refer to: https://api-docs.midtrans.com/#approve-transaction)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject approveTransaction(String orderId) throws MidtransError;

    /**
     * Do `/{orderId}/cancel` API request to Core API
     * @param  orderId {String} of the transaction (more detail refer to: https://api-docs.midtrans.com/#cancel-transaction)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject cancelTransaction(String orderId) throws MidtransError;

    /**
     * Do `/{orderId}/expire` API request to Core API
     * @param  orderId {String} of the transaction (more detail refer to: https://api-docs.midtrans.com/#expire-transaction)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject expireTransaction(String orderId) throws MidtransError;

    /**
     * Do `/{orderId}/refund` API request to Core API
     * @param orderId {String} of the transaction (more detail refer to: https://api-docs.midtrans.com/#refund-transaction)
     * @param params {Map Object} object of Core API JSON body as parameter, will be converted to JSON
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject refundTransaction(String orderId, Map<String, String> params) throws MidtransError;

    /**
     * Do `/token` API request to Core API
     * @param  params {Map Object} object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://api-docs.midtrans.com/#get-token)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject cardToken(Map<String, String> params) throws MidtransError;

    /**
     * Do `/card/register` API request to Core API
     * @param  params {Map Object} object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://api-docs.midtrans.com/#register-card)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject registerCard(Map<String, String> params) throws MidtransError;

    /**
     * Do `/point_inquiry/{tokenId}` API request to Core API
     * @param  tokenId {String} of credit card (more detail refer to: https://api-docs.midtrans.com/#point-inquiry)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject cardPointInquiry(String tokenId) throws MidtransError;

    /**
     * Do `/capture API` request to Core API
     * @param params {Map Object} object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://api-docs.midtrans.com/#capture-transaction)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject captureTransaction(Map<String, String> params) throws MidtransError;

    /**
     * Do `/point_inquiry/{tokenId}` API request to Core API
     * @param  orderId {String} of credit card (more detail refer to: https://api-docs.midtrans.com/#get-transaction-status-b2b)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject getTransactionStatusB2B(String orderId) throws MidtransError;

    /**
     * Do `/{orderId}/status/b2b` API request to Core API
     * @param orderId {String} of the transaction (more detail refer to: https://api-docs.midtrans.com/#direct-refund-transaction)
     * @param params {Map Object} object of Core API JSON body as parameter, will be converted to JSON
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject directRefundTransaction(String orderId, Map<String, String> params) throws MidtransError;

    /**
     * Do `/{orderId}/deny` API request to Core API
     * @param orderId {String} of the transaction (more detail refer to: https://api-docs.midtrans.com/#deny-transaction)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject denyTransaction(String orderId) throws MidtransError;

    /**
     * Do `v1/bins/{bin}` API request to Core API
     * @param binNumber {String} of the transaction (more detail refer to: https://api-docs.midtrans.com/#bin-api)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject getBIN(String binNumber) throws MidtransError;

    /**
     * Do `v1/subscriptions` API request to Core API for Create subscription.
     * (more detail refer to: http://api-docs.midtrans.com/#create-subscription)
     *
     * @param subscriptionReq {Map Object} object of Subscription request body
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject createSubscription(Map<String, Object> subscriptionReq) throws MidtransError;

    /**
     * Do `v1/subscriptions/{subscription_id}` Find subscription by id to see the subscription details. (more detail refer to: http://api-docs.midtrans.com/#get-subscription)
     * @param subscriptionId {String} Subscription ID given by Midtrans
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject getSubscription(String subscriptionId) throws MidtransError;

    /**
     * Do `v1/subscriptions/{subscription_id}/disable` Make the subscription inactive (the subscription will not create transaction anymore)s. (more detail refer to: http://api-docs.midtrans.com/#disable-subscription)
     * @param subscriptionId {String} Subscription ID given by Midtrans
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject disableSubscription(String subscriptionId) throws MidtransError;

    /**
     * Do `v1/subscriptions/{subscription_id}/enable` Make the subscription active (the subscription will create periodic transaction) (more detail refer to: http://api-docs.midtrans.com/#enable-subscription)
     * @param subscriptionId {String} Subscription ID given by Midtrans
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject enableSubscription(String subscriptionId) throws MidtransError;

    /**
     * Do `v1/subscriptions/{subscription_id}` Update existing subscription details. (more detail refer to: http://api-docs.midtrans.com/#update-subscription)
     * @param subscriptionId {String} Subscription ID given by Midtrans
     * @param subscriptionReq {Map Object} object of Subscription request body
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject updateSubscription(String subscriptionId, Map<String, Object> subscriptionReq) throws MidtransError;

    /**
     * Do `/v2/pay/account` to link customer account to be used for specific payment channels (more detail refer to: http://api-docs.midtrans.com/#create-pay-account)
     * @param accountRequest {Map Object} object of create payment account request body
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject linkPaymentAccount(Map<String, Object> accountRequest) throws MidtransError;

    /**
     * Do `v2/pay/account/{account_id}` find account by id to see the customer account details (more detail refer to: http://api-docs.midtrans.com/#get-pay-account)
     * @param accountId {String} Customer account id to be used for payment
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject getPaymentAccount(String accountId) throws MidtransError;

    /**
     * Do `v2/pay/account/{account_id}/unbind` to unbind a linked customer account (more detail refer to: http://api-docs.midtrans.com/#unbind-pay-account)
     * @param accountId {String} Customer account id to be used for payment
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject unlinkPaymentAccount(String accountId) throws MidtransError;
}
