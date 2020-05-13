package com.midtrans.sample.controller;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransIrisApi;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Controller
public class IrisController {

    private String sandboxCreatorKey = "IRIS-330198f0-e49d-493f-baae-585cfded355d";
    private String sandboxApproverKey = "IRIS-1595c12b-6814-4e5a-bbbb-9bc18193f47b";

    MidtransIrisApi irisApi = new ConfigFactory(
            new Config(sandboxCreatorKey,
                    null,
                    false))
            .getIrisApi();

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

        JSONObject currentBalance = irisApi.getAggregatorBalance();
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
        JSONArray response = irisApi.getListBeneficiaries();
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
        irisApi.apiConfig().setSERVER_KEY(sandboxCreatorKey);
        JSONObject result = irisApi.createPayouts(payouts);

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    private Map<String, String> getBeneficiaries(Map<String, String> params) throws MidtransError {
        irisApi.apiConfig().setSERVER_KEY(sandboxCreatorKey);
        JSONArray result = irisApi.getListBeneficiaries();
        System.out.println(result);
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
        irisApi.apiConfig().setSERVER_KEY(sandboxApproverKey);
        JSONObject result = irisApi.approvePayouts(params);
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/iris/payouts/reject", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> reject(@RequestBody Map<String, Object> params) throws MidtransError {
        irisApi.apiConfig().setSERVER_KEY(sandboxApproverKey);
        JSONObject result = irisApi.rejectPayouts(params);
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/iris/payouts/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> payoutDetails(@RequestBody Map<String, String> params) throws MidtransError {
        String referenceNo = params.get("reference_no");
        irisApi.apiConfig().setSERVER_KEY(sandboxCreatorKey);
        JSONObject result = irisApi.getPayoutsDetails(referenceNo);
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

}