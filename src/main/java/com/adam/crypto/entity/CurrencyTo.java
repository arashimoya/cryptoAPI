package com.adam.crypto.entity;

import lombok.Builder;

@Builder
public class CurrencyTo {
    private String currencyName;
    private float rate;
    private double fee;
    private float result;

    public String getCurrencyName() {
        return currencyName;
    }

    public float getRate() {
        return rate;
    }

    public double getFee() {
        return fee;
    }

    public float getResult() {
        return result;
    }
}
