package org.thinkium.blockchain.web3j.utils;

import java.math.BigInteger;

/**
 * @author HarryPotter
 * @date 12:24 2020/7/7
 * @email harry@potter.com
 */
public class IBan {
    /**
     * Prepare an IBAN for mod 97 computation by moving the first 4 chars to the end and transforming the letters to
     * numbers (A = 10, B = 11, ..., Z = 35), as specified in ISO13616.
     */
    public static String iso13616Prepare(String iBan) {
        iBan = iBan.toUpperCase();
        iBan = iBan.substring(4) + iBan.substring(0, 4);
        char[] chars = iBan.toCharArray();
        StringBuilder s = new StringBuilder();
        for (char aChar : chars) {
            if (aChar >= 'A' && aChar <= 'Z') {
                // A = 10, B = 11, ... Z = 35
                s.append((aChar - 'A' + 10));
            } else {
                s.append(aChar);
            }
        }
        
        return s.toString();
    }
    
    /**
     * Calculates the MOD 97 10 of the passed IBAN as specified in ISO7064.
     */
    public static int mod9710(String iBan) {
        String remainder = iBan, block;
        
        while (remainder.length() > 2) {
            block = remainder.substring(0, Math.min(9, remainder.length()));
            remainder = Integer.parseInt(block) % 97 + remainder.substring(block.length());
        }
        return Integer.parseInt(remainder) % 97;
    }
    
    public static String fromBBan(String bBan) {
        String countryCode = "TH";
        int remainder = mod9710(iso13616Prepare(countryCode + "00" + bBan));
        String checkDigit = 98 - remainder + "";
        return countryCode + Basic.padLeft(checkDigit, 2) + bBan;
    }
    
    public static String fromAddress(String address) {
        BigInteger num = Numeric.toBigInt(address.toLowerCase());
        String base36 = num.toString(36);
        String padded = Basic.padLeft(base36, 30);
        return fromBBan(padded.toUpperCase());
    }
    
    public static String toIBan(String address) {
        return fromAddress(address);
    }
    
    public static String toAddress(String iBan) {
        if (isDirect(iBan)) {
            String base36 = iBan.substring(4);
            BigInteger num = new BigInteger(base36, 36);
            return Basic.toChecksumAddress(Basic.padLeft(Numeric.toHexStringNoPrefix(num), 40));
        }
        return "";
    }
    
    public static boolean isDirect(String iBan) {
        return iBan.length() == 34 || iBan.length() == 35;
    }
    
    public static boolean isValid(String iBan) {
        return iBan.matches("^TH[0-9]{2}[0-9A-Z]{30,31}$") && mod9710(iso13616Prepare(iBan)) == 1;
    }
}
