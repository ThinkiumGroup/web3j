package org.thinkium.blockchain.web3j.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author HarryPotter
 * @date 14:31 2021/1/21
 * @email harry@potter.com
 */
public class BasicTest {
    
    private static final String errorCheckSumAddress = "0x08d04fc4513e27854ac19f16B9b8cB8D564e2c68";
    private static final String rightCheckSumAddress = "0x08D04fc4513e27854ac19f16B9b8cB8D564e2c68";
    private static final String hex = "8D04fc4513e27854ac19f16B9b8cB8D564e2c68";
    private static final String strictAddress = "0x08d04fc4513e27854ac19f16b9b8cb8d564e2c68";
    
    @Test
    public void test() {
        Assert.assertFalse(Basic.isAddress(errorCheckSumAddress));
        Assert.assertTrue(Basic.isAddress(rightCheckSumAddress));
        Assert.assertTrue(Basic.isStrictAddress(rightCheckSumAddress.toLowerCase()));
        Assert.assertTrue(Basic.isStrictAddress(errorCheckSumAddress.toLowerCase()));
        Assert.assertTrue(Basic.isAddress(errorCheckSumAddress.toLowerCase()));
        
        Assert.assertFalse(Basic.isChecksumAddress(errorCheckSumAddress));
        Assert.assertTrue(Basic.isChecksumAddress(rightCheckSumAddress));
        
        Assert.assertEquals(Basic.toStrictAddress(hex), strictAddress);
        Assert.assertEquals(Basic.toStrictAddress(rightCheckSumAddress), strictAddress);
    }
}