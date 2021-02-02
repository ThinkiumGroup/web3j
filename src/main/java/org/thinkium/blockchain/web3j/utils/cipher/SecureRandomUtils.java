package org.thinkium.blockchain.web3j.utils.cipher;

import java.security.SecureRandom;

/**
 * Created by thk on 6/18/19.
 */
public final class SecureRandomUtils {
    
    private static final SecureRandom SECURE_RANDOM;
    
    static {
        if (isAndroidRuntime()) {
            new LinuxSecureRandom();
        }
        SECURE_RANDOM = new SecureRandom();
    }
    
    public static SecureRandom secureRandom() {
        return SECURE_RANDOM;
    }
    
    // Taken from BitcoinJ implementation
    // https://github.com/bitcoinj/bitcoinj/blob/3cb1f6c6c589f84fe6e1fb56bf26d94cccc85429/core/src/main/java/org/bitcoinj/core/Utils.java#L573
    private static int isAndroid = -1;
    
    public static boolean isAndroidRuntime() {
        if (isAndroid == -1) {
            final String runtime = System.getProperty("java.runtime.name");
            isAndroid = (runtime != null && runtime.equals("Android Runtime")) ? 1 : 0;
        }
        return isAndroid == 1;
    }
    
    private SecureRandomUtils() {
    }
}
