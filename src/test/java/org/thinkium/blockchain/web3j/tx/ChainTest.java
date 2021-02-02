package org.thinkium.blockchain.web3j.tx;

import com.alibaba.fastjson.JSONArray;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author HarryPotter
 * @date 12:28 2020/7/7
 * @email harry@potter.com
 * @description
 */
public class ChainTest extends BaseTest {
    
    private static final Logger log = LoggerFactory.getLogger(ChainTest.class);
    
    @Test
    public void testGetChainStats() {
        Map result = web3.getChainStats("1");
        log.debug("result:{}", formatOut(result));
    }
    
    @Test
    public void testGetBlockHeader() {
        String height = "30";
        Map result = web3.getBlockHeader("1", height);
        log.debug("result:{}", formatOut(result));
    }
    
    @Test
    public void testGetBlockTxs() {
        String chainId = "1";
        String height = "84";
        String page = "1";
        String size = "10";
        
        Map result = web3.getBlockTxs(chainId, height, page, size);
        log.debug("result:{}", formatOut(result));
    }
    
    @Test
    public void testGetChainInfo() {
        JSONArray result = web3.getChainInfo();
        log.debug("result:{}", formatOut(result));
    }
    
}
