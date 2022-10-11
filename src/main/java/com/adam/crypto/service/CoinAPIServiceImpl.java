package com.adam.crypto.service;


import com.adam.crypto.entity.Exchange;
import com.adam.crypto.entity.ExchangeParams;
import com.adam.crypto.entity.Quote;
import com.adam.crypto.handler.RestTemplateErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import org.springframework.http.HttpHeaders.*;
import java.time.Duration;

@Service
public class CoinAPIServiceImpl  implements CoinAPIService{

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);



    private RestTemplate restTemplate;

    @Autowired
    public CoinAPIServiceImpl(@Value("${app.api-key}") String API_KEY, RestTemplateBuilder restTemplateBuilder){
        restTemplate = restTemplateBuilder
                .defaultHeader("X-CoinAPI-Key", API_KEY)
                .errorHandler(new RestTemplateErrorHandler())
                .build();
    }



    @Override
    public Quote getQuote(String currency) {

        return restTemplate.getForObject("http://rest.coinapi.io/v1/exchangerate/" + currency, Quote.class);
    }



}
