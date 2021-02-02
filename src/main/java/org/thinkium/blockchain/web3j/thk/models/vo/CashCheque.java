package org.thinkium.blockchain.web3j.thk.models.vo;


import org.thinkium.blockchain.web3j.abi.TypeEncoder;
import org.thinkium.blockchain.web3j.abi.datatypes.Address;
import org.thinkium.blockchain.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;

public class CashCheque {
    private int chainId;   // meaningless when a check is generated. use fromChainId if cash a check otherwise use toChainId if cancel a check
    private int fromChainId;
    private String fromAddress;
    private int nonce;
    private int toChainId;
    private String toAddress;
    private int expireHeight;// The overdue height refers to that when the height of the target chain exceeds (excluding) this value, the check cannot be withdrawn and can only be returned
    private BigInteger amount;
    
    public int getChainId() {
        return chainId;
    }
    
    public void setChainId(int chainId) {
        this.chainId = chainId;
    }
    
    public int getFromChainId() {
        return fromChainId;
    }
    
    public void setFromChainId(int fromChainId) {
        this.fromChainId = fromChainId;
    }
    
    public String getFromAddress() {
        return fromAddress;
    }
    
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
    
    public int getNonce() {
        return nonce;
    }
    
    public void setNonce(int nonce) {
        this.nonce = nonce;
    }
    
    public int getToChainId() {
        return toChainId;
    }
    
    public void setToChainId(int toChainId) {
        this.toChainId = toChainId;
    }
    
    public String getToAddress() {
        return toAddress;
    }
    
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }
    
    public int getExpireHeight() {
        return expireHeight;
    }
    
    public void setExpireHeight(int expireHeight) {
        this.expireHeight = expireHeight;
    }
    
    public BigInteger getAmount() {
        return amount;
    }
    
    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }
    
    // 4bytes FromChain + 20bytes FromAddress + 8bytes Nonce + 4bytes ToChain + 20bytes ToAddress +
    // 8bytes ExpireHeight + 1bytes len(Amount.Bytes()) + Amount.Bytes()
    // all is BigEndian
    // 0x000000022c7536e3605d9c16a7a3d7b1898e529396a65c23000000000000003c000000032c7536e3605d9c16a7a3d7b1898e529396a65c2400000000000083ec200000000000000000000000000000000000000000000000000000000000000001
    public String encode() {
        final StringBuilder sb = new StringBuilder("0x");
        sb.append(getIntHex(fromChainId, 4));
        sb.append(getAddressHex(fromAddress));
        sb.append(getIntHex(nonce, 8));
        sb.append(getIntHex(toChainId, 4));
        sb.append(getAddressHex(toAddress));
        sb.append(getIntHex(expireHeight, 8));
        sb.append("20");
        sb.append(TypeEncoder.encode(new Uint256(amount)));
        return sb.toString();
    }
    
    public static String getAddressHex(String address) {
        return TypeEncoder.encode(new Address(address)).substring(64 - 20 * 2);
    }
    
    public static String getIntHex(long num, int length) {
        return TypeEncoder.encode(new Uint256(num)).substring(64 - length * 2);
    }
    
    @Override
    public String toString() {
        return "CashCheque{" +
                "fromChainId=" + fromChainId +
                ", fromAddress='" + fromAddress + '\'' +
                ", nonce=" + nonce +
                ", toChainId=" + toChainId +
                ", toAddress='" + toAddress + '\'' +
                ", expireHeight=" + expireHeight +
                ", amount=" + amount +
                '}';
    }
}
    