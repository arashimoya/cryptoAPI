package com.adam.crypto.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Rate {
    @JsonAlias("asset_id_quote")
    private final String assetIdQuote;
    private final BigDecimal rate;

    public Rate(String assetIdQuote, BigDecimal rate) {
        this.assetIdQuote = assetIdQuote;
        this.rate = rate;
    }

    public String getAssetIdQuote() {
        return assetIdQuote;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
