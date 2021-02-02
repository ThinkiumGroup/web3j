package org.thinkium.blockchain.web3j.tx;

import org.thinkium.blockchain.web3j.protocol.Web3j;
import org.thinkium.blockchain.web3j.protocol.exceptions.TransactionException;
import org.thinkium.blockchain.web3j.protocol.methods.ThkCall;
import org.thinkium.blockchain.web3j.protocol.methods.ThkSendTransaction;
import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;
import org.thinkium.blockchain.web3j.tx.exceptions.ContractCallException;
import org.thinkium.blockchain.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.thinkium.blockchain.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Transaction manager abstraction for executing transactions with Ethereum client via various
 * mechanisms.
 */
public abstract class TransactionManager {
    public static final int DEFAULT_BLOCK_TIME = 2 * 1000;
    
    public static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 30;
    public static final long DEFAULT_POLLING_FREQUENCY = DEFAULT_BLOCK_TIME;
    public static final String REVERT_ERR_STR = "Contract Call has been reverted by the EVM with the reason: '%s'.";
    
    private final TransactionReceiptProcessor transactionReceiptProcessor;
    private final String chainId;
    private final String fromAddress;
    
    protected TransactionManager(TransactionReceiptProcessor transactionReceiptProcessor, String chainId, String fromAddress) {
        this.transactionReceiptProcessor = transactionReceiptProcessor;
        this.chainId = chainId;
        this.fromAddress = fromAddress;
    }
    
    protected TransactionManager(Web3j web3j, String chainId, String fromAddress) {
        this(new PollingTransactionReceiptProcessor(web3j, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH), chainId, fromAddress);
    }
    
    protected TransactionManager(Web3j web3j, int attempts, long sleepDuration, String chainId, String fromAddress) {
        this(new PollingTransactionReceiptProcessor(web3j, sleepDuration, attempts), chainId, fromAddress);
    }
    
    protected TransactionReceipt executeTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) throws IOException, TransactionException {
        return executeTransaction(gasPrice, gasLimit, to, data, value, false);
    }
    
    protected TransactionReceipt executeTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            boolean constructor)
            throws IOException, TransactionException {
        ThkSendTransaction thkSendTransaction = sendTransaction(gasPrice, gasLimit, to, data, value, constructor);
        return processResponse(thkSendTransaction);
    }
    
    public ThkSendTransaction sendTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) throws IOException {
        return sendTransaction(gasPrice, gasLimit, to, data, value, false);
    }
    
    public abstract ThkSendTransaction sendTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            boolean constructor)
            throws IOException;
    
    public abstract String sendCall(String to, String data) throws IOException;
    
    public String getChainId() {
        return chainId;
    }
    
    public String getFromAddress() {
        return fromAddress;
    }
    
    private TransactionReceipt processResponse(ThkSendTransaction transactionResponse) throws IOException, TransactionException {
        if (transactionResponse.hasError()) {
            throw new RuntimeException("Error processing transaction request: " + transactionResponse.getError().getMessage());
        }
        String transactionHash = transactionResponse.getHash();
        
        try {
            return transactionReceiptProcessor.waitForTransactionReceipt(getChainId(), transactionHash);
        } catch (Exception e) {
            if (e instanceof TransactionException) {
                throw e;
            } else {
                throw new TransactionException(e.getMessage(), transactionHash);
            }
        }
    }
    
    public static void assertCallNotReverted(ThkCall thkCall) {
        if (thkCall.isReverted()) {
            throw new ContractCallException(String.format(REVERT_ERR_STR, thkCall.getRevertReason()));
        }
    }
}
