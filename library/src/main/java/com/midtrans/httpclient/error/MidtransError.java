package com.midtrans.httpclient.error;

import lombok.Getter;
import okhttp3.Response;

import java.util.ArrayList;

/**
 * MidtransError class to catch error messages
 */
@Getter
public class MidtransError extends Exception {

    @Deprecated
    private ArrayList errorMessages;

    private String message;
    private Integer statusCode;
    private String responseBody;
    private Response response;

    public MidtransError(String message) {
        super(message);
    }

    public MidtransError(Throwable cause) {
        super(cause);
    }

    public MidtransError(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new Midtrans exception with the specified details.
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
     * Constructs a new Midtrans exception with the specified details.
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

    @Deprecated
    private MidtransError(final Builder builder) {
        errorMessages = builder.errorMessages;
    }

    /**
     * get error messages
     *
     * @return errorMessage ArrayList
     */
    @Deprecated
    public ArrayList getErrorMessages() {
        return errorMessages;
    }

    /**
     * MidtransError Builder
     */
    @Deprecated
    public static class Builder {
        private ArrayList errorMessages;

        public Builder() {
        }

        /**
         * Set error message
         *
         * @param errorMessages error message from Midtrans error response
         * @return {@link MidtransError.Builder ErrorMesage.Builder}
         */
        public Builder errorMessage(final ArrayList errorMessages) {
            this.errorMessages = errorMessages;
            return this;
        }

        /**
         * Set default error
         *
         * @return {@link MidtransError.Builder ErrorMesage.Builder}
         */
        public Builder defaultError() {
            this.errorMessages.add("Unknown ERROR");
            return this;
        }

        /**
         * Build MidtransError object from builder
         *
         * @return {@link MidtransError MidtransError}
         */
        public MidtransError build() {
            return new MidtransError(this);
        }

    }
}
