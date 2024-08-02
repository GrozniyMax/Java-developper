package com.maxim.tbank.multitreading.TranslationTask;

import com.maxim.tbank.controllers.Request;
import com.maxim.tbank.translation.TranslationParams;
import com.maxim.tbank.translation.Translator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.util.Pair;

import java.util.Optional;
import java.util.concurrent.Callable;
@RequiredArgsConstructor
public class TranslationTask implements Callable<TranslationTaskResult> {
    @NonNull
    private TranslationTaskParams params;

    @Override
    public TranslationTaskResult call() throws Exception {
        // Раз каждый запрос к переводчику стоит денег, то будем сначала искать переведенное слово в базе данных
        // Поскольку мы по ТЗ и так сохраняем
        Optional<String> searchResult = params.getDatabaseManager()
                .searchForTranslation(params.getText(),
                        params.getSourceLanguageCode(),
                        params.getTargetLanguageCode());
//
//        System.out.println("!!!" + params.getText());

        if (searchResult.isPresent()) {
            return new TranslationTaskResult(params.getPosition(), params.getText(), searchResult.get());
        }else {
            return new TranslationTaskResult(params.getPosition(), params.getText(), params.getTranslator()
                    .translate(new TranslationParams(params.getSourceLanguageCode(),
                            params.getTargetLanguageCode(),
                            params.getText())));

        }
    }
}
