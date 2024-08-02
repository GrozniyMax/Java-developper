package com.maxim.tbank.translation.exceptions;

public class LanguageIsNotSupported extends Exception{

    public LanguageIsNotSupported() {
    }

    public LanguageIsNotSupported(String message) {
        super(message);
    }
}
