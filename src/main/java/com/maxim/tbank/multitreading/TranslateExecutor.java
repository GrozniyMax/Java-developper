package com.maxim.tbank.multitreading;

import com.maxim.tbank.controllers.Request;
import com.maxim.tbank.database.DatabaseManager;
import com.maxim.tbank.database.RequestDao;
import com.maxim.tbank.database.exception.DatabaseException;
import com.maxim.tbank.multitreading.TranslationTask.TranslationParamsFactory;
import com.maxim.tbank.multitreading.TranslationTask.TranslationTask;
import com.maxim.tbank.multitreading.TranslationTask.TranslationTaskResult;
import com.maxim.tbank.multitreading.exceptions.InternalTranslatorExecutorError;
import com.maxim.tbank.translation.Translators;
import com.maxim.tbank.translation.exceptions.LanguageIsNotSupported;
import com.maxim.tbank.translation.exceptions.TranslatorIsNotSupportedException;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
public class TranslateExecutor {

    @Autowired
    private Translators translators;

    @Autowired
    private DatabaseManager databaseManager;

    private TranslationParamsFactory paramsFactory;

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    @PostConstruct
    public void init() {
        paramsFactory = new TranslationParamsFactory(databaseManager);
    }

    public String translateAndSave(Request request, String translatorName, String ip)
            throws TranslatorIsNotSupportedException, DatabaseException, LanguageIsNotSupported, InternalTranslatorExecutorError {

        log.info("Started translating new request");

        paramsFactory.clear(request.getSourceLanguageCode(),
                request.getTargetLanguageCode(),
                translators.getTranslator(translatorName));


        RequestDao.RequestDaoBuilder daoBuilder = RequestDao.builder()
                .ip(ip)
                .originLanguage(request.getSourceLanguageCode())
                .destinationLanguage(request.getTargetLanguageCode());

        String[] splitted = request.getText().split(" +");
        List<String> translationResultList = new ArrayList<>(Collections.nCopies(splitted.length, ""));
         List<Future< TranslationTaskResult >> futures = Arrays.stream(splitted)
                 .map(paramsFactory::createParams)
                 .map(TranslationTask::new)
                 .map(task -> executor.submit(task)).toList();

         TranslationTaskResult taskResult;
         for (Future< TranslationTaskResult > future : futures) {
             try {
                 taskResult = future.get();
                 daoBuilder.translation(taskResult.sourceText(), taskResult.translatedText());
                 translationResultList.set(taskResult.position().intValue(), taskResult.translatedText());
             } catch (ExecutionException e){
                 if (e.getCause() instanceof LanguageIsNotSupported){
                     throw (LanguageIsNotSupported) e.getCause();
                 }else {
                     e.getCause().printStackTrace();
                     throw new InternalTranslatorExecutorError(e.getCause());
                 }
             } catch (InterruptedException|CancellationException exception){
                 throw new InternalTranslatorExecutorError(exception);
             }
         }

         if (translationResultList.contains("")) throw new InternalTranslatorExecutorError("Some shit");

        StringBuilder resultBuilder = new StringBuilder();

        translationResultList.forEach((t)->resultBuilder.append(t).append(" "));

        //Поскольку ограничение всего на 10 потоков то, чтобы не дожидаться результата записи в бд
        // и не выйти за рамки ограничения по потокам, используем тот же executor
        executor.submit(()->databaseManager.save(daoBuilder.build()));

        log.info("Finished translating new request");
        return resultBuilder.toString();
    }


}
