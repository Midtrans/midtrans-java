package com.midtrans.httpclient.error;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

import java.util.ArrayList;

/**
 * ErrorUtils parse error from midtrans
 */
@Deprecated
public class ErrorUtils {

    private static MidtransError parseError(final Response<?> response) {
        JSONObject bodyObj;
        ArrayList<Object> errorMessages = new ArrayList<>();
        try {
            assert response.errorBody() != null;
            String errorBody = response.errorBody().string();
            if (errorBody != null) {
                try {
                    bodyObj = new JSONObject(errorBody);
                    if (bodyObj.has("error_messages")) {
                        JSONArray errors = bodyObj.getJSONArray("error_messages");
                        for (int i = 0; i < errors.length(); i++) {
                            errorMessages.add(errors.get(i));
                        }
                    }
                } catch (JSONException e) {
                    throw new MidtransError(e.getMessage());
                }
            } else {
                return new MidtransError.Builder()
                        .defaultError()
                        .build();
            }
        } catch (Exception e) {
            try {
                throw new MidtransError(e.getMessage());
            } catch (MidtransError midtransError) {
                midtransError.printStackTrace();
            }
        }
        return new MidtransError.Builder()
                .errorMessage(errorMessages)
                .build();
    }

    /**
     * catch http error message from Midtrans
     *
     * @param code     http status code from HttpClient response (Retrofit)
     * @param response Retrofit response from midtrans
     */
    public void catchHttpErrorMessage(int code, Response response) {
        MidtransError midtransError = parseError(response);
        switch (code) {
            case 400:
                System.out.println("400 Bad Request: There was a problem in the JSON you submitted " + midtransError.getErrorMessages());
                break;
            case 401:
                System.out.println("401 Unauthorized: " + midtransError.getErrorMessages());
                return;
            case 404:
                System.out.println("404 Not Found " + midtransError.getErrorMessages());
                break;
            case 500:
                System.out.println("HTTP ERROR 500: Internal Server ERROR! " + midtransError.getErrorMessages());
                break;
            default:
                System.out.println(midtransError.getErrorMessages().toString());
                break;
        }
    }
}
