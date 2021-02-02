package org.thinkium.blockchain.web3j.tx;

import com.alibaba.fastjson.JSONArray;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author HarryPotter
 * @date 12:27 2020/7/7
 * @email harry@potter.com
 * @description
 */
public class PosTest extends BaseTest {
    
    private static final Logger log = LoggerFactory.getLogger(PosTest.class);
    
    @Test
    public void testGetCommittee() {
        String chainId = "1";
        String epoch = "1";
        JSONArray result = web3.getCommittee(chainId, epoch);
        log.debug("result:{}", formatOut(result));
    }
    
    @Test
    public void Ping() {
        String ipAddress = "192.168.1.7:22007";
        Map result = web3.Ping(ipAddress);
        log.debug("result:{}", formatOut(result));
    }
}
