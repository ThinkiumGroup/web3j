package org.thinkium.blockchain.web3j.protocol.methods;

import org.thinkium.blockchain.web3j.protocol.Response;

/**
 * @author HarryPotter
 * @date 22:51 2020/11/15
 * @email harry@potter.com
 */
public class ThkGetTransactionByHash extends Response<TransactionReceipt> {
    public static class Params {
        String chainId;
        String hash;
        
        public Params(String chainId, String hash) {
            this.chainId = chainId;
            this.hash = hash;
        }
        
        public String getChainId() {
            return chainId;
        }
        
        public void setChainId(String chainId) {
            this.chainId = chainId;
        }
        
        public String getHash() {
            return hash;
        }
        
        public void setHash(String hash) {
            this.hash = hash;
        }
    }
    
    @Override
    public void setResult(TransactionReceipt result) {
        super.setResult(result);
    }
    
    public TransactionReceipt getTransaction() {
        return getResult();
    }
}