package org.thinkium.blockchain.web3j.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Our common JSON-RPC response type.
 *
 * @param <T> the object type contained within the response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Response<T> {
    private T result;
    private String rawResponse;
    
    private Error error;
    
    public Response() {
    }
    
    public T getResult() {
        return result;
    }
    
    public void setResult(T result) {
        this.result = result;
    }
    
    public String getRawResponse() {
        return rawResponse;
    }
    
    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }
    
    public Error getError() {
        return error;
    }
    
    public void setError(Error error) {
        this.error = error;
    }
    
    public boolean hasError() {
        return error != null && error.code != 0;
    }
    
    public static class Error {
        private int code;
        private String message;
        
        public Integer getCode() {
            return code;
        }
        
        public void setCode(Integer code) {
            this.code = code;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
