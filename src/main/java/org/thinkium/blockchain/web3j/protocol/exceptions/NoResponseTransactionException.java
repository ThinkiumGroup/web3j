package org.thinkium.blockchain.web3j.protocol.exceptions;

import org.thinkium.blockchain.web3j.protocol.methods.Transaction;

public class NoResponseTransactionException extends RuntimeException {
    
    private String localHash;
    private Transaction rawTransaction;
    
    public NoResponseTransactionException(String message, Transaction rawTransaction) {
        super(message + ", localHash:" + rawTransaction.getHash());
        this.rawTransaction = rawTransaction;
        this.localHash = rawTransaction.getHash();
    }
    
    public String getLocalHash() {
        return localHash;
    }
    
    public Transaction getRawTransaction() {
        return rawTransaction;
    }
}