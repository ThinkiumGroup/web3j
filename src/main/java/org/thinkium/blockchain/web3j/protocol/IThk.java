package org.thinkium.blockchain.web3j.protocol;

import org.thinkium.blockchain.web3j.protocol.methods.*;
import org.thinkium.blockchain.web3j.thk.models.vo.CashCheque;

import java.io.IOException;
import java.math.BigInteger;

public interface IThk {
    /* Get account information*/
    Request<?, ThkGetAccount> getAccount(String chainId, String address);
    
    /* Get nonce*/
    BigInteger getNonce(String chainId, String address) throws IOException;
    
    /* Get transaction details*/
    Request<?, ThkGetTransactionByHash> getTransactionByHash(String chainId, String hash);
    
    /* Get chain details*/
    Request<?, ThkGetAccount> getChainStats(String chainId);
    
    /* Obtain the transaction information within the specified height according to the address*/
    Request<?, ThkGetAccount> getTransactions(String chainId, String address, String startHeight, String endHeight);
    
    /* Get block details*/
    Request<?, ThkGetAccount> getBlockHeader(String chainId, String height);
    
    /* Obtain the transaction within the corresponding height */
    Request<?, ThkGetAccount> getBlockTxs(String chainId, String height, String page, String size);
    
    //Send transaction
    Request<?, ThkSendTransaction> sendTx(Transaction tx);
    
    //Get chain structure
    Request<?, ThkGetAccount> getChainInfo();
    
    //Get Committee details
    Request<?, ThkGetAccount> getCommittee(String chainId, String epoch);
    
    //Get the data in the contract
    Request<?, ThkCall> callTransaction(Transaction tx);
    
    /**
     * Obtain proof of cashing or revoking a check
     *
     * @param cashCheque   Obtain the necessary parameters for proof. Note that tochain is passed to the ID of the initiating chain when revoking, which is the opposite of generating a check
     * @param cashOrCancel True cash, false undo
     */
    Request<?, ThkGetAccount> makeVccProof(CashCheque cashCheque, boolean cashOrCancel);
    
    //Get node operation information
    Request<?, ThkGetAccount> ping(String address);
}