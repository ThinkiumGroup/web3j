package org.thinkium.blockchain.web3j.tx;

import org.thinkium.blockchain.web3j.crypto.Credentials;
import org.thinkium.blockchain.web3j.protocol.Web3j;
import org.thinkium.blockchain.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;

public class BatchTransactionManager extends RawTransactionManager {
    private BigInteger nonce = BigInteger.ONE.negate();
    
    public BatchTransactionManager(Web3j web3j, Credentials credentials, String chainId) {
        super(web3j, credentials, chainId);
    }
    
    public BatchTransactionManager(
            Web3j web3j,
            Credentials credentials,
            String chainId,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(web3j, credentials, chainId, transactionReceiptProcessor);
    }
    
    public BatchTransactionManager(Web3j web3j, Credentials credentials, String chainId, int attempts, long sleepDuration) {
        super(web3j, credentials, chainId, attempts, sleepDuration);
    }
    
    @Override
    protected BigInteger getNonce() throws IOException {
        if (nonce.compareTo(BigInteger.ZERO) < 0) {
            nonce = web3j.getNonce(getChainId(), credentials.getAddress());
        } else {
            nonce = nonce.add(BigInteger.ONE);
        }
        return nonce;
    }
}
