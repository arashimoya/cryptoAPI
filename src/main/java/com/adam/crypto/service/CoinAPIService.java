package com.adam.crypto.service;

import com.adam.crypto.entity.Exchange;
import com.adam.crypto.entity.ExchangeParams;
import com.adam.crypto.entity.Quote;
import org.springframework.stereotype.Service;



@Service
public interface CoinAPIService {

    Quote getQuote(String currency);

}
