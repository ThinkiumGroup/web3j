package org.thinkium.blockchain.web3j.protocol.core;

import org.thinkium.blockchain.web3j.protocol.Request;
import org.thinkium.blockchain.web3j.protocol.Web3j;
import org.thinkium.blockchain.web3j.protocol.Web3jService;
import org.thinkium.blockchain.web3j.protocol.methods.*;
import org.thinkium.blockchain.web3j.thk.models.vo.CashCheque;
import org.thinkium.blockchain.web3j.utils.Async;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author HarryPotter
 * @date 15:04 2020/11/16
 * @email harry@potter.com
 */
public class JsonRpcWeb3 implements Web3j {
    
    protected final Web3jService web3jService;
    private final ScheduledExecutorService scheduledExecutorService;
    
    public JsonRpcWeb3(Web3jService web3jService) {
        this(web3jService, Async.defaultExecutorService());
    }
    
    public JsonRpcWeb3(
            Web3jService web3jService,
            ScheduledExecutorService scheduledExecutorService) {
        this.web3jService = web3jService;
        this.scheduledExecutorService = scheduledExecutorService;
    }
    
    @Override
    public Request<?, ThkGetAccount> getAccount(String chainId, String address) {
        return new Request<>(
                "GetAccount",
                new ThkGetAccount.Params(chainId, address),
                web3jService,
                ThkGetAccount.class);
    }
    
    @Override
    public BigInteger getNonce(String chainId, String address) throws IOException {
        return getAccount(chainId, address).send().getAccount().getNonce();
    }
    
    @Override
    public Request<?, ThkGetTransactionByHash> getTransactionByHash(String chainId, String hash) {
        return new Request<>(
                "GetTransactionByHash",
                new ThkGetTransactionByHash.Params(chainId, hash),
                web3jService,
                ThkGetTransactionByHash.class);
    }
    
    @Override
    public Request<?, ThkGetAccount> getChainStats(String chainId) {
        return null;
    }
    
    @Override
    public Request<?, ThkGetAccount> getTransactions(String chainId, String address, String startHeight, String endHeight) {
        return null;
    }
    
    @Override
    public Request<?, ThkGetAccount> getBlockHeader(String chainId, String height) {
        return null;
    }
    
    @Override
    public Request<?, ThkGetAccount> getBlockTxs(String chainId, String height, String page, String size) {
        return null;
    }
    
    @Override
    public Request<?, ThkSendTransaction> sendTx(Transaction tx) {
        return new Request<>(
                "SendTx",
                tx,
                web3jService,
                ThkSendTransaction.class);
    }
    
    @Override
    public Request<?, ThkGetAccount> getChainInfo() {
        return null;
    }
    
    @Override
    public Request<?, ThkGetAccount> getCommittee(String chainId, String epoch) {
        return null;
    }
    
    @Override
    public Request<?, ThkCall> callTransaction(Transaction tx) {
        return new Request<>(
                "CallTransaction",
                tx,
                web3jService,
                ThkCall.class);
    }
    
    @Override
    public Request<?, ThkGetAccount> makeVccProof(CashCheque cashCheque, boolean cashOrCancel) {
        return null;
    }
    
    @Override
    public Request<?, ThkGetAccount> ping(String address) {
        return null;
    }
    
    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
        try {
            web3jService.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close web3j service", e);
        }
    }
}
