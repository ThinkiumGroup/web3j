package org.thinkium.blockchain.web3j.utils;

import org.thinkium.blockchain.web3j.utils.cipher.Hash;

/**
 * @author HarryPotter
 * @date 10:46 2021/1/21
 * @email harry@potter.com
 */
public class Basic {
    
    private static final String Pad = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    
    public static String padLeft(String string, int chars) {
        return chars <= string.length() ? string : Pad.substring(0, chars - string.length()) + string;
    }
    
    public static boolean isStrictAddress(String address) {
        return address.matches("^0x[0-9a-f]{40}$");
    }
    
    public static boolean isChecksumAddress(String address) {
        address = Numeric.cleanHexPrefix(address);
        String addressHash = Numeric.cleanHexPrefix(Hash.sha3String(address.toLowerCase()));
        for (int i = 0; i < 40; i++) {
            // the nth letter should be uppercase if the nth digit of casemap is 1
            int num = Integer.valueOf(addressHash.charAt(i) + "", 16);
            char c = address.charAt(i);
            if ((num > 7 && c != Character.toUpperCase(c)) || (num <= 7 && c != Character.toLowerCase(c))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isAddress(String address) {
        if (!address.toLowerCase().matches("^0x[0-9a-f]{40}$")) {
            return false;
        } else if (address.matches("^(0x)?[0-9a-f]{40}$") || address.matches("^(0x)?[0-9A-F]{40}$")) {
            return true;
        } else {
            return isChecksumAddress(address);
        }
    }
    
    public static String toChecksumAddress(String address) {
        address = Numeric.cleanHexPrefix(address).toLowerCase();
        if (!isStrictAddress(Numeric.prependHexPrefix(address))) {
            return "";
        }
        String addressHash = Numeric.cleanHexPrefix(Hash.sha3String(address));
        StringBuilder checksumAddress = new StringBuilder("0x");
        for (int i = 0; i < address.length(); i++) {
            // If ith character is 9 to f then make it uppercase
            if (Integer.valueOf(addressHash.charAt(i) + "", 16) > 7) {
                checksumAddress.append(Character.toUpperCase(address.charAt(i)));
            } else {
                checksumAddress.append(address.charAt(i));
            }
        }
        return checksumAddress.toString();
    }
    
    public static String toStrictAddress(String address) {
        if (isStrictAddress(address)) {
            return address;
        }
        
        if (address.matches("^[0-9a-f]{40}$")) {
            return "0x" + address;
        }
        
        return "0x" + padLeft(Numeric.cleanHexPrefix(address.toLowerCase()), 40);
    }
}
