package com.adam.crypto.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.http.HttpClient;

@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler{
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR ||
                response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if(response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR){
            throw new HttpClientErrorException(response.getStatusCode());
        } else if( response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }
}
