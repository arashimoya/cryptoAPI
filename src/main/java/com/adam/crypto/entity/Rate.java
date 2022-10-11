package com.adam.crypto.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rate {
    @JsonAlias("asset_id_quote")
    private String assetIdQuote;
    private float rate;

    public String getAssetIdQuote() {
        return assetIdQuote;
    }

    public float getRate() {
        return rate;
    }
}
