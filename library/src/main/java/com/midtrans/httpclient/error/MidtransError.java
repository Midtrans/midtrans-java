package com.midtrans.httpclient.error;

import lombok.Getter;
import okhttp3.Response;

/**
 * MidtransError class to catch error messages
 */
@Getter
public class MidtransError extends Exception {

    private String message;
    private Integer statusCode;
    private String responseBody;
    private Response response;

    /**
     * Constructs a Midtrans exception with the message
     *
     * @param message
     */
    public MidtransError(String message) {
        super(message);
        this.message = message;
    }

    /**
     *  Constructs a Midtrans exception with the specified cause
     *
     * @param cause
     */
    public MidtransError(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a Midtrans exception with message and specified cause
     *
     * @param message
     * @param cause
     */
    public MidtransError(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    /**
     * Constructs a Midtrans exception with the specified details.
     *
     * @param message
     * @param statusCode
     * @param data
     * @param response
     */
    public MidtransError(String message, Integer statusCode, String data, Response response) {
        this(message, statusCode, data, response, null);
    }

    /**
     * Constructs a Midtrans exception with the specified details.
     *
     * @param message
     * @param statusCode
     * @param responseBody
     * @param response
     * @param e
     */
    public MidtransError(String message, Integer statusCode, String responseBody, Response response, Throwable e) {
        super(message, e);
        this.message = message;
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.response = response;
    }

}
