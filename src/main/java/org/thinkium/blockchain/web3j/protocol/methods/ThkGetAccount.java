package org.thinkium.blockchain.web3j.protocol.methods;

import org.thinkium.blockchain.web3j.protocol.Response;

import java.math.BigInteger;

/**
 * @author HarryPotter
 * @date 22:51 2020/11/15
 * @email harry@potter.com
 */
public class ThkGetAccount extends Response<ThkGetAccount.Account> {
    public static class Params {
        String chainId;
        String address;
        
        public Params(String chainId, String address) {
            this.chainId = chainId;
            this.address = address;
        }
        
        public String getChainId() {
            return chainId;
        }
        
        public void setChainId(String chainId) {
            this.chainId = chainId;
        }
        
        public String getAddress() {
            return address;
        }
        
        public void setAddress(String address) {
            this.address = address;
        }
    }
    
    @Override
    public void setResult(Account result) {
        super.setResult(result);
    }
    
    public Account getAccount() {
        return getResult();
    }
    
    public static class Account {
        private String address;
        private BigInteger nonce;
        private BigInteger balance;
        private String localCurrency;
        private String storageRoot;
        private String codeHash;
        private String longStorageRoot;
        
        public String getAddress() {
            return address;
        }
        
        public void setAddress(String address) {
            this.address = address;
        }
        
        public BigInteger getNonce() {
            return nonce;
        }
        
        public void setNonce(BigInteger nonce) {
            this.nonce = nonce;
        }
        
        public BigInteger getBalance() {
            return balance;
        }
        
        public void setBalance(BigInteger balance) {
            this.balance = balance;
        }
        
        public String getLocalCurrency() {
            return localCurrency;
        }
        
        public void setLocalCurrency(String localCurrency) {
            this.localCurrency = localCurrency;
        }
        
        public String getStorageRoot() {
            return storageRoot;
        }
        
        public void setStorageRoot(String storageRoot) {
            this.storageRoot = storageRoot;
        }
        
        public String getCodeHash() {
            return codeHash;
        }
        
        public void setCodeHash(String codeHash) {
            this.codeHash = codeHash;
        }
        
        public String getLongStorageRoot() {
            return longStorageRoot;
        }
        
        public void setLongStorageRoot(String longStorageRoot) {
            this.longStorageRoot = longStorageRoot;
        }
    }
}