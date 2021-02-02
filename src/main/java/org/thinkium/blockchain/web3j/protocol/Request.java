package org.thinkium.blockchain.web3j.protocol;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;


public class Request<S, T extends Response> {
    private String method;
    private S params;
    
    private Web3jService web3jService;
    
    private Class<T> responseType;
    
    public Request() {
    }
    
    public Request(String method, S params, Web3jService web3jService, Class<T> type) {
        this.method = method;
        this.params = params;
        this.web3jService = web3jService;
        this.responseType = type;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public S getParams() {
        return params;
    }
    
    public void setParams(S params) {
        this.params = params;
    }
    
    @JsonIgnore
    public Class<T> getResponseType() {
        return responseType;
    }
    
    public T send() throws IOException {
        return web3jService.send(this, responseType);
    }
    
    public CompletableFuture<T> sendAsync() {
        return web3jService.sendAsync(this, responseType);
    }
    
}
