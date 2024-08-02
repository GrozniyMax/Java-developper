package com.maxim.tbank.controllers.exception;

public class MissedSomePropepry extends Exception{

    public MissedSomePropepry() {
    }

    public MissedSomePropepry(String message) {
        super(message);
    }

    public MissedSomePropepry(String message, Throwable cause) {
        super(message, cause);
    }
}
