package org.thinkium.blockchain.web3j.crypto;

import org.thinkium.blockchain.web3j.protocol.methods.Transaction;
import org.thinkium.blockchain.web3j.utils.Numeric;
import org.thinkium.blockchain.web3j.utils.cipher.*;

import java.math.BigInteger;

/** Credentials wrapper. */
public class Credentials {
    
    private final ECKeyPair ecKeyPair;
    private final String address;
    
    private Credentials(ECKeyPair ecKeyPair, String address) {
        this.ecKeyPair = ecKeyPair;
        this.address = address;
    }
    
    public ECKeyPair getEcKeyPair() {
        return ecKeyPair;
    }
    
    public String getAddress() {
        return address;
    }
    
    public static Credentials create(ECKeyPair ecKeyPair) {
        String address = Numeric.prependHexPrefix(Keys.getAddress(ecKeyPair));
        return new Credentials(ecKeyPair, address);
    }
    
    public static Credentials create(String privateKey, String publicKey) {
        return create(new ECKeyPair(Numeric.toBigInt(privateKey), Numeric.toBigInt(publicKey)));
    }
    
    public static Credentials create(String privateKey) {
        return create(ECKeyPair.create(Numeric.toBigInt(privateKey)));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        Credentials that = (Credentials) o;
        
        if (ecKeyPair != null ? !ecKeyPair.equals(that.ecKeyPair) : that.ecKeyPair != null) {
            return false;
        }
        
        return address != null ? address.equals(that.address) : that.address == null;
    }
    
    @Override
    public int hashCode() {
        int result = ecKeyPair != null ? ecKeyPair.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
    
    public String getPublicKey() {
        String publicKeyHex = Numeric.toHexString(Numeric.toBytesPadded(ecKeyPair.getPublicKey(), 64));
        return publicKeyHex.replace("0x", "0x04");
    }
    
    public String sign(Transaction tx) {
        return signHash(tx.hash());
    }
    
    public String sign(String string) {
        byte[] messageHash = Hash.sha3(string.getBytes());
        return signHash(messageHash);
    }
    
    public String signHash(byte[] messageHash) {
        ECDSASignature sig = ecKeyPair.sign(messageHash);
        // Now we have to work backwards to figure out the recId needed to recover the signature.
        int recId = -1;
        for (int i = 0; i < 4; i++) {
            BigInteger k = Sign.recoverFromSignature(i, sig, messageHash);
            if (k != null && k.equals(ecKeyPair.getPublicKey())) {
                recId = i;
                break;
            }
        }
        if (recId == -1) {
            throw new RuntimeException("Could not construct a recoverable key. Are your credentials valid?");
        }
        int headerByte = recId + 27;
        byte v = (byte) headerByte;
        byte[] r = Numeric.toBytesPadded(sig.r, 32);
        byte[] s = Numeric.toBytesPadded(sig.s, 32);
        String rHex = Numeric.toHexString(r);
        String sHex = Numeric.toHexString(s);
        String sigData = rHex + sHex.replace("0x", "");
        sigData = sigData + "1c";
        return sigData;
    }
}
