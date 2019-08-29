package com.midtrans.api.httpclient;

import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.Map;

public interface SnapApi {

    @POST("transactions")
    Call<ResponseBody> generateToken(@Body Map<String, Object> objectMap);

    @POST("transactions")
    Call<JSONObject> genToken(@Body Map<String, Object> objectMap);

}
