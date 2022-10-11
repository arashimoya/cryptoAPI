package com.adam.crypto;

import com.adam.crypto.entity.Exchange;
import com.adam.crypto.entity.ExchangeParams;
import com.adam.crypto.entity.Quote;
import com.adam.crypto.service.CoinAPIService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class CurrencyRestControllerTest {

    private final String baseUrl = "http://localhost:2137/currencies/";
    @Autowired
    MockMvc mockMvc;
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

        Quote quote = Mockito.mock(Quote.class);

        Mockito.when(coinAPIService.getQuote(currency_id, new String[0])).thenReturn(quote);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + currency_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", contains(currency_id)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void when2FiltersGiven_thenReturnQuotaWith2Currencies() throws Exception{
        String currency_id = "BTC";

        Quote quote = Mockito.mock(Quote.class);

        String[] filter = new String[]{
                "ETH",
                "USDT"
        };

        Mockito.when(coinAPIService.getQuote(currency_id, new String[0])).thenReturn(quote);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + currency_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", contains(filter[0])))
                .andExpect(jsonPath("$", contains(filter[1])))
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
        String[] filter = new String[]{
                "ETH",
                "BTC",
                "USDT"
        };
        ExchangeParams exchangeParams = new ExchangeParams("DOGE", filter,123);

        Mockito.when(coinAPIService.getExchange(exchangeParams)).thenThrow(ResponseStatusException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl+"exchange")
                        .content(mapper.writeValueAsString(exchangeParams)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", contains(filter[0])))
                .andExpect(jsonPath("$", contains(filter[1])))
                .andExpect(jsonPath("$", contains(filter[2])));
    }




}
