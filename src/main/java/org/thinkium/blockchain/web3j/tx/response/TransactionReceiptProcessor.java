package org.thinkium.blockchain.web3j.tx.response;

import org.thinkium.blockchain.web3j.protocol.ErrorEnum;
import org.thinkium.blockchain.web3j.protocol.Web3j;
import org.thinkium.blockchain.web3j.protocol.exceptions.TransactionException;
import org.thinkium.blockchain.web3j.protocol.methods.ThkGetTransactionByHash;
import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;

import java.io.IOException;
import java.util.Optional;

/** Abstraction for managing how we wait for transaction receipts to be generated on the network. */
public abstract class TransactionReceiptProcessor {
    
    private final Web3j web3j;
    
    public TransactionReceiptProcessor(Web3j web3j) {
        this.web3j = web3j;
    }
    
    public abstract TransactionReceipt waitForTransactionReceipt(String chainId, String transactionHash) throws IOException, TransactionException;
    
    Optional<? extends TransactionReceipt> sendTransactionReceiptRequest(String chainId, String transactionHash) throws IOException, TransactionException {
        ThkGetTransactionByHash transactionReceipt = web3j.getTransactionByHash(chainId, transactionHash).send();
        if (transactionReceipt.hasError() && transactionReceipt.getError().getCode() != ErrorEnum.TransactionNotFound.getCode()) {
            throw new TransactionException("Error processing request: " + transactionReceipt.getError().getMessage(), transactionHash);
        }
        
        return Optional.ofNullable(transactionReceipt.getTransaction());
    }
}
