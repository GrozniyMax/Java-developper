package com.maxim.tbank.translation;

import com.maxim.tbank.translation.exceptions.LanguageIsNotSupported;

import java.util.Collection;

/**
 *  Базовый интерфейс для всех переводчиков <br>
 *  Все наследники автоматически добавляются в менеджер переводчиков {@code TranslatorManager}
 * @see Translators
 */
public interface Translator {

    /**
     * Returns name of translator provider (such as Google, Yandex etc)
     * @return name in lower case
     */
    String getName();

    String translate(TranslationParams params) throws LanguageIsNotSupported;

    Boolean checkIfLanguageSupported(String languageCode) ;

    Collection<Language> getSupportedLanguages();

    Boolean isEnabled();
}
