package com.adam.crypto.entity;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public final class CurrencyTo {
    private final String currencyName;
    private final BigDecimal rate;
    private final BigDecimal fee;
    private final BigDecimal result;

    public String getCurrencyName() {
        return currencyName;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public BigDecimal getResult() {
        return result;
    }
}
