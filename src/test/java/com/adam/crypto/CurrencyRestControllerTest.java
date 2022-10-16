package com.adam.crypto;

import com.adam.crypto.entity.*;
import com.adam.crypto.service.CoinAPIService;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasToString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class CurrencyRestControllerTest {

    private final String baseUrl = "http://localhost:2137/currencies/";
    @Autowired
    MockMvc mockMvc;
    @MockBean
    JsonSerializer<ExchangeParams> exchangeParamsJsonSerializer;
    @MockBean
    CoinAPIService coinAPIService;

    @Autowired
    ObjectMapper mapper;

    //getQuota
    @Test
    public void whenValidInput_thenStatusOk() throws Exception {

        String currency_id = "BTC";

        mockMvc.perform(MockMvcRequestBuilders
                .get(baseUrl + currency_id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void whenNullInput_thenStatusBadRequest() throws Exception {

        String currency_id = null;

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + currency_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void whenValueIsBTC_thenReturnsQuotaForBTC() throws Exception{
        String currency_id = "BTC";

        ArrayList<String> filter = new ArrayList<>();

        Quote quote = new Quote(currency_id, new ArrayList<>());

        Mockito.when(coinAPIService.getQuote(currency_id, filter)).thenReturn(quote);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + currency_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("filter", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source", hasToString(currency_id)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void when2FiltersGiven_thenReturnQuotaWith2Currencies() throws Exception{
        String currency_id = "BTC";

        ArrayList<Rate> rates = new ArrayList<Rate>();
        rates.add(new Rate("DOGE", BigDecimal.valueOf(326286.16521820497008772323498)));
        rates.add(new Rate("ETH", BigDecimal.valueOf(14.818612578725771406186817)));
        Quote quote = new Quote(currency_id, rates);

        ArrayList<String> filter = new ArrayList<>();
        filter.add("ETH");
        filter.add("DOGE");

        Mockito.when(coinAPIService.getQuote(currency_id, filter)).thenReturn(quote);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + currency_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("filter", filter.get(0))
                        .param("filter", filter.get(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rates.[1].assetIdQuote", hasToString(filter.get(0))))
                .andExpect(jsonPath("$.rates.[0].assetIdQuote", hasToString(filter.get(1))))
                .andDo(MockMvcResultHandlers.print());
    }

    //getExchange
    @Test
    public void whenNullValue_thenReturn400() throws Exception{
        ExchangeParams exchangeParams = new ExchangeParams(null,new String[0], 123);

        Mockito.when(coinAPIService.getExchange(exchangeParams)).thenThrow(ResponseStatusException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl+"exchange")
                        .content(mapper.writeValueAsString(exchangeParams)))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void whenValidValue_thenReturn200() throws Exception{
        String[] filter = new String[]{
                "ETH"
        };
        ExchangeParams exchangeParams = new ExchangeParams("DOGE", filter,123);

        Exchange exchange = Mockito.mock(Exchange.class);


        Mockito.when(coinAPIService.getExchange(exchangeParams)).thenReturn(exchange);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl+"exchange")
                        .content(mapper.writeValueAsString(exchangeParams)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenManyValidValues_thenReturn200() throws Exception{
        String[] filter = new String[]{
                "ETH",
                "BTC",
                "USDT"
        };
        ExchangeParams exchangeParams = new ExchangeParams("DOGE", filter,123);

        Exchange exchange = Mockito.mock(Exchange.class);


        Mockito.when(coinAPIService.getExchange(exchangeParams)).thenReturn(exchange);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl+"exchange")
                        .content(mapper.writeValueAsString(exchangeParams)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenManyValidValues_thenReturn200_andReturnExchangeContainingTheSameValues() throws Exception{
        String currencyFrom = "DOGE";
        String[] filter = new String[]{
                "ETH",
                "USDT"
        };
        ExchangeParams exchangeParams = new ExchangeParams(currencyFrom, filter,121);

        List<CurrencyTo> currencyToList = new ArrayList<>();


        Exchange exchange = Exchange.builder()
                .amount(121)
                .currencyFromName("")
                .currencyToList(currencyToList)
                .build();

        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(exchangeParams);

        Mockito.when(coinAPIService.getExchange(exchangeParams)).thenReturn(exchange);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(baseUrl+"exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", contains(filter[0])))
                .andExpect(jsonPath("$", contains(filter[1])));
    }




}
