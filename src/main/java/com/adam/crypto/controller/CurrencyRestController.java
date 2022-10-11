package com.adam.crypto.controller;


import com.adam.crypto.entity.*;
import com.adam.crypto.service.CoinAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/currencies")
public class CurrencyRestController {

    private final CoinAPIService coinAPIService;

    @Autowired
    public CurrencyRestController(CoinAPIService coinAPIService) {
        this.coinAPIService = coinAPIService;
    }

    @GetMapping("/{currency}")
    public Quote getQuotes(@PathVariable String currency, @RequestParam(required = false) String[] filter){
        Quote theQuote = coinAPIService.getQuote(currency);
        if(filter !=null && filter.length !=0){
           theQuote.filterRates(filter);
        }

        return theQuote;

    }

    @PostMapping("/exchange")
    public Exchange getExchangePrognosis(@RequestBody ExchangeParams exchangeParams){
        Quote theQuote = coinAPIService.getQuote(exchangeParams.getCurrencyFrom());
        theQuote.filterRates(exchangeParams.getCurrenciesTo());

        List<CurrencyTo> currencyToList = theQuote.getRates().stream().map(rate -> CurrencyTo.builder()
                .currencyName(rate.getAssetIdQuote())
                .fee(rate.getRate() * 0.01)
                .rate(rate.getRate())
                .result((exchangeParams.getAmount()* rate.getRate()))
                .build()).collect(Collectors.toList());

        return Exchange.builder()
                .currencyToList(currencyToList)
                .currencyFromName(exchangeParams.getCurrencyFrom())
                .amount(exchangeParams.getAmount())
                .build();


    }

}
