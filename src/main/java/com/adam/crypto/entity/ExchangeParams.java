package com.adam.crypto.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.NonNull;

public final class ExchangeParams {
    @JsonAlias("from")
    @NonNull
    private final String currencyFrom;
    @JsonAlias("to")
    @NonNull
    private final String[] currenciesTo;
    @NonNull
    private final float amount;

    public ExchangeParams(String currencyFrom, String[] currenciesTo, float amount) {
        this.currencyFrom = currencyFrom;
        this.currenciesTo = currenciesTo;
        this.amount = amount;
    }

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
