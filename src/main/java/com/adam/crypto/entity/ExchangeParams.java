package com.adam.crypto.entity;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ExchangeParams {
    @JsonAlias("from")
    private String currencyFrom;
    @JsonAlias("to")
    private String[] currenciesTo;
    private float amount;

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public String[] getCurrenciesTo() {
        return currenciesTo;
    }

    public float getAmount() {
        return amount;
    }
}
