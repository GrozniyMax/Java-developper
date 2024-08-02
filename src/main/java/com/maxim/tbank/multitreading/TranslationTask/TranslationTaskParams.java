package com.maxim.tbank.multitreading.TranslationTask;

import com.maxim.tbank.database.DatabaseManager;
import com.maxim.tbank.translation.Translator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class TranslationTaskParams {

    private Long position;

    private String text;

    private String sourceLanguageCode;

    private String targetLanguageCode;

    private Translator translator;

    private DatabaseManager databaseManager;
}
