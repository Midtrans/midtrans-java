package com.midtrans.api.service.impl;

import com.midtrans.api.Config;
import com.midtrans.api.httpclient.APIHttpClient;
import com.midtrans.api.httpclient.CoreApi;
import com.midtrans.api.service.MidtransCoreApi;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class MidtransCoreApiImpl implements MidtransCoreApi {
    private static final Logger LOGGER = Logger.getLogger(MidtransCoreApi.class.getName());

    private CoreApi coreApi;
    private APIHttpClient httpClient;

    public MidtransCoreApiImpl(Config config) {
        this.httpClient = new APIHttpClient(config);
        this.coreApi = this.httpClient.getClient().create(CoreApi.class);
    }

    //Http Handle Method for handle HTTP Response
    private JSONObject httpHandle(Call<ResponseBody> call) {
        JSONObject object = new JSONObject();
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                try {
                    if (response.body() != null) {
                        object = new JSONObject(response.body().string());
                        LOGGER.info("Midtrans response: " + object);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                httpClient.httpErrorHandle(response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public JSONObject chargeTransaction(Map<String, Object> body) {
        Call<ResponseBody> call = coreApi.chargeTransaction(body);
        return httpHandle(call);
    }

    @Override
    public JSONObject checkTransaction(String orderId) {
        Call<ResponseBody> call = coreApi.checkTransaction(orderId);
        return httpHandle(call);
    }

    @Override
    public JSONObject approveTransaction(String orderId) {
        Call<ResponseBody> call = coreApi.approveTransaction(orderId);
        return httpHandle(call);
    }

    @Override
    public JSONObject cancelTransaction(String orderId) {
        Call<ResponseBody> call = coreApi.cancelTransaction(orderId);
        return httpHandle(call);
    }

    @Override
    public JSONObject expireTransaction(String orderId) {
        Call<ResponseBody> call = coreApi.expireTransaction(orderId);
        return httpHandle(call);
    }

    @Override
    public JSONObject refundTransaction(String orderId, Map<String, String> body) {
        //config.getCoreApi();
        //CoreApi coreApi = httpClient.getClient().create(CoreApi.class);
        Call<ResponseBody> call = coreApi.refundTransaction(orderId, body);
        return httpHandle(call);
    }


}
