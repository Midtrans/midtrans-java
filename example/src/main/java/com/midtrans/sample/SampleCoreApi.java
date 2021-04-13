package com.midtrans.sample;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.sample.data.DataMockup;
import com.midtrans.service.MidtransCoreApi;
import org.json.JSONObject;

import java.util.Map;

import static com.midtrans.sample.data.Constant.sandboxClientKey;
import static com.midtrans.sample.data.Constant.sandboxServerKey;

public class SampleCoreApi {

    public static void main(String[] args) throws MidtransError {
        DataMockup dataMockup = new DataMockup();
        dataMockup.setPaymentType("gopay");

        /*
          Core Api Request with global config
          */
        RequestWithCoreApiAndGlobalConfig(dataMockup.initDataMock());

        /*
          Core Api Request with options config
          */
        RequestWithCoreApiAndConfigOptions(dataMockup.initDataMock());

        /*
          Api Request with MidtransCoreApi object and specific config
          */
        RequestWithMidtransCoreApiObject(dataMockup.initDataMock());
    }


    private static void RequestWithCoreApiAndGlobalConfig(Map<String, Object> request) throws MidtransError {
        /*
          Set configuration globally
          */
        Midtrans.serverKey = sandboxServerKey;
        Midtrans.clientKey = sandboxClientKey;

        /*
          Api Request with static method using Global Config
          */
        JSONObject result = CoreApi.chargeTransaction(request);
        System.out.println(result);
    }

    private static void RequestWithCoreApiAndConfigOptions(Map<String, Object> request) throws MidtransError {
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

        JSONObject result = CoreApi.chargeTransaction(request, configOptions);
        System.out.println(result);
    }

    private static void RequestWithMidtransCoreApiObject(Map<String, Object> request) throws MidtransError {
        /*
          Initiate MidtransCoreApi Object with minimal config, also you can initiate config, with config builder.
          */
        MidtransCoreApi coreApi = new ConfigFactory(new Config(sandboxServerKey, sandboxClientKey, false)).getCoreApi();

        /*
          Api request to Midtrans API
          */
        JSONObject result = coreApi.chargeTransaction(request);
        System.out.println(result);
    }

}
