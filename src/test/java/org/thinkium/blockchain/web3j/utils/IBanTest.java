package org.thinkium.blockchain.web3j.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author HarryPotter
 * @date 14:31 2021/1/21
 * @email harry@potter.com
 */
public class IBanTest {
    
    @Test
    public void test() {
        String iBanAddress = "TH93112A8NT0RIRPIXR7IXJBB1U0ZBXEB1K";
        String checkSumAddress = "0x08D04fc4513e27854ac19f16B9b8cB8D564e2c68";
        String errorIBanAddress = "TH90112A8NT0RIRPIXR7IXJBB1U0ZBXEB1K";
        
        Assert.assertEquals(IBan.toIBan(checkSumAddress), iBanAddress);
        Assert.assertEquals(IBan.toAddress(iBanAddress), checkSumAddress);
        Assert.assertEquals(IBan.toAddress(iBanAddress.toLowerCase()), checkSumAddress);
        Assert.assertTrue(IBan.isValid(iBanAddress));
        Assert.assertFalse(IBan.isValid(errorIBanAddress));
    }
}
