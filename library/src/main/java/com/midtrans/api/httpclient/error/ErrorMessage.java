package com.midtrans.api.httpclient.error;

import java.util.ArrayList;

public class ErrorMessage {
    private ArrayList errorMessages;

    public ArrayList getErrorMessages() {
        return errorMessages;
    }

    private ErrorMessage(final Builder builder) {
        errorMessages = builder.errorMessages;
    }

    public static class Builder {
        private ArrayList errorMessages;

        public Builder() {
        }

        public Builder errorMessage(final ArrayList errorMessages) {
            this.errorMessages = errorMessages;
            return this;
        }

        public Builder defaultError() {
            this.errorMessages.add("Unknown ERROR");
            return this;
        }

        public ErrorMessage build() {
            return new ErrorMessage(this);
        }

    }
}
