package org.thinkium.blockchain.web3j.thk.models.vo;

import org.thinkium.blockchain.web3j.utils.cipher.Hash;

/**
 * Created by thk on 6/17/19.
 */
public class Transaction {
    private String chainId;
    private String fromChainId;
    private String toChainId;
    private String sig;
    private String pub;
    private String from;
    private String to;
    private String nonce;
    private String value;
    private String input;
    
    private Boolean useLocal;
    private String extra;
    
    public Boolean getUseLocal() {
        return useLocal;
    }
    
    public void setUseLocal(Boolean useLocal) {
        this.useLocal = useLocal;
    }
    
    public String getExtra() {
        return extra;
    }
    
    public void setExtra(String extra) {
        this.extra = extra;
    }
    
    public String getChainId() {
        return chainId;
    }
    
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }
    
    public String getFromChainId() {
        return fromChainId;
    }
    
    public void setFromChainId(String fromChainId) {
        this.fromChainId = fromChainId;
    }
    
    public String getToChainId() {
        return toChainId;
    }
    
    public void setToChainId(String toChainId) {
        this.toChainId = toChainId;
    }
    
    public String getSig() {
        return sig;
    }
    
    public void setSig(String sig) {
        this.sig = sig;
    }
    
    public String getPub() {
        return pub;
    }
    
    public void setPub(String pub) {
        this.pub = pub;
    }
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getTo() {
        return to;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
    
    public String getNonce() {
        return nonce;
    }
    
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getInput() {
        return input;
    }
    
    public void setInput(String input) {
        this.input = input;
    }
    
    /** tx hash */
    public byte[] hash() {
        String to = this.getTo();
        String input = this.getInput();
        String from = this.getFrom();
        if (to.length() > 2) {
            to = to.substring(2);
        }
        if (input.length() > 2) {
            input = input.substring(2);
        }
        if (from.length() > 2) {
            from = from.substring(2);
        }
        
        String u = "";
        String extra = "";
        
        if (this.getUseLocal()) {
            u = "1";
        } else {
            u = "0";
        }
        
        if (!this.getExtra().isEmpty()) {
            extra = extra.replace("0x", "");
        }
        
        String split = "-";
        String str = this.getChainId() + split
                + from + split
                + to + split
                + this.getNonce() + split
                + u + split
                + this.getValue() + split
                + input + split
                + extra;
        
        return Hash.sha3(str.getBytes());
    }
}
