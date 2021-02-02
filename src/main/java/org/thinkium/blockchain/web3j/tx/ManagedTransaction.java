package org.thinkium.blockchain.web3j.tx;

import org.thinkium.blockchain.web3j.protocol.Web3j;
import org.thinkium.blockchain.web3j.protocol.exceptions.TransactionException;
import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;

/** Generic transaction manager. */
public abstract class ManagedTransaction {
    
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);
    
    protected Web3j web3j;
    
    protected TransactionManager transactionManager;
    
    protected ManagedTransaction(Web3j web3j, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.web3j = web3j;
    }
    
    /**
     * Return the current gas price from the ethereum node.
     *
     * <p>Note: this method was previously called {@code getGasPrice} but was renamed to distinguish
     * it when a bean accessor method on {@link Contract} was added with that name. If you have a
     * Contract subclass that is calling this method (unlikely since those classes are usually
     * generated and until very recently those generated subclasses were marked {@code final}), then
     * you will need to change your code to call this method instead, if you want the dynamic
     * behavior.
     *
     * @return the current gas price, determined dynamically at invocation
     *
     * @throws IOException if there's a problem communicating with the ethereum node
     */
    public BigInteger requestCurrentGasPrice() throws IOException {
        return GAS_PRICE;
    }
    
    protected TransactionReceipt send(String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit) throws IOException, TransactionException {
        return transactionManager.executeTransaction(gasPrice, gasLimit, to, data, value);
    }
    
    protected TransactionReceipt send(
            String to,
            String data,
            BigInteger value,
            BigInteger gasPrice,
            BigInteger gasLimit,
            boolean constructor)
            throws IOException, TransactionException {
        return transactionManager.executeTransaction(gasPrice, gasLimit, to, data, value, constructor);
    }
    
    protected String call(String to, String data) throws IOException {
        return transactionManager.sendCall(to, data);
    }
}
