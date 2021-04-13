package com.midtrans.sample;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.IrisApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransIrisApi;
import org.json.JSONObject;

import static com.midtrans.sample.data.Constant.*;

public class SampleIrisApi {

    public static void main(String[] args) throws MidtransError {
        /*
          Iris Api Request with options config
          */
        RequestWithIrisApiAndConfigOptions();

        /*
          Api Request via MidtransIrisApi object with minimal config
          */
        RequestWithMidtransIrisApiObject();
    }


    private static void RequestWithIrisApiAndConfigOptions() throws MidtransError {
          /*
          Initiate Config Object
          */
        Config configOptions = Config.builder()
                .setServerKey(sandboxCreatorKey)
                .setIsProduction(false)
                .build();

         /*
          Api request with static method using Config Options
          */
        JSONObject result = IrisApi.getBalance(configOptions);
        System.out.println(result);
    }


    private static void RequestWithMidtransIrisApiObject() throws MidtransError {
        /*
          Initiate MidtransSnapApi Object with minimal config, also you can initiate config with config builder.
          */
        MidtransIrisApi irisApi = new ConfigFactory(new Config(sandboxCreatorKey, null, false)).getIrisApi();

        /*
          Api request to Midtrans API
          */
        JSONObject result = irisApi.getBalance();
        System.out.println(result);
    }
}
