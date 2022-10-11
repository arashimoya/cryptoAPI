package com.adam.crypto.entity;

import lombok.Builder;

@Builder
public final class CurrencyTo {
    private final String currencyName;
    private final float rate;
    private final double fee;
    private final float result;

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
