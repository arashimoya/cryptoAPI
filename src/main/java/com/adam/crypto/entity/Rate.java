package com.adam.crypto.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Rate {
    @JsonAlias("asset_id_quote")
    private final String assetIdQuote;
    private final float rate;

    public Rate(String assetIdQuote, float rate) {
        this.assetIdQuote = assetIdQuote;
        this.rate = rate;
    }

    public String getAssetIdQuote() {
        return assetIdQuote;
    }

    public float getRate() {
        return rate;
    }
}
