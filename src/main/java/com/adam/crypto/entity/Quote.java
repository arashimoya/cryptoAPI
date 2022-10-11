package com.adam.crypto.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Quote {
    @JsonAlias("asset_id_base")
    private final String source;
    private final List<Rate> rates;

    public Quote(String source, List<Rate> rates) {
        this.source = source;
        this.rates = rates;
    }

    public String getSource() {
        return source;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public Quote filterRates(String[] filter){
        return new Quote(this.source, getRates().stream().filter(
                rate -> Arrays.stream(filter).anyMatch(s -> s.equals(rate.getAssetIdQuote()))).collect(Collectors.toList()));
    }
}
