package com.midtrans.sample;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.sample.data.DataMockup;
import com.midtrans.service.MidtransSnapApi;
import org.json.JSONObject;

import java.util.Map;

import static com.midtrans.sample.data.Constant.sandboxClientKey;
import static com.midtrans.sample.data.Constant.sandboxServerKey;

public class SampleSnapApi {

    public static void main(String[] args) throws MidtransError {
        DataMockup dataMockup = new DataMockup();
        dataMockup.setPaymentType("gopay");

        /*
          Snap Api Request with global config
          */
        RequestWithSnapApiAndGlobalConfig(dataMockup.initDataMock());

        /*
          Snap Api Request with options config
          */
        RequestWithSnapApiAndConfigOptions(dataMockup.initDataMock());

        /*
          Api Request with MidtransSnapApi object and specific config
          */
        RequestWithMidtransSnapApiObject(dataMockup.initDataMock());
    }


    private static void RequestWithSnapApiAndGlobalConfig(Map<String, Object> request) throws MidtransError {
        /*
          Set configuration globally
          */
        Midtrans.serverKey = sandboxServerKey;
        Midtrans.clientKey = sandboxClientKey;

        /*
          Api Request with static method using Global Config
          */
        JSONObject result = SnapApi.createTransaction(request);
        System.out.println(result);
    }

    private static void RequestWithSnapApiAndConfigOptions(Map<String, Object> request) throws MidtransError {
        /*
          Initiate Config Object
          */
        Config configOptions = Config.builder()
                .setServerKey(sandboxServerKey)
                .setClientKey(sandboxClientKey)
                .setIsProduction(false)
                .build();

         /*
          Api request with static method using Config Options
          */
        JSONObject result = SnapApi.createTransaction(request, configOptions);
        System.out.println(result);
    }

    private static void RequestWithMidtransSnapApiObject(Map<String, Object> request) throws MidtransError {
        /*
          Initiate MidtransSnapApi Object with minimal config, also you can initiate config, with config builder.
          */
        MidtransSnapApi snapApi = new ConfigFactory(new Config(sandboxServerKey, sandboxClientKey, false)).getSnapApi();

        /*
          Api request to Midtrans API
          */
        JSONObject result = snapApi.createTransaction(request);
        System.out.println(result);
    }

}
