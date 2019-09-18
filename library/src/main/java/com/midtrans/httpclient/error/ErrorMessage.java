package com.midtrans.httpclient.error;

import java.util.ArrayList;

/**
 * ErrorMessage class to catch error messages
 */
public class ErrorMessage {
    private ArrayList errorMessages;

    /**
     * get error messages
     *
     * @return errorMessage ArrayList
     */
    public ArrayList getErrorMessages() {
        return errorMessages;
    }

    private ErrorMessage(final Builder builder) {
        errorMessages = builder.errorMessages;
    }

    /**
     * ErrorMessage Builder
     */
    public static class Builder {
        private ArrayList errorMessages;

        public Builder() {
        }

        /**
         * Set error message
         *
         * @param errorMessages error message from Midtrans error response
         * @return {@link ErrorMessage.Builder ErrorMesage.Builder}
         */
        public Builder errorMessage(final ArrayList errorMessages) {
            this.errorMessages = errorMessages;
            return this;
        }

        /**
         * Set default error
         *
         * @return {@link ErrorMessage.Builder ErrorMesage.Builder}
         */
        public Builder defaultError() {
            this.errorMessages.add("Unknown ERROR");
            return this;
        }

        /**
         * Build ErrorMessage object from builder
         *
         * @return {@link ErrorMessage ErrorMessage}
         */
        public ErrorMessage build() {
            return new ErrorMessage(this);
        }

    }
}
