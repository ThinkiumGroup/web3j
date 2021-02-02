package org.thinkium.blockchain.web3j.protocol.core;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkium.blockchain.web3j.protocol.HttpService;
import org.thinkium.blockchain.web3j.protocol.Web3j;
import org.thinkium.blockchain.web3j.protocol.methods.ThkGetAccount;
import org.thinkium.blockchain.web3j.protocol.methods.Transaction;
import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;
import org.thinkium.blockchain.web3j.tx.BaseTest;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author HarryPotter
 * @date 15:38 2020/11/16
 * @email harry@potter.com
 */
public class Web3JTest extends BaseTest {
    
    private static final Logger log = LoggerFactory.getLogger(Web3JTest.class);
    private Web3j web3j;
    
    @Before
    public void setUp() {
        this.web3j = Web3j.load(new HttpService("http://rpctest.thinkium.org/v2"));
    }
    
    @Test
    public void getAccount() {
        try {
            final ThkGetAccount.Account account = web3j.getAccount("1", ADDRESS).send().getAccount();
            log.debug("account:{}", formatOut(account));
            log.debug("account.getNonce:{}", account.getNonce().toString());
            log.debug("account.getBalance:{}", account.getBalance());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void getNonce() {
        try {
            final BigInteger nonce = web3j.getNonce("1", ADDRESS);
            log.debug("nonce:{}", nonce);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void getTransactionByHash() {
        try {
            final TransactionReceipt transaction = web3j.getTransactionByHash("103", "0x2db22c0845702e6096a269926e0a1099e34c98c44a4a363f0640c871f389d09c").send().getTransaction();
            log.debug("transaction:{}", formatOut(transaction));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void getChainStats() {
    }
    
    @Test
    public void getTransactions() {
    }
    
    @Test
    public void getBlockHeader() {
    }
    
    @Test
    public void getBlockTxs() {
    }
    
    @Test
    public void sendTx() {
        try {
            final Transaction transaction = new Transaction();
            transaction.setChainId("1");
            transaction.setFromChainId("1");
            transaction.setToChainId("1");
            transaction.setFrom("0xc8abcd36d9147cea0f19eb83f98276cae634c830");
            transaction.setTo("");
            transaction.setNonce(BigInteger.ZERO);
            transaction.setValue(BigInteger.ZERO);
            transaction.setInput("0x608060405234801561001057600080fd5b5061015c806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80633ccfd60b1461003b5780633d79d1c814610045575b600080fd5b610043610063565b005b61004d610111565b6040518082815260200191505060405180910390f35b7f49b4c3f4344f33413322c03885f90f29e906bd8eb0cb2c3d815ee1ad2b3c989c3073ffffffffffffffffffffffffffffffffffffffff16316040518082815260200191505060405180910390a13373ffffffffffffffffffffffffffffffffffffffff166108fc3073ffffffffffffffffffffffffffffffffffffffff16319081150290604051600060405180830381858888f1935050505015801561010e573d6000803e3d6000fd5b50565b60003073ffffffffffffffffffffffffffffffffffffffff163190509056fea165627a7a723058203d1d4fe1e8a7daecc05f1a38dfe2bb6539188181d7d9ec5a9f15550f3c1ea74d0029");
            transaction.setSig("0xf25dc5200d33e7596b45d9f7f1165447c36e36ce374e72fdbb09cae178039bc12b2b85e73bf8e7f2c60c350d025e6b6bb2475769c5279e470f9d7bce12300f261b");
            transaction.setPub("0x040754028c7f3224f75d189412c1b0bd84ca4c5104f5fe868c6862ec6966379e211ef79700a5e6eac7d34ba145984a7bcf537ac66a06d17abce61b342846fe883c");
            transaction.setUseLocal(false);
            transaction.setExtra("7b22676173223a363030303030307d");
            final String hash = web3j.sendTx(transaction).send().getHash();
            log.debug("hash:{}", hash);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void getChainInfo() {
    }
    
    @Test
    public void getCommittee() {
    }
    
    @Test
    public void callTransaction() {
    }
    
    @Test
    public void makeVccProof() {
    }
    
    @Test
    public void ping() {
    }
}
