package org.thinkium.blockchain.web3j.tx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkium.blockchain.web3j.crypto.Credentials;
import org.thinkium.blockchain.web3j.protocol.HttpService;
import org.thinkium.blockchain.web3j.protocol.Web3j;
import org.thinkium.blockchain.web3j.thk.Thk;

import java.util.Map;

/**
 * @author HarryPotter
 * @date 12:25 2020/7/7
 * @email harry@potter.com
 * @description
 */
public class BaseTest {
    protected static final Credentials HarryPotter = Credentials.create("4ef61bb72fbe09c548adc47f1a0660b35d56113fb338cede18bcbb647d40eb67");
    protected static final Web3j web3j = Web3j.load(new HttpService("http://43.247.184.50:8020/v2"));
    
    protected static String PRIVATE_KEY = "0x8e5b44b6cee8fa05092b4b5a8843aa6b0ec37915a940c9b5938e88a7e6fdd83a";
    protected static String ADDRESS = "0xf167a1c5c5fab6bddca66118216817af3fa86827";
    
    protected static String PRIVATE_KEY2 = "0xc614545a9f1d9a2eeda26836e42a4c11631f25dc3d0dcc37fe62a89c4ff293d1";
    protected static String ADDRESS2 = "0x5dfcfc6f4b48f93213dad643a50228ff873c15b9";
    
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    static Thk web3;
    
    static {
        web3 = new Thk();
        web3.setHOST_URL("http://rpctest.thinkium.org");
    }
    
    public static String formatOut(Object origin) {
        return JSON.toJSONString(origin, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
    }
    
    public Object retryIfPending(Callback callback) {
        final long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 30 * 1000) {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final Object result = callback.execute();
            if (result != null) {
                return result;
            }
            log.debug("retry again");
        }
        throw new RuntimeException("timeout");
    }
    
    interface Callback {
        Object execute();
    }
    
    public Map getPendingTxByHash(String chainId, String hash) {
        return ((Map) retryIfPending(
                () -> {
                    final Map txInfo = web3.getTransactionByHash(chainId, hash);
                    if (txInfo.get("errCode") == null) {
                        if (((int) txInfo.get("status")) == 1) {
                            log.debug("getTxByHash-txInfo:{}", formatOut(txInfo));
                            return txInfo;
                        } else {
                            throw new RuntimeException("tx failed");
                        }
                    } else {
                        return null;
                    }
                }
        ));
    }
    
}
