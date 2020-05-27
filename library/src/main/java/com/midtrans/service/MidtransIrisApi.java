package com.midtrans.service;

import com.midtrans.Config;
import com.midtrans.httpclient.error.MidtransError;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public interface MidtransIrisApi {

    /**
     * Do re-set config Class iris-credential, IrisIdempotencyKey
     *
     * @return {Config class}
     */
    Config apiConfig();

    /**
     * Do `/ping` Returns pong message for monitoring purpose
     *
     * @return {String} - with value Pong
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    String ping() throws MidtransError;

    /**
     * Do get `/balance` API request to Use get current balance information. For Aggregator Partner you need to top up to Iris’ bank account.
     *
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response refer to: https://iris-docs.midtrans.com/#check-balance-aggregator
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject getBalance() throws MidtransError;

    /**
     * Do create `/beneficiaries` Use this API to create a new beneficiary information for quick access on the payout page in Iris Portal.
     *
     * @param params Map Object parameter, object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://iris-docs.midtrans.com/#create-beneficiaries)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject createBeneficiaries(Map<String, String> params) throws MidtransError;

    /**
     * Do update `/beneficiaries/{alias_name}` Use this API to update an existing beneficiary identified by it's `alias_name`.
     *
     * @param aliasName Alias name used by the Beneficiary. Length should be less than or equal to 20 characters only alphanumeric characters are allowed
     * @param params    Map Object parameter, object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://iris-docs.midtrans.com/#update-beneficiaries)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject updateBeneficiaries(String aliasName, Map<String, String> params) throws MidtransError;

    /**
     * Do get `/beneficiaries` Use this API to fetch list of all beneficiaries saved in Iris Portal.
     *
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response refer to: https://iris-docs.midtrans.com/#list-beneficiaries
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONArray getBeneficiaries() throws MidtransError;

    /**
     * Do create `/payouts` This API is for Creator to create a payout. It can be used for single payout and also multiple payouts.
     *
     * @param params Map Object parameter, object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://iris-docs.midtrans.com/#create-payouts)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject createPayouts(Map<String, Object> params) throws MidtransError;

    /**
     * Do approve `/payouts/approve` Use this API for Apporver to approve multiple payout request.
     *
     * @param params Map Object parameter, object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://iris-docs.midtrans.com/#approve-payouts)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject approvePayouts(Map<String, Object> params) throws MidtransError;

    /**
     * Do reject `/payouts/reject` Use this API for Apporver to reject multiple payout request.
     *
     * @param params Map Object parameter, object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://iris-docs.midtrans.com/#reject-payouts)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject rejectPayouts(Map<String, Object> params) throws MidtransError;

    /**
     * Do get `/payouts/{reference_no}` Use this API for get details of a single payout
     *
     * @param referenceNo String parameter, unique reference no of a payout (more params detail refer to: https://iris-docs.midtrans.com/#get-payout-details)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject getPayoutDetails(String referenceNo) throws MidtransError;

    /**
     * Do get `/statements` Use this API for list all transactions history for a month. You can specified start date and also end date for range transaction history.
     *
     * @param fromDate String date parameter, start date range for payouts (YYYY-MM-DD) more params detail refer to: https://iris-docs.midtrans.com/#transaction-history
     * @param toDate   String date parameter, end date range for payouts (YYYY-MM-DD) more params detail refer to: https://iris-docs.midtrans.com/#get-payout-details
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONArray getTransactionHistory(String fromDate, String toDate) throws MidtransError;

    /**
     * Do get `/channels` Use this API for get top up information channel only for Aggregator Partner
     *
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response (more params detail refer to: https://iris-docs.midtrans.com/#top-up-channel-information-aggregator)
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONArray getTopUpChannels() throws MidtransError;

    /**
     * Do get `/bank_accounts` Use this API for show list of registered bank accounts for facilitator partner
     *
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response (more params detail refer to: https://iris-docs.midtrans.com/#bank-accounts-facilitator)
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONArray getBankAccounts() throws MidtransError;

    /**
     * Do get `/bank_accounts/{bank_account_id}/balance` For Facilitator Partner, use this API is to get current balance information of your registered bank account.
     *
     * @param bankAccountId String Bank Account Number
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response refer to: https://iris-docs.midtrans.com/#check-balance-facilitator
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject getFacilitatorBalance(String bankAccountId) throws MidtransError;

    /**
     * Do get `/beneficiary_banks` Use this API for show list of supported banks in IRIS. https://iris-docs.midtrans.com/#supported-banks
     *
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response (more params detail refer to: https://iris-docs.midtrans.com/#list-banks)
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject getBeneficiaryBanks() throws MidtransError;

    /**
     * Do validate `/account_validation` Use this API for check if an account is valid, if valid return account information.
     *
     * @param bank    String bank code
     * @param account String Account number
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response (more params detail refer to: https://iris-docs.midtrans.com/#validate-bank-account)
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject validateBankAccount(String bank, String account) throws MidtransError;

}
