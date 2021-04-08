package com.midtrans.sample.controller;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransIrisApi;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.midtrans.sample.data.Constant.sandboxApproverKey;
import static com.midtrans.sample.data.Constant.sandboxCreatorKey;

@Controller
public class IrisController {

    MidtransIrisApi irisApi = new ConfigFactory(
            new Config(sandboxCreatorKey,
                    null,
                    false))
            .getIrisApi();

    public IrisController() {
        String irisSandboxMerchantKey = "IRIS-merchant-c8709d85-09d6-49c4-8ff5-9eaf81ec31cd";
        irisApi.apiConfig().setIRIS_MERCHANT_KEY(irisSandboxMerchantKey);
    }

    @GetMapping(value = "/iris/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ping() throws MidtransError {
        String result = irisApi.ping();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/iris/index", method = RequestMethod.GET)
    public String iris(Model model) throws MidtransError {
        LocalDate localDate = LocalDate.now();
        String fromDate  = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
        String toDate = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);

        JSONObject currentBalance = irisApi.getBalance();
        JSONArray transactionHistory = irisApi.getTransactionHistory(fromDate, toDate);

        List<Map<String, Object>> rows = new ArrayList<>();
        List<String> headers = Arrays.asList("Name", "Amount", "Type", "Reference", "Account", "Bank", "Status");
        for (int i =0; i < transactionHistory.length(); i++) {
            Map<String, Object> value = new HashMap<>();
            if (transactionHistory.getJSONObject(i).has("beneficiary_name")) {
                value.put("Name", transactionHistory.getJSONObject(i).getString("beneficiary_name"));
            }
            if (transactionHistory.getJSONObject(i).has("amount")) {
                value.put("Amount", transactionHistory.getJSONObject(i).getString("amount"));
            }
            if (transactionHistory.getJSONObject(i).has("type")) {
                value.put("Type", transactionHistory.getJSONObject(i).getString("type"));
            }
            if (transactionHistory.getJSONObject(i).has("reference_no")) {
                value.put("Reference", transactionHistory.getJSONObject(i).getString("reference_no"));
            }
            if (transactionHistory.getJSONObject(i).has("beneficiary_account")) {
                value.put("Account", transactionHistory.getJSONObject(i).getString("beneficiary_account"));
            }
            if (transactionHistory.getJSONObject(i).has("account")) {
                value.put("Bank", transactionHistory.getJSONObject(i).getString("account"));
            }
            if (transactionHistory.getJSONObject(i).has("status")) {
                value.put("Status", transactionHistory.getJSONObject(i).getString("status"));
            }
            rows.add(i, value);
        }
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("headers", headers);
        model.addAttribute("rows", rows);
        model.addAttribute("balance", currentBalance.getString("balance"));
        return "iris/index";
    }

    @RequestMapping(value = "/iris/payouts", method = RequestMethod.GET)
    public String payout(Model model) throws MidtransError {
        JSONArray response = irisApi.getBeneficiaries();
        List<String> listNameBeneficiaries = new ArrayList<>();
        if (response != null) {
            int len = response.length();
            for (int i=0;i<len;i++){
                listNameBeneficiaries.add((response.getJSONObject(i).getString("name")));
            }
        }
        model.addAttribute("amounts", getRandomNumberString());
        model.addAttribute("names", listNameBeneficiaries);
        return "iris/create-payout";
    }

