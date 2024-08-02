package com.maxim.tbank.controllers;

import com.maxim.tbank.database.exception.DatabaseException;
import com.maxim.tbank.multitreading.TranslateExecutor;
import com.maxim.tbank.multitreading.exceptions.InternalTranslatorExecutorError;
import com.maxim.tbank.translation.Language;
import com.maxim.tbank.translation.Translators;
import com.maxim.tbank.translation.exceptions.LanguageIsNotSupported;
import com.maxim.tbank.translation.exceptions.TranslatorIsNotSupportedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/translator")
public class TranslationController {

    @Autowired
    private TranslateExecutor executor;

    @Autowired
    private Translators translators;


    @GetMapping("/{translator-name}/languages")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Language> getLanguages(@PathVariable("translator-name") String translatorName) throws TranslatorIsNotSupportedException {
        System.out.println(translatorName);
        return translators.getTranslator(translatorName).getSupportedLanguages().stream().toList();
    }

    @PostMapping("/{translator-name}/translate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getTranslation(@PathVariable("translator-name") String translatorName, @RequestBody Request body, HttpServletRequest request) throws TranslatorIsNotSupportedException, LanguageIsNotSupported, InternalTranslatorExecutorError, DatabaseException {
//        return executor.translateAndSave(body,translatorName,request.getRemoteAddr());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(String.format("{\"text\":\"%s\"}", executor.translateAndSave(body,translatorName,request.getRemoteAddr())));
    }

    @ExceptionHandler(TranslatorIsNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleException(TranslatorIsNotSupportedException e) {
        return e.getMessage() + " translator is not supported";
    }

    @ExceptionHandler(LanguageIsNotSupported.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleException(LanguageIsNotSupported e){
        return e.getMessage() + " language(s) is/are not supported";
    }

    @ExceptionHandler({DatabaseException.class, InternalTranslatorExecutorError.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody String handleException(Exception e) {
        return String.format("Error: %s with messaage: %s.", e.getClass(), e.getMessage());
    }
}
