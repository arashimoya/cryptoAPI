package com.adam.crypto.remotedatasource;

import com.adam.crypto.entity.Quote;

import java.util.Optional;

public interface CoinAPIClient {
   Optional<Quote> getQuote(String currency);
}