    private static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    @PostMapping(value = "/iris/payouts/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createPayout(@RequestBody Map<String, String> params) throws MidtransError {
        ArrayList<Map<String,String>> payoutBeneficiaries = new ArrayList<>();
        payoutBeneficiaries.add(getBeneficiaries(params));

        Map<String, Object> payouts = new HashMap<>();
        payouts.put("payouts", payoutBeneficiaries);
        irisApi.apiConfig().setServerKey(sandboxCreatorKey);
        JSONObject result = irisApi.createPayouts(payouts);

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    private Map<String, String> getBeneficiaries(Map<String, String> params) throws MidtransError {
        irisApi.apiConfig().setServerKey(sandboxCreatorKey);
        JSONArray result = irisApi.getBeneficiaries();
        Map<String, String> beneficiary = new HashMap<>();
        if (result != null) {
            for (int i=0;i<result.length();i++){
                String resultName = result.getJSONObject(i).getString("name");
                String paramName = params.get("name");
                if (resultName.equals(paramName)) {
                    beneficiary.put("beneficiary_name",result.getJSONObject(i).getString("name"));
                    beneficiary.put("beneficiary_account",result.getJSONObject(i).getString("account"));
                    beneficiary.put("beneficiary_bank",result.getJSONObject(i).getString("bank"));
                    beneficiary.put("beneficiary_email",result.getJSONObject(i).getString("email"));
                    beneficiary.put("amount",params.get("amount"));
                    beneficiary.put("notes",params.get("notes"));
                    break;
                }
            }
        }
        return beneficiary;
    }

    @PostMapping(value = "/iris/payouts/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> approve(@RequestBody Map<String, Object> params) throws MidtransError {
        irisApi.apiConfig().setServerKey(sandboxApproverKey);
        JSONObject result = irisApi.approvePayouts(params);
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/iris/payouts/reject", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> reject(@RequestBody Map<String, Object> params) throws MidtransError {
        irisApi.apiConfig().setServerKey(sandboxApproverKey);
        JSONObject result = irisApi.rejectPayouts(params);
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/iris/payouts/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> payoutDetails(@RequestBody Map<String, String> params) throws MidtransError {
        String referenceNo = params.get("reference_no");
        irisApi.apiConfig().setServerKey(sandboxCreatorKey);
        JSONObject result = irisApi.getPayoutDetails(referenceNo);
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/iris/notifications", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> notifications(@RequestHeader("Iris-Signature") String irisSignature, HttpEntity<String> httpEntity) {
        // Get json body request
        String jsonBodyRequest = httpEntity.getBody();

        // Create hash Signature from payload + iris-merchant-key
        String hashParam = jsonBodyRequest + irisApi.apiConfig().getIRIS_MERCHANT_KEY();
        String hashSignature = sha512(hashParam);

        JSONObject jsonObject = new JSONObject(jsonBodyRequest);

        // 1. Validate the value header Iris-Signature
        if (irisSignature.equals(hashSignature)) {

            if (jsonObject.getString("status").equals("approved")) {
                // TODO set payouts status on your database to 'approved' e.g: STATUS 'Payout status approved
                System.out.println("IRIS NOTIFICATION RECEIVED  : STATUS APPROVED");
            }

            else if (jsonObject.getString("status").equals("rejected")) {
                // TODO set payouts status on your database to 'rejected'
                System.out.println("IRIS NOTIFICATION RECEIVED  : STATUS REJECTED");
            }

            else if (jsonObject.getString("status").equals("processed")) {
                // TODO set payouts status on your database to 'processed'
                System.out.println("IRIS NOTIFICATION RECEIVED  : STATUS PROCESSED");
            }

            else if (jsonObject.getString("status").equals("completed")) {
                // TODO set payouts status on your database to 'completed'
                System.out.println("IRIS NOTIFICATION RECEIVED  : STATUS COMPLETED");
            }

            else if (jsonObject.getString("status").equals("failed")) {
                // TODO set payouts status on your database to 'failed'
                System.out.println("IRIS NOTIFICATION RECEIVED  : STATUS FAILED");
            }

            else if (jsonObject.getString("status").equals("topup")) {
                // TODO set topup status on your database to 'topup'
                System.out.println("IRIS NOTIFICATION RECEIVED  : TOPUP");
            }

            /*
            For testing purpose from Iris dashboard
             */
            else if (jsonObject.getString("status").equals("test")) {
                System.out.println("IRIS NOTIFICATION RECEIVED : STATUS TEST");
            }
        } else {
            System.out.println("IRIS NOTIFICATION RECEIVED : SIGNATURE NOT VALID");
            return new ResponseEntity<>("SIGNATURE NOT VALID", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    private static String sha512(String input)
    {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            /*
            digest() method is called to calculate message digest of the input string.
            returned as array of byte
             */
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String signature = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (signature.length() < 32) {
                signature = "0" + signature;
            }
            // return the HashString
           return signature;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
