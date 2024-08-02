package com.maxim.tbank.translation.translators.Yandex;

import com.maxim.tbank.controllers.Request;
import com.maxim.tbank.translation.Language;
import com.maxim.tbank.translation.TranslationParams;
import com.maxim.tbank.translation.Translator;
import com.maxim.tbank.translation.exceptions.LanguageIsNotSupported;
import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
public class YandexTranslator implements Translator {


    @Value("${yandex.translator.IAM-token}")
    private String token;

    @Value("${yandex.translator.folder-id}")
    private String folderId;

    @Value("${yandex.translator.translate-url}")
    private String translateUrl;

    @Value("${yandex.translator.languages-url}")
    private String languagesUrl;

    private HttpHeaders headers = new HttpHeaders();

    private Map<String, Language> supportedLanguages = new HashMap<>();

    private Boolean isEnabled = false;

    @PostConstruct
    public void init() {
        //Устанавливаем заголовки т.к они одинаковые для каждого запроса к Яндекс переводчику
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

//        System.out.println("!!!!"+languagesUrl);
        RestTemplate restTemplate = new RestTemplate();
        String body = String.format("{\"folderId\":\"%s\"}", folderId);
//        System.out.println(body);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<LanguageSupport> response = restTemplate.exchange(languagesUrl, HttpMethod.POST, request, LanguageSupport.class);
            response.getBody().getLanguages().forEach((yaLanguage) -> supportedLanguages.put(yaLanguage.getCode(), yaLanguage));
            isEnabled = true;
        } catch (RestClientException e) {
            if (e.getMessage().contains("401")){
                log.error("Token is invalid/expired or folderId is invalid", e);
            }else {
                log.error("Error occurred while trying to initialize Yandex translator", e);
            }
        }
    }

    @Override
    public String getName() {
        return "yandex";
    }

    @Override
    public String translate(TranslationParams params) throws LanguageIsNotSupported {

        if (!(checkIfLanguageSupported(params.getSourceLanguageCode())&&
                checkIfLanguageSupported(params.getTargetLanguageCode())))
            throw new LanguageIsNotSupported(params.getSourceLanguageCode() + " and " + params.getTargetLanguageCode());
        
        
        RestTemplate template = new RestTemplate();
//        System.out.println("url: "+url);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + token);
//
//        headers.entrySet().forEach(System.out::println);

        RequestToYandex requestToYandex = RequestToYandex.builder()
                .folderId(folderId)
                .sourceLanguageCode(params.getSourceLanguageCode())
                .targetLanguageCode(params.getTargetLanguageCode())
                .text(params.getText())
                .build();
        

        HttpEntity<RequestToYandex> requestEntity = new HttpEntity<>(requestToYandex, headers);
//
//        System.out.println(template.postForEntity(translateUrl,requestEntity,String.class).getBody());

        ResponseEntity<Responce> response = template.exchange(translateUrl, HttpMethod.POST, requestEntity, Responce.class);

        return response.getBody().translations.get(0).getText();
    }

    @Override
    public Boolean checkIfLanguageSupported(String languageCode) {
        return supportedLanguages.containsKey(languageCode);
    }

    @Override
    public Collection<Language> getSupportedLanguages() {
        return supportedLanguages.values();
    }

    @Override
    public Boolean isEnabled() {
        return isEnabled;
    }


}
