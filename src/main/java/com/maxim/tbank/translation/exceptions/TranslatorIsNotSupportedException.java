package com.maxim.tbank.translation.exceptions;

public class TranslatorIsNotSupportedException extends Exception{

    public TranslatorIsNotSupportedException() {
    }

    public TranslatorIsNotSupportedException(String message) {
        super(message);
    }
}
