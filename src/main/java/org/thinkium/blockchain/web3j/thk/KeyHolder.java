package org.thinkium.blockchain.web3j.thk;


import org.thinkium.blockchain.web3j.thk.models.vo.Transaction;
import org.thinkium.blockchain.web3j.utils.Numeric;
import org.thinkium.blockchain.web3j.utils.cipher.ECDSASignature;
import org.thinkium.blockchain.web3j.utils.cipher.ECKeyPair;
import org.thinkium.blockchain.web3j.utils.cipher.Hash;
import org.thinkium.blockchain.web3j.utils.cipher.Sign;

import java.math.BigInteger;

/**
 * @author HarryPotter
 * @date 20:26 2020/7/10
 * @email harry@potter.com
 */
public class KeyHolder {
    private String privateKey;
    private ECKeyPair keyPair;
    
    public KeyHolder(String privateKey) {
        this.privateKey = privateKey;
        keyPair = ECKeyPair.create(Numeric.hexStringToByteArray(privateKey));
    }
    
    public String getPrivateKey() {
        return privateKey;
    }
    
    public ECKeyPair getKeyPair() {
        return keyPair;
    }
    
    public String getPublicKey() {
        String publicKeyHex = Numeric.toHexString(Numeric.toBytesPadded(keyPair.getPublicKey(), 64));
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
        ECDSASignature sig = keyPair.sign(messageHash);
        // Now we have to work backwards to figure out the recId needed to recover the signature.
        int recId = -1;
        for (int i = 0; i < 4; i++) {
            BigInteger k = Sign.recoverFromSignature(i, sig, messageHash);
            if (k != null && k.equals(keyPair.getPublicKey())) {
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
        Sign.SignatureData signatureData = new Sign.SignatureData(v, r, s);
        String rHex = Numeric.toHexString(r);
        String sHex = Numeric.toHexString(s);
        String sigData = rHex + sHex.replace("0x", "");
        if (v == 0) {
            sigData = sigData + "1b";
        } else {
            sigData = sigData + "1c";
        }
        return sigData;
    }
}