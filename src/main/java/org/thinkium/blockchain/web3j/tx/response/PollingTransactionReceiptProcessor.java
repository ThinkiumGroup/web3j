package org.thinkium.blockchain.web3j.tx.response;

import org.thinkium.blockchain.web3j.protocol.Web3j;
import org.thinkium.blockchain.web3j.protocol.exceptions.TransactionException;
import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;

import java.io.IOException;
import java.util.Optional;

/** With each provided transaction hash, poll until we obtain a transaction receipt. */
public class PollingTransactionReceiptProcessor extends TransactionReceiptProcessor {
    
    protected final long sleepDuration;
    protected final int attempts;
    
    public PollingTransactionReceiptProcessor(Web3j web3j, long sleepDuration, int attempts) {
        super(web3j);
        this.sleepDuration = sleepDuration;
        this.attempts = attempts;
    }
    
    @Override
    public TransactionReceipt waitForTransactionReceipt(String chainId, String transactionHash) throws IOException, TransactionException {
        return getTransactionReceipt(chainId, transactionHash, sleepDuration, attempts);
    }
    
    private TransactionReceipt getTransactionReceipt(String chainId, String transactionHash, long sleepDuration, int attempts) throws IOException, TransactionException {
        Optional<? extends TransactionReceipt> receiptOptional = sendTransactionReceiptRequest(chainId, transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (!receiptOptional.isPresent()) {
                try {
                    Thread.sleep(sleepDuration);
                } catch (InterruptedException e) {
                    throw new TransactionException(e);
                }
                receiptOptional = sendTransactionReceiptRequest(chainId, transactionHash);
            } else {
                return receiptOptional.get();
            }
        }
        
        throw new TransactionException("Transaction receipt was not generated after "
                + ((sleepDuration * attempts) / 1000
                + " seconds for transaction: "
                + transactionHash),
                transactionHash);
    }
}
