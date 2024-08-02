package com.maxim.tbank.translation;

import com.maxim.tbank.translation.exceptions.TranslatorIsNotSupportedException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class Translators {

    @Autowired
    ApplicationContext applicationContext;

    private Map<String, Translator> translators = new HashMap<>();


    @PostConstruct
    public void init() {
        applicationContext.getBeansOfType(Translator.class).values().forEach(
                translator -> {
                    if (translator.isEnabled()) {
                        translators.put(translator.getName(), translator);
                        log.info("Translator '{}' enabled", translator.getName());
                    }else {
                        log.warn(translator.getName() + " is disabled");
                    }
                }
        );
    }

    public Translator getTranslator(String name) throws TranslatorIsNotSupportedException {
        if (!translators.containsKey(name)) throw new TranslatorIsNotSupportedException(name);
        return translators.get(name);
    }

}
