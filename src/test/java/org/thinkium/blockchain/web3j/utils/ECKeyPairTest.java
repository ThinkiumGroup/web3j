package org.thinkium.blockchain.web3j.utils;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkium.blockchain.web3j.utils.cipher.*;

/**
 * @author HarryPotter
 * @date 18:46 2020/7/2
 * @email harry@potter.com
 */
public class ECKeyPairTest {
    private static final Logger log = LoggerFactory.getLogger(LinuxSecureRandom.class);
    
    private static String generateMnemonics() {
        byte[] initialEntropy = new byte[16];
        SecureRandomUtils.secureRandom().nextBytes(initialEntropy);
        return MnemonicUtils.generateMnemonic(initialEntropy);
    }
    
    @Test
    public void genKey() {
        String mnemonic = generateMnemonics();
        
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");
        
        ECKeyPair ecKeyPair = ECKeyPair.create(Hash.sha256(seed));
//        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.hexStringToByteArray("0x5027156baafa0758473862ca54acd4690f0426ab267186b4324661b474576989"));
        
        String priKeyWithPrefix = Numeric.toHexStringWithPrefix(ecKeyPair.getPrivateKey());
        log.debug("priKeyWithPrefix: {}", priKeyWithPrefix);
        String pubKeyWithPrefix = Numeric.toHexStringWithPrefix(ecKeyPair.getPublicKey());
        log.debug("pubKeyWithPrefix: {}", pubKeyWithPrefix);
        
        String pubKey = Numeric.toHexString(Numeric.toBytesPadded(ecKeyPair.getPublicKey(), 64));
        log.debug("pubKey: {}", pubKey.replace("0x", "0x04"));
        System.out.println();
        
        String addressByEcKeyPair = Keys.getAddress(ecKeyPair);
        log.debug("addressByEcKeyPair: {}", addressByEcKeyPair);
        String addressByPubKey = Keys.getAddress(pubKeyWithPrefix);
        log.debug("addressByPubKey: {}", addressByPubKey);
    }
}
