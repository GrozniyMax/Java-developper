package com.maxim.tbank.translation.translators.Yandex;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;


@Data
class Responce{
    List<SubResponceEntity> translations;

    @Data
    static class SubResponceEntity{
        private String text;
        private Optional<String> detectedLanguageCode;
    }

}


