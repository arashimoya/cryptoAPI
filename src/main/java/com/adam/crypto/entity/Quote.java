package com.adam.crypto.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
    @JsonAlias("asset_id_base")
    private String source;
    private List<Rate> rates;

    public String getSource() {
        return source;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void filterRates(String[] filter){
        rates = getRates().stream().filter(
                rate -> Arrays.stream(filter).anyMatch(s -> s.equals(rate.getAssetIdQuote()))).collect(Collectors.toList());
    }
}
