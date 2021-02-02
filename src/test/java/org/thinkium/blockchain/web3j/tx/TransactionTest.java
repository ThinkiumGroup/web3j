package org.thinkium.blockchain.web3j.tx;

import com.alibaba.fastjson.JSONArray;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkium.blockchain.web3j.thk.KeyHolder;
import org.thinkium.blockchain.web3j.thk.models.vo.Transaction;

import java.util.Map;


public class TransactionTest extends BaseTest {
    
    private static final Logger log = LoggerFactory.getLogger(TransactionTest.class);
    
    @Test
    public void testGetAccount() {
        String chainId = "1";
        Map result = web3.getAccount(chainId, ADDRESS);
        log.debug("result:{}", formatOut(result));
    }
    
    @Test
    public void testGetTransactionByHash() {
        String chainId = "1";
        String hash = "0xb298a034848ea3fccf421824c9bc42d1525994843fcda67fd01ca66f16128ebe";
        Map result = web3.getTransactionByHash(chainId, hash);
        log.debug("result:{}", formatOut(result));
    }
    
    @Test
    public void testGetTransactions() {
        String startHeight = "90000";
        String endHeight = "100000";
        JSONArray arr = web3.GetTransactions("1", ADDRESS, startHeight, endHeight);
        log.debug("result:{}", formatOut(arr));
    }
    
    @Test
    public void testSendTx() {
        KeyHolder keyHolder = new KeyHolder(PRIVATE_KEY);
        
        Transaction transaction = new Transaction();
        transaction.setChainId("1");
        transaction.setFrom(ADDRESS);
        transaction.setTo(ADDRESS2);
        transaction.setToChainId("1");
        transaction.setFromChainId("1");
        transaction.setValue("1" + "000000000000000000");
        transaction.setInput("");
        transaction.setPub(keyHolder.getPublicKey());
        transaction.setUseLocal(false);
        transaction.setExtra("");
        
        String nonceVal = web3.getNonce(transaction.getChainId(), transaction.getFrom()) + "";
        transaction.setNonce(nonceVal);
        transaction.setSig(keyHolder.sign(transaction));
        Map result = web3.sendTx(transaction);
        log.debug("result:{}", formatOut(result));
        final String hash = (String) result.get("TXhash");
        getPendingTxByHash(transaction.getChainId(), hash);
    }
    
}
