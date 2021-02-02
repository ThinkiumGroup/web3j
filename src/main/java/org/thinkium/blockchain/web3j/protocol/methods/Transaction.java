package org.thinkium.blockchain.web3j.protocol.methods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.StringUtils;
import org.thinkium.blockchain.web3j.utils.Numeric;
import org.thinkium.blockchain.web3j.utils.cipher.Hash;

import java.io.IOException;
import java.math.BigInteger;

public class Transaction {
    @JsonIgnore
    private String hash;
    private String chainId;
    private String fromChainId;
    private String toChainId;
    private String from;
    private String to;
    @JsonSerialize(using = BigIntegerSerializer.class)
    private BigInteger nonce;
    @JsonSerialize(using = BigIntegerSerializer.class)
    private BigInteger value;
    private String input;
    private String sig;
    private String pub;
    private Boolean useLocal;
    private String extra;
    
    public Transaction() {
    }
    
    public Transaction(String chainId, String fromChainId, String toChainId, String from, String to, BigInteger nonce, BigInteger value, String input, String sig, String pub, Boolean useLocal, String extra) {
        this.chainId = chainId;
        this.fromChainId = fromChainId;
        this.toChainId = toChainId;
        this.from = from;
        this.to = to;
        this.nonce = nonce;
        this.value = value;
        this.input = input;
        this.sig = sig;
        this.pub = pub;
        this.useLocal = useLocal;
        this.extra = extra;
    }
    
    public Transaction(String chainId, String from, String to, String input) {
        this.chainId = chainId;
        this.fromChainId = chainId;
        this.toChainId = chainId;
        this.from = from;
        this.to = to;
        this.nonce = BigInteger.ZERO;
        this.value = BigInteger.ZERO;
        this.input = input;
        this.sig = "";
        this.pub = "";
        this.useLocal = false;
        this.extra = "";
    }
    
    public Transaction(String chainId, BigInteger nonce, BigInteger gasLimit, String to, BigInteger value, String input) {
        this.chainId = chainId;
        this.fromChainId = chainId;
        this.toChainId = chainId;
        this.from = "";
        this.to = to;
        this.nonce = nonce;
        this.value = value;
        this.input = input;
        this.sig = "";
        this.pub = "";
        this.useLocal = false;
        this.extra = Numeric.toHexStringNoPrefix(String.format("{\"gas\": %s}", gasLimit).getBytes());
    }
    
    public String getHash() {
        return hash;
    }
    
    public void setHash(String hash) {
        this.hash = hash;
    }
    
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
    
    public BigInteger getNonce() {
        return nonce;
    }
    
    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }
    
    public BigInteger getValue() {
        return value;
    }
    
    public void setValue(BigInteger value) {
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
        
        String u = this.getUseLocal() ? "1" : "0";
        String extra = "";
        if (StringUtils.isNotBlank(this.getExtra())) {
            extra = this.getExtra().replace("0x", "");
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
        
        final byte[] hashBytes = Hash.sha3(str.getBytes());
        setHash(Numeric.toHexString(hashBytes));
        return hashBytes;
    }
    
    public static class BigIntegerSerializer extends JsonSerializer<BigInteger> {
        @Override
        public void serialize(BigInteger bigInteger, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(bigInteger.toString());
        }
    }
    
    public static Transaction createTransaction(
            String chainId,
            BigInteger nonce,
            BigInteger gasLimit,
            String to,
            BigInteger value,
            String input) {
        return new Transaction(chainId, nonce, gasLimit, to, value, input);
    }
    
    public static Transaction createEthCallTransaction(String chainId, String from, String to, String data) {
        return new Transaction(chainId, from, to, data);
    }
}
