package org.thinkium.blockchain.web3j.contracts;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkium.blockchain.web3j.crypto.Credentials;
import org.thinkium.blockchain.web3j.protocol.exceptions.NoResponseTransactionException;
import org.thinkium.blockchain.web3j.protocol.exceptions.TransactionException;
import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;
import org.thinkium.blockchain.web3j.tx.BaseTest;
import org.thinkium.blockchain.web3j.tx.BatchTransactionManager;
import org.thinkium.blockchain.web3j.tx.RawTransactionManager;
import org.thinkium.blockchain.web3j.tx.gas.ContractGasProvider;
import org.thinkium.blockchain.web3j.tx.gas.DefaultGasProvider;
import org.thinkium.blockchain.web3j.tx.response.FakeTransactionReceiptProcessor;
import org.thinkium.blockchain.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * @author HarryPotter
 * @date 12:24 2020/7/7
 * @email harry@potter.com
 */
public class Erc20Test extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(Erc20Test.class);
    
    private HumanStandardToken humanStandardToken;
    private static final String chainId = "1";
    private static final String contractAddress = "0x5ab9c6ea17fcb2707db890094fdd85e2cc47d6c7";
    private static String address = null;
    final ContractGasProvider defaultGasProvider = new DefaultGasProvider();
    BigInteger wei = BigInteger.valueOf(1000000000000000000L);
    
    @Before
    public void setUp() {
        humanStandardToken = HumanStandardToken.load(chainId, contractAddress, web3j, HarryPotter, defaultGasProvider);
        address = HarryPotter.getAddress();
    }
    
    @Test
    public void deploy() throws Exception {
        Credentials credentials = Credentials.create(Numeric.cleanHexPrefix("0x9fb5d402bcf0fdbb838b37f60fd5c7a189f70a334b843934c121e718f4278c7b"));
        ContractGasProvider gasProvider = new DefaultGasProvider();
        String chainId = "1";
        final RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials, chainId);
        
        BigInteger _initialAmount = BigInteger.valueOf(1000000).multiply(wei);
        String _tokenName = "Harry Potter";
        BigInteger _decimalUnits = BigInteger.valueOf(18);
        String _tokenSymbol = "HP";
        
        final HumanStandardToken token = HumanStandardToken.deploy(web3j, transactionManager, gasProvider, _initialAmount, _tokenName, _decimalUnits, _tokenSymbol).send();
        final String tokenAddress = token.getContractAddress();
        log.debug("token.address:{}", tokenAddress); //0x461805f3979b4b66bf2d1bc5749139c7f59fcae2
    }
    
    @Test
    public void readFunctions() throws Exception {
        final String name = humanStandardToken.name().send();
        log.debug("name:{}\n", name);
        final BigInteger totalSupply = humanStandardToken.totalSupply().send();
        log.debug("totalSupply:{}\n", totalSupply);
        final BigInteger decimals = humanStandardToken.decimals().send();
        log.debug("decimals:{}\n", decimals);
        BigInteger balanceOf = humanStandardToken.balanceOf(address).send();
        log.debug("balanceOf:{}\n", balanceOf);
        final String symbol = humanStandardToken.symbol().send();
        log.debug("symbol:{}", symbol);
        balanceOf = humanStandardToken.balanceOf(ADDRESS).send();
        log.debug("balanceOf:{}\n", balanceOf);
    }
    
    @Test
    public void writeFunctions() throws Exception {
        try {
            final TransactionReceipt tx = humanStandardToken.transfer(ADDRESS, BigInteger.ONE).send();
            log.debug("transfer:{}\n", formatOut(tx));
            final BigInteger balanceOf = humanStandardToken.balanceOf(ADDRESS).send();
            log.debug("balanceOf:{}\n", balanceOf);
        } catch (NoResponseTransactionException e) {
            e.printStackTrace();
        } catch (TransactionException e2) {
            System.out.println(e2.getTransactionHash());
            e2.printStackTrace();
        }
    }
    
    @Test
    public void batchWriteFunctions() throws Exception {
        // batch send
        final FakeTransactionReceiptProcessor fakeTransactionReceiptProcessor = new FakeTransactionReceiptProcessor();
        final BatchTransactionManager batchTransactionManager = new BatchTransactionManager(web3j, HarryPotter, chainId, fakeTransactionReceiptProcessor);
        humanStandardToken = HumanStandardToken.load(contractAddress, web3j, batchTransactionManager, defaultGasProvider);
        
        BigInteger balanceOf = humanStandardToken.balanceOf(ADDRESS).send();
        log.debug("balanceOf:{}\n", balanceOf);
        for (int i = 0; i < 10; i++) {
            final TransactionReceipt tx = humanStandardToken.transfer(ADDRESS, BigInteger.ONE).send();
            log.debug("transfer:{}\n", formatOut(tx));
        }
        balanceOf = humanStandardToken.balanceOf(ADDRESS).send();
        log.debug("balanceOf:{}\n", balanceOf);
    }
}
