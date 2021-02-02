package org.thinkium.blockchain.web3j.protocol.methods;


import java.math.BigInteger;
import java.util.List;

public class TransactionReceipt {
    public interface Status {
        int Ok = 1;
        int Failed = 0;
    }
    
    private Transaction tx;
    private List<Log> logs;
    private int status;
    private String transactionHash;
    private BigInteger blockHeight;
    private String gasUsed;
    private String gasFee;
    private String contractAddress;
    private String root;
    private String out;
    
    public Transaction getTx() {
        return tx;
    }
    
    public void setTx(Transaction tx) {
        this.tx = tx;
    }
    
    public List<Log> getLogs() {
        return logs;
    }
    
    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public boolean isStatusOK() {
        return Status.Ok == getStatus();
    }
    
    public String getTransactionHash() {
        return transactionHash;
    }
    
    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }
    
    public BigInteger getBlockHeight() {
        return blockHeight;
    }
    
    public void setBlockHeight(BigInteger blockHeight) {
        this.blockHeight = blockHeight;
    }
    
    public String getGasUsed() {
        return gasUsed;
    }
    
    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }
    
    public String getGasFee() {
        return gasFee;
    }
    
    public void setGasFee(String gasFee) {
        this.gasFee = gasFee;
    }
    
    public String getContractAddress() {
        return contractAddress;
    }
    
    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }
    
    public String getRoot() {
        return root;
    }
    
    public void setRoot(String root) {
        this.root = root;
    }
    
    public String getOut() {
        return out;
    }
    
    public void setOut(String out) {
        this.out = out;
    }
}
