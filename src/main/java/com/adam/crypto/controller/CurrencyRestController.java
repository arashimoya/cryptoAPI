package com.adam.crypto.controller;


import com.adam.crypto.entity.*;
import com.adam.crypto.service.CoinAPIService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/currencies")
public final class CurrencyRestController {

    private final CoinAPIService coinAPIService;

    @Autowired
    public CurrencyRestController(CoinAPIService coinAPIService) {
        this.coinAPIService = coinAPIService;
    }

    @GetMapping("/{currency}")
    public Quote getQuotes(@PathVariable String currency, @RequestParam(required = false) String[] filter){

        if(currency==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return coinAPIService.getQuote(currency, filter);

    }

    @PostMapping("/exchange")
    public Exchange getExchangePrognosis(@RequestBody ExchangeParams exchangeParams){
        if(exchangeParams==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if(exchangeParams.getCurrencyFrom() == null || exchangeParams.getCurrenciesTo() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }else {
            Exchange exchange = coinAPIService.getExchange(exchangeParams);
            System.out.println(exchange.toString());
            return exchange;
        }


    }

}
