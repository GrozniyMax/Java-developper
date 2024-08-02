package com.maxim.tbank.translation.translators.Yandex;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
class RequestToYandex {

    @NonNull
    private String folderId;

    private String sourceLanguageCode;

    private String targetLanguageCode;

    @Builder.Default
    private final String format = "PLAIN_TEXT";

    @Singular
    private List<String> texts = new ArrayList<>();

//        @Builder
//        public Request(String sourceLanguageCode, String targetLanguageCode, String text){
//            this.sourceLanguageCode = sourceLanguageCode;
//            this.targetLanguageCode = targetLanguageCode;
//            this.texts.add(text);
//        }
}