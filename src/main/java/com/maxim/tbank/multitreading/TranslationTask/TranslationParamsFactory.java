package com.maxim.tbank.multitreading.TranslationTask;

import com.maxim.tbank.controllers.Request;
import com.maxim.tbank.database.DatabaseManager;
import com.maxim.tbank.translation.TranslationParams;
import com.maxim.tbank.translation.Translator;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
/**
 * Фабрика параметров для {@link TranslationTask}
 *
 */
public class TranslationParamsFactory {

    private Long wordCounter = 0L;


    private String sourceLanguage;


    private String destinationLanguage;


    private Translator translator;


    private DatabaseManager databaseManager;

    public TranslationParamsFactory(@NonNull DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Очищает фабрику для новых запросов <br>
     * <strong>Обязательно вызвать перед началом работы с новым запросом</strong>
     * @param sourceLanguage язык исходного текста
     * @param destinationLanguage язык переведенного текста
     */
    public void clear(String sourceLanguage, String destinationLanguage, Translator translator) {
        this.sourceLanguage = sourceLanguage;
        this.destinationLanguage = destinationLanguage;
        this.translator = translator;
        wordCounter=0L;

    }

    private Long getNextPosition(){
        return wordCounter++;
    }


    public TranslationTaskParams createParams(String text) {
        return TranslationTaskParams.builder()
                .sourceLanguageCode(sourceLanguage)
                .targetLanguageCode(destinationLanguage)
                .databaseManager(databaseManager)
                .position(getNextPosition())
                .translator(translator)
                .text(text)
                .build();
    }


}
