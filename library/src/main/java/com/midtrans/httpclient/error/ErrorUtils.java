package com.midtrans.httpclient.error;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

import java.util.ArrayList;

/**
 * ErrorUtils parse error from midtrans
 */
public class ErrorUtils {

    private static ErrorMessage parseError(final Response<?> response) {
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
                    e.printStackTrace();
                }
            } else {
                return new ErrorMessage.Builder()
                        .defaultError()
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ErrorMessage.Builder()
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
        ErrorMessage errorMessage = parseError(response);
        switch (code) {
            case 400:
                System.out.println("400 Bad Request: There was a problem in the JSON you submitted " + errorMessage.getErrorMessages());
                break;
            case 401:
                System.out.println("401 Unauthorized: " + errorMessage.getErrorMessages());
                return;
            case 404:
                System.out.println("404 Not Found " + errorMessage.getErrorMessages());
                break;
            case 500:
                System.out.println("HTTP ERROR 500: Internal Server ERROR! " + errorMessage.getErrorMessages());
                break;
            default:
                System.out.println(errorMessage.getErrorMessages().toString());
                break;
        }
    }
}
