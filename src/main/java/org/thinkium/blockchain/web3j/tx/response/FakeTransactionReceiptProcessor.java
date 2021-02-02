package org.thinkium.blockchain.web3j.tx.response;

import org.thinkium.blockchain.web3j.protocol.exceptions.TransactionException;
import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;

import java.io.IOException;

public class FakeTransactionReceiptProcessor extends TransactionReceiptProcessor {
    public FakeTransactionReceiptProcessor() {
        super(null);
    }
    
    @Override
    public TransactionReceipt waitForTransactionReceipt(String chainId, String transactionHash) throws IOException, TransactionException {
        final TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setStatus(TransactionReceipt.Status.Ok);
        transactionReceipt.setTransactionHash(transactionHash);
        return transactionReceipt;
    }
}