package com.adam.crypto.service;


import com.adam.crypto.entity.CurrencyTo;
import com.adam.crypto.entity.Exchange;
import com.adam.crypto.entity.ExchangeParams;
import com.adam.crypto.entity.Quote;

import com.adam.crypto.remotedatasource.CoinAPIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public final class CoinAPIServiceImpl  implements CoinAPIService{

    private final CoinAPIClient coinAPIClient;

    @Autowired
    public CoinAPIServiceImpl(CoinAPIClient coinAPIClient){
        this.coinAPIClient = coinAPIClient;

    }

    @Override
    public Quote getQuote(String currency, List<String> filter) {

        Optional<Quote> opt = coinAPIClient.getQuote(currency);
        Quote filteredQuote;
        if(opt.isPresent()){
            filteredQuote = opt.get();
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if(filter !=null ){
            filteredQuote = filteredQuote.filterRates(
                    filter.stream().map(String::toUpperCase).toArray(String[]::new));
        }

        return filteredQuote;
    }
    @Override
    public Exchange getExchange(ExchangeParams exchangeParams) {
        Optional<Quote> optQuote = coinAPIClient.getQuote(exchangeParams.getCurrencyFrom());
        if(optQuote.isPresent()) {
            Quote filteredQuote = optQuote.get().filterRates(exchangeParams.getCurrenciesTo());

            List<CurrencyTo> currencyToList = filteredQuote.getRates().stream().map(rate ->
                    CurrencyTo.builder()
                    .currencyName(rate.getAssetIdQuote())
                    .fee(rate.getRate().multiply(BigDecimal.valueOf(0.01)))
                    .rate(rate.getRate())
                    .result((rate.getRate().multiply(BigDecimal.valueOf(exchangeParams.getAmount()))))
                    .build()).collect(Collectors.toList());

            return Exchange.builder()
                    .currencyToList(currencyToList)
                    .currencyFromName(exchangeParams.getCurrencyFrom())
                    .amount(exchangeParams.getAmount())
                    .build();
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


}
