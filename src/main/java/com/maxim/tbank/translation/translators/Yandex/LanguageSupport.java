package com.maxim.tbank.translation.translators.Yandex;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxim.tbank.translation.Language;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@ToString
@Getter
class LanguageSupport  {
    @JsonProperty("languages")
    List<YaLanguage> languages;

    @ToString
    static class YaLanguage implements Language {
        private String code;
        private Optional<String> name = Optional.empty();

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public Optional<String> getName() {
            return name;
        }
    }
}
