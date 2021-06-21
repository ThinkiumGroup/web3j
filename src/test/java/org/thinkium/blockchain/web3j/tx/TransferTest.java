package org.thinkium.blockchain.web3j.tx;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkium.blockchain.web3j.protocol.exceptions.TransactionException;
import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;
import org.thinkium.blockchain.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author HarryPotter
 * @date 14:55 2020/12/2
 * @email harry@potter.com
 */
public class TransferTest extends BaseTest {
    
    private static final Logger log = LoggerFactory.getLogger(TransferTest.class);
    private Transfer transfer;
    
    @Before
    public void setUp() {
        transfer = new Transfer(web3j, new RawTransactionManager(web3j, HarryPotter, "1"));
    }
    
    @Test
    public void testTransfer() throws InterruptedException, TransactionException, IOException {
        final TransactionReceipt tx = transfer.send(ADDRESS, BigDecimal.ONE, Convert.Unit.TKM);
        log.debug("testTransfer() tx:{}", formatOut(tx));
    }
}
