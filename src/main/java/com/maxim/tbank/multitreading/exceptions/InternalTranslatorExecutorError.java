package com.maxim.tbank.multitreading.exceptions;

public class InternalTranslatorExecutorError extends Exception{

    public InternalTranslatorExecutorError() {
    }

    public InternalTranslatorExecutorError(String message) {
        super(message);
    }

    public InternalTranslatorExecutorError(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalTranslatorExecutorError(Throwable cause) {
        super(cause);
    }

    public InternalTranslatorExecutorError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
