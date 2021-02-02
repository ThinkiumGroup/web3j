package org.thinkium.blockchain.web3j.tx.response;

import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;

/** Transaction receipt processor callback. */
public interface Callback {
    void accept(TransactionReceipt transactionReceipt);
    
    void exception(Exception exception);
}
