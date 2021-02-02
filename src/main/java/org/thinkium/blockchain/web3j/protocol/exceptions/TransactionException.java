package org.thinkium.blockchain.web3j.protocol.exceptions;

import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;

/**
 * Transaction timeout exception indicates that we have breached some threshold waiting for a
 * transaction to execute.
 */
public class TransactionException extends Exception {
    
    private String transactionHash;
    private TransactionReceipt transactionReceipt;
    
    public TransactionException(String message, String transactionHash) {
        super(message + ", remoteHash:" + transactionHash);
        this.transactionHash = transactionHash;
    }
    
    public TransactionException(String message, TransactionReceipt transactionReceipt) {
        super(message + ", remoteHash:" + transactionReceipt.getTransactionHash());
        this.transactionHash = transactionReceipt.getTransactionHash();
        this.transactionReceipt = transactionReceipt;
    }
    
    public TransactionException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Obtain the transaction hash .
     *
     * @return optional transaction hash .
     */
    public String getTransactionHash() {
        return transactionHash;
    }
    
    /**
     * Obtain the transaction receipt.
     *
     * @return optional transaction receipt.
     */
    public TransactionReceipt getTransactionReceipt() {
        return transactionReceipt;
    }
}
