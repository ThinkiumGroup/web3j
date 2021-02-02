package org.thinkium.blockchain.web3j.tx;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkium.blockchain.web3j.thk.KeyHolder;
import org.thinkium.blockchain.web3j.thk.contract.ContractConstants;
import org.thinkium.blockchain.web3j.thk.models.vo.CashCheque;
import org.thinkium.blockchain.web3j.thk.models.vo.Transaction;

import java.math.BigInteger;
import java.util.Map;

/**
 * @author HarryPotter
 * @date 15:50 2020/7/3
 * @email harry@potter.com
 */
public class CashChequeTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(CashChequeTest.class);
    
    private static int FROM_CHAIN_ID = 1;
    private static int TO_CHAIN_ID = 2;
    
    private static String PRIVATE_KEY_FROM = PRIVATE_KEY;
    private static final KeyHolder keyHolder;
    private static String FROM_ADDRESS = ADDRESS;
    private static String TO_ADDRESS = ADDRESS;
    private static String TEST_AMOUNT = "1" + "000000000000000000";
    
    private static int EXPIRE_AFTER = 200;
    
    static {
        keyHolder = new KeyHolder(PRIVATE_KEY_FROM);
    }
    
    //Cross chain transfer process, from a chain to B chain
    //1 - first, write a check from chain a, send a transaction to [system cross chain withdrawal contract (0x000000000000000)], and specify the overdue height of the check -- that is, when the height of chain B exceeds (excluding) this value, the check cannot be withdrawn, but can only be returned
    //2 - check the previous transaction result
    //3 - if the transaction is successful, obtain the check deposit certificate of chain B from chain a
    //4 - send a transaction from chain B to [system cross chain deposit contract (0x000000000003000000)] with the returned check certificate as input
    //5 - check the transaction result of the previous step. If the transaction is successful, the cross chain transfer is successful
    //Cancel check - the check cannot be cancelled until the specified block height is reached
    //6 - if the check reaches the specified block height and is not withdrawn, the cross chain deposit needs to be cancelled manually
    //7 - get the check cancellation certificate from the B chain and use it as input to send a transaction to [system cross chain cancellation deposit contract (0x000000000003000000)]
    //8 - check the transaction status of the previous step. If it fails, try step 6, 7, 8 again or contact inter core technology for assistance
    
    /**
     * Generate check
     */
    public CashCheque genCheque() {
        log.debug("genCheque()");
        final int height = getChainHeight(TO_CHAIN_ID + "");
        int expireHeight = height + EXPIRE_AFTER;
        log.debug("genCheque-expireHeight:{}", expireHeight);
        final int nonce = getNonce(FROM_CHAIN_ID + "", FROM_ADDRESS);
        log.debug("genCheque-nonce:{}", nonce);
        
        final CashCheque cashCheque = new CashCheque();
        cashCheque.setChainId(FROM_CHAIN_ID);
        cashCheque.setFromChainId(FROM_CHAIN_ID);
        cashCheque.setFromAddress(FROM_ADDRESS);
        cashCheque.setNonce(nonce);
        cashCheque.setToChainId(TO_CHAIN_ID);
        cashCheque.setToAddress(TO_ADDRESS);
        cashCheque.setExpireHeight(expireHeight);
        cashCheque.setAmount(new BigInteger(TEST_AMOUNT));
        log.debug("genCheque-vccProof:{}", cashCheque);
        
        final String vccInput = cashCheque.encode();
        log.debug("genCheque-vccInput:{}", vccInput);
        
        sendTx(FROM_CHAIN_ID, TO_CHAIN_ID, FROM_ADDRESS, ContractConstants.SystemContractAddress.WITHDRAW, vccInput);
        
        return cashCheque;
    }
    
    /**
     * Cash a check
     */
    @Test
    public void testCashCheque() {
        EXPIRE_AFTER = 200;
        log.debug("testCashCheque()");
        final CashCheque cashCheque = genCheque();
        Map proofOut = (Map) retryIfPending(() -> {
            Map proofIn = web3.RpcMakeVccProof(cashCheque, true);
            if (proofIn.get("errCode") != null) {
                return null;
            }
            return proofIn;
        });
        log.debug("testCashCheque-proof:{}", proofOut);
        
        assert !isExpire(cashCheque);
        
        final String input = ((String) proofOut.get("input"));
        log.debug("testCashCheque-proof-input:{}", input);
        sendTx(cashCheque.getToChainId(), cashCheque.getToChainId(), cashCheque.getFromAddress(), ContractConstants.SystemContractAddress.DEPOSIT, input);
    }
    
    /**
     * Cancel a check
     */
    @Test
    public void testCancelCheque() {
        EXPIRE_AFTER = 2;
        log.debug("testCancelCheque()");
        final CashCheque cashCheque = genCheque();
        
        log.debug("waiting until block height > expireHeight");
        retryIfPending(() -> isExpire(cashCheque) ? 1 : null);
        
        cashCheque.setChainId(cashCheque.getToChainId());
        Map proof = web3.RpcMakeVccProof(cashCheque, false);
        log.debug("testCancelCheque-proof:{}", proof);
        final String input = ((String) proof.get("input"));
        log.debug("testCancelCheque-input:{}", input);
        Assert.assertNotNull(input);
        
        sendTx(cashCheque.getFromChainId(), cashCheque.getFromChainId(), cashCheque.getFromAddress(), ContractConstants.SystemContractAddress.CANCEL, input);
    }
    
    public Map sendTx(int fromChainId, int toChainId, String fromAddress, String toAddress, String input) {
        log.debug("sendTx");
        String pub = keyHolder.getPublicKey();
        Transaction tx = new Transaction();
        tx.setChainId(fromChainId + "");
        tx.setFromChainId(fromChainId + "");
        tx.setToChainId(toChainId + "");
        tx.setFrom(fromAddress);
        tx.setTo(toAddress);
        tx.setNonce(getNonce(tx));
        tx.setValue("0");
        tx.setInput(input);
        tx.setPub(pub);
        tx.setUseLocal(false);
        tx.setExtra("");
        tx.setSig(keyHolder.sign(tx));
        Map result = web3.sendTx(tx);
        log.debug("sendTx-result:{}", result);
        final String hash = (String) result.get("TXhash");
        return getPendingTxByHash(tx.getChainId(), hash);
    }
    
    public boolean isExpire(CashCheque cashCheque) {
        final int currentChainHeight = getChainHeight(cashCheque.getToChainId() + "");
        final int expireHeight = cashCheque.getExpireHeight();
        log.debug("isExpire-currentChainHeight:{}, expireHeight:{}", currentChainHeight, expireHeight);
        return currentChainHeight > expireHeight;
    }
    
    public int getChainHeight(String chainId) {
        final Map chainInfoMap = web3.getChainStats(chainId);
        return (int) chainInfoMap.get("currentheight");
    }
    
    public static String getNonce(Transaction tx) {
        return getNonce(tx.getFromChainId(), tx.getFrom()) + "";
    }
    
    public static int getNonce(String chainId, String addr) {
        return web3.getNonce(chainId, addr);
    }
}
