package com.maxim.tbank.controllers;

import lombok.Data;
import lombok.NonNull;

@Data
public class Request {

    @NonNull
    private String sourceLanguageCode;
    @NonNull
    private String targetLanguageCode;
    @NonNull
    private String text;
}
