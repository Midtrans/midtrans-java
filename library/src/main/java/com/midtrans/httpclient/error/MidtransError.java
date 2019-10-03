package com.midtrans.httpclient.error;

import java.util.ArrayList;

/**
 * MidtransError class to catch error messages
 */
public class MidtransError extends Exception {

    private ArrayList errorMessages;

    /**
     * get error messages
     *
     * @return errorMessage ArrayList
     */
    public ArrayList getErrorMessages() {
        return errorMessages;
    }

    public MidtransError(String message) {
        super(message);
    }

    public MidtransError(Throwable cause) {
        super(cause);
    }

    public MidtransError(String message, Throwable cause) {
        super(message, cause);
    }

    private MidtransError(final Builder builder) {
        errorMessages = builder.errorMessages;
    }

    /**
     * MidtransError Builder
     */
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
