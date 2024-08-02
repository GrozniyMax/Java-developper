package com.maxim.tbank.database;


import lombok.*;
import org.springframework.data.annotation.AccessType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class RequestDao {

    @Builder.Default
    private Long id = 0L;

    private String ip;

    /**
     * Каждая пара состоит из изначального слова и переведенного соответственно
     */
    @Singular("translation")
    private Map<String, String> translation = new HashMap<>();

    private String originLanguage;

    private String destinationLanguage;
}
