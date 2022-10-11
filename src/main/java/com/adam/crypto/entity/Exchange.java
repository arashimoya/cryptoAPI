package com.adam.crypto.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;

import java.util.List;

@Builder
public class Exchange {
    private String currencyFromName;
    private float amount;
    private List<CurrencyTo> currencyToList;


    public String getCurrencyFromName() {
        return currencyFromName;
    }

    public List<CurrencyTo> getCurrencyToList() {
        return currencyToList;
    }

    public float getAmount() {
        return amount;
    }
}

