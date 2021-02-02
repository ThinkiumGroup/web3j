package org.thinkium.blockchain.web3j.protocol;

/**
 * @author HarryPotter
 * @date 23:42 2020/11/18
 * @email harry@potter.com
 */
public enum ErrorEnum {
    TransactionNotFound(4003, "Transaction not found"),
    ;
    private int code;
    private String message;
    
    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
