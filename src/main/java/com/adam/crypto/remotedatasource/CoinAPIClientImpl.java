package com.adam.crypto.remotedatasource;

import com.adam.crypto.entity.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public final class CoinAPIClientImpl implements CoinAPIClient{

    private final RestTemplate restTemplate;
    private final String URI = "http://rest.coinapi.io/v1/exchangerate/";

    @Autowired
    public CoinAPIClientImpl(@Lazy RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<Quote> getQuote(String currency) {
        Quote theQuote = restTemplate.getForObject(URI + currency.toUpperCase(), Quote.class);

        return Optional.of(theQuote);
    }

}
