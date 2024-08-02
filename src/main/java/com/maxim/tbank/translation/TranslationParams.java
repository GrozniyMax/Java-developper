package com.maxim.tbank.translation;

import lombok.Data;
import lombok.NonNull;

/**
 * Класс для параметров перевода
 */
@Data
public class TranslationParams {
    @NonNull
    private String sourceLanguageCode;
    @NonNull
    private String targetLanguageCode;
    @NonNull
    private String text;
}
