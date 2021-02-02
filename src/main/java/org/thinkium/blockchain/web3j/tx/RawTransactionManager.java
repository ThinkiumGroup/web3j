package org.thinkium.blockchain.web3j.tx;

import org.thinkium.blockchain.web3j.crypto.Credentials;
import org.thinkium.blockchain.web3j.protocol.Web3j;
import org.thinkium.blockchain.web3j.protocol.exceptions.NoResponseTransactionException;
import org.thinkium.blockchain.web3j.protocol.methods.ThkCall;
import org.thinkium.blockchain.web3j.protocol.methods.ThkSendTransaction;
import org.thinkium.blockchain.web3j.protocol.methods.Transaction;
import org.thinkium.blockchain.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;

public class RawTransactionManager extends TransactionManager {
    
    final Web3j web3j;
    final Credentials credentials;
    
    public RawTransactionManager(Web3j web3j, Credentials credentials, String chainId) {
        super(web3j, chainId, credentials.getAddress());
        
        this.web3j = web3j;
        this.credentials = credentials;
    }
    
    public RawTransactionManager(
            Web3j web3j,
            Credentials credentials,
            String chainId,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, chainId, credentials.getAddress());
        
        this.web3j = web3j;
        this.credentials = credentials;
    }
    
    public RawTransactionManager(Web3j web3j, Credentials credentials, String chainId, int attempts, long sleepDuration) {
        super(web3j, attempts, sleepDuration, chainId, credentials.getAddress());
        
        this.web3j = web3j;
        this.credentials = credentials;
    }
    
    protected BigInteger getNonce() throws IOException {
        return web3j.getNonce(getChainId(), credentials.getAddress());
    }
    
    @Override
    public ThkSendTransaction sendTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            boolean constructor)
            throws IOException {
        BigInteger nonce = getNonce();
        Transaction rawTransaction = Transaction.createTransaction(getChainId(), nonce, gasLimit, to, value, data);
        return signAndSend(rawTransaction);
    }
    
    @Override
    public String sendCall(String to, String data) throws IOException {
        ThkCall thkCall = web3j.callTransaction(Transaction.createEthCallTransaction(getChainId(), getFromAddress(), to, data)).send();
        assertCallNotReverted(thkCall);
        return thkCall.getValue();
    }
    
    /*
     * @param rawTransaction a RawTransaction istance to be signed
     * @return The transaction signed and encoded without ever broadcasting it
     */
    public String sign(Transaction rawTransaction) {
        rawTransaction.setFrom(credentials.getAddress());
        rawTransaction.setPub(credentials.getPublicKey());
        String sign = credentials.sign(rawTransaction);
        rawTransaction.setSig(sign);
        return sign;
    }
    
    public ThkSendTransaction signAndSend(Transaction rawTransaction) throws IOException {
        sign(rawTransaction);
        try {
            return web3j.sendTx(rawTransaction).send();
        } catch (Exception e) {
            throw new NoResponseTransactionException(e.getMessage(), rawTransaction);
        }
    }
}
