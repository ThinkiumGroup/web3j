package org.thinkium.blockchain.web3j.tx;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkium.blockchain.web3j.abi.FunctionEncoder;
import org.thinkium.blockchain.web3j.abi.FunctionReturnDecoder;
import org.thinkium.blockchain.web3j.abi.TypeReference;
import org.thinkium.blockchain.web3j.abi.datatypes.*;
import org.thinkium.blockchain.web3j.abi.datatypes.generated.Int64;
import org.thinkium.blockchain.web3j.abi.datatypes.generated.Uint256;
import org.thinkium.blockchain.web3j.abi.datatypes.generated.Uint64;
import org.thinkium.blockchain.web3j.thk.KeyHolder;
import org.thinkium.blockchain.web3j.thk.contract.BusinessObj;
import org.thinkium.blockchain.web3j.thk.contract.BusinessObjListResult;
import org.thinkium.blockchain.web3j.thk.contract.Contract;
import org.thinkium.blockchain.web3j.thk.models.vo.Transaction;
import org.thinkium.blockchain.web3j.utils.FilesUtils;

import java.util.*;

/**
 * @author HarryPotter
 * @date 12:24 2020/7/7
 * @email harry@potter.com
 */
public class ContractTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(ContractTest.class);
    
    @Test
    public void testDeployContract() {
        final KeyHolder keyHolder = new KeyHolder(PRIVATE_KEY);
        String chainId = "1";
        
        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ deploy ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        
        Transaction tx = new Transaction();
        tx.setFrom(ADDRESS);
        tx.setTo("");
        tx.setChainId(chainId);
        tx.setFromChainId(chainId);
        tx.setToChainId(chainId);
        tx.setValue("0");
        tx.setInput("");
        tx.setPub(keyHolder.getPublicKey());
        tx.setUseLocal(false);
        tx.setExtra("");
        
        String nonce = web3.getNonce(tx.getChainId(), tx.getFrom()) + "";
        tx.setNonce(nonce);
        
        String binContent = FilesUtils.getResourcesFileContent("hello.bin");
        Map result = Contract.deploy(web3, keyHolder, tx, binContent, Collections.emptyList());
        log.debug("result:{}", formatOut(result));
        String hash = result.get("TXhash").toString();
        assert StringUtils.isNotBlank(hash);
        
        Map txInfo = getPendingTxByHash(tx.getChainId(), hash);
        
        
        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ send ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        final String contractAddress = txInfo.get("contractAddress").toString();
        assert StringUtils.isNotBlank(contractAddress);
        tx.setTo(contractAddress);
        
        nonce = web3.getNonce(tx.getChainId(), tx.getFrom()) + "";
        tx.setNonce(nonce);
        
        Function function = new Function("setNickname", Collections.singletonList(new Utf8String("world")), Collections.emptyList());
        Map resultSend = Contract.send(web3, keyHolder, tx, function);
        log.debug("resultSend:{}", formatOut(resultSend));
        
        hash = resultSend.get("TXhash").toString();
        assert StringUtils.isNotBlank(hash);
        
        txInfo = getPendingTxByHash(tx.getChainId(), hash);
        
        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ call ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        function = new Function("getNickname", Collections.emptyList(), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
        
        Map resultCall = Contract.call(web3, tx, function);
        log.debug("resultCall:{}", formatOut(resultCall));
        
        List<Type> types = FunctionReturnDecoder.decode(resultCall.get("out").toString(), function.getOutputParameters());
        log.debug("output: {}", types.get(0).getValue().toString());
    }
    
    @Test
    public void testCallContract() {
        final KeyHolder keyHolder = new KeyHolder(PRIVATE_KEY);
        String chainId = "1";
        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ send ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Transaction tx = new Transaction();
        tx.setFrom(ADDRESS);
        tx.setTo("0x819b9ac5e1b1187c288bc02e2665787f2f30732b");
        tx.setChainId(chainId);
        tx.setFromChainId(chainId);
        tx.setToChainId(chainId);
        tx.setValue("0");
        tx.setInput("");
        tx.setPub(keyHolder.getPublicKey());
        tx.setUseLocal(false);
        tx.setExtra("");
        
        String nonce = web3.getNonce(tx.getChainId(), tx.getFrom()) + "";
        tx.setNonce(nonce);
        
        Function function = new Function("setNickname", Collections.singletonList(new Utf8String("hello")), Collections.emptyList());
        Map resultSend = Contract.send(web3, keyHolder, tx, function);
        log.debug("resultSend:{}", formatOut(resultSend));
        
        String hash = resultSend.get("TXhash").toString();
        assert StringUtils.isNotBlank(hash);
        
        getPendingTxByHash(tx.getChainId(), hash);
        
        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ call ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        function = new Function("getNickname", Collections.emptyList(), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
        
        Map resultCall = Contract.call(web3, tx, function);
        log.debug("resultCall:{}", formatOut(resultCall));
        
        List<Type> types = FunctionReturnDecoder.decode(resultCall.get("out").toString(), function.getOutputParameters());
        log.debug("output: {}", types.get(0).getValue().toString());
    }
    
    @Test
    public void testDeployContract2() {
        final KeyHolder keyHolder = new KeyHolder(PRIVATE_KEY);
        String chainId = "1";
        
        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ deploy ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        
        Transaction tx = new Transaction();
        tx.setFrom(ADDRESS);
        tx.setTo("");
        tx.setChainId(chainId);
        tx.setFromChainId(chainId);
        tx.setToChainId(chainId);
        tx.setValue("0");
        tx.setInput("");
        tx.setPub(keyHolder.getPublicKey());
        tx.setUseLocal(false);
        tx.setExtra("");
        
        String nonce = web3.getNonce(tx.getChainId(), tx.getFrom()) + "";
        tx.setNonce(nonce);
        
        String binContent = FilesUtils.getResourcesFileContent("Greeter.bin");
        Map result = Contract.deploy(web3, keyHolder, tx, binContent, Collections.emptyList());
        log.debug("result:{}", formatOut(result));
        String hash = result.get("TXhash").toString();
        assert StringUtils.isNotBlank(hash);
        
        Map txInfo = getPendingTxByHash(tx.getChainId(), hash);
        
        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ send ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        final String contractAddress = txInfo.get("contractAddress").toString();
        assert StringUtils.isNotBlank(contractAddress);
        tx.setTo(contractAddress);
        
        nonce = web3.getNonce(tx.getChainId(), tx.getFrom()) + "";
        tx.setNonce(nonce);
        
        Function function = new Function("setGreeting", Collections.singletonList(new Utf8String("Greetings!")), Collections.emptyList());
        Map resultSend = Contract.send(web3, keyHolder, tx, function);
        log.debug("resultSend:{}", formatOut(resultSend));
        
        hash = resultSend.get("TXhash").toString();
        assert StringUtils.isNotBlank(hash);
        
        txInfo = getPendingTxByHash(tx.getChainId(), hash);
        
        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ call ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        function = new Function("greet", Collections.emptyList(), Collections.singletonList(new TypeReference<DynamicArray<Utf8String>>() {
        }));
        
        Map resultCall = Contract.call(web3, tx, function);
        log.debug("resultCall:{}", formatOut(resultCall));
        
        List<Type> utf8Strings = FunctionReturnDecoder.decode(
                resultCall.get("out").toString(),
                function.getOutputParameters());
        for (Type t : utf8Strings) {
            log.debug("output...:{}", t.getValue().toString());
        }
    }
    
    @Test
    public void testDeployContract3() {
        final KeyHolder keyHolder = new KeyHolder(PRIVATE_KEY);
        
        Transaction tx = new Transaction();
        tx.setFrom(ADDRESS);
        tx.setChainId("1");
        tx.setFromChainId("1");
        tx.setToChainId("1");
        tx.setValue("0");
        tx.setInput("");
        tx.setPub(keyHolder.getPublicKey());
        tx.setUseLocal(false);
        tx.setExtra("");
        
        String nonce = web3.getNonce(tx.getChainId(), tx.getFrom()) + "";
        tx.setNonce(nonce);
        tx.setTo("");
        
        String binContent = FilesUtils.getResourcesFileContent("Business.bin");
        try {
            Map result = Contract.deploy(web3, keyHolder, tx, binContent, Collections.emptyList());
            log.debug("result:{}", formatOut(result));
            String hash = result.get("TXhash").toString();
            assert StringUtils.isNotBlank(hash);
            
            Map txInfo = getPendingTxByHash(tx.getChainId(), hash);
            
            log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ send ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            final String contractAddress = txInfo.get("contractAddress").toString();
            assert StringUtils.isNotBlank(contractAddress);
            tx.setTo(contractAddress);
            
            nonce = web3.getNonce(tx.getChainId(), tx.getFrom()) + "";
            tx.setNonce(nonce);
            //Function function1 = new Function("setGreeting", Arrays.asList(new Utf8String("Greetings!")),  Collections.emptyList());
            
            BusinessObj testDataInfo = new BusinessObj();
            testDataInfo.setDataNo("a");
            testDataInfo.setData("b");
            
            Function function1 = new Function("createObj", Arrays.asList(testDataInfo), Collections.emptyList());
            Map resultSend = Contract.send(web3, keyHolder, tx, function1);
            log.debug("resultSend:{}", formatOut(resultSend));
            
            hash = resultSend.get("TXhash").toString();
            assert StringUtils.isNotBlank(hash);
            
            txInfo = getPendingTxByHash(tx.getChainId(), hash);
            
            log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ send-2 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            BusinessObj testDataInfo2 = new BusinessObj();
            testDataInfo2.setDataNo("a2");
            testDataInfo2.setData("b2");
            Function function2 = new Function("createObj", Arrays.asList(testDataInfo2), Collections.emptyList());
            
            nonce = web3.getNonce(tx.getChainId(), tx.getFrom()) + "";
            tx.setNonce(nonce);
            
            Map resultSend2 = Contract.send(web3, keyHolder, tx, function2);
            hash = resultSend2.get("TXhash").toString();
            assert StringUtils.isNotBlank(hash);
            
            txInfo = getPendingTxByHash(tx.getChainId(), hash);
            
            // Function function = new Function("getObjsNum", Arrays.asList(),Arrays.asList(new TypeReference<Int64>() { }));
            // Function function = new Function("getObjById", Arrays.asList(new Utf8String("a")),Arrays.asList(new TypeReference<BusinessObj>() { }));
            
            log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ call ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Function function = new Function("getAllObjs",
                    Arrays.asList(new Uint64(1), new Uint64(5)),
                    Arrays.asList(new TypeReference<Int64>() {
                                  }, new TypeReference<Int64>() {
                                  }, new TypeReference<Int64>() {
                                  },
                            new TypeReference<BusinessObjListResult>() {
                            }));
            Map resultCall = Contract.call(web3, tx, function);
            log.debug("resultCall:{}", formatOut(resultCall));
            
            List<Type> utf8Strings = FunctionReturnDecoder.decode(
                    resultCall.get("out").toString(),
                    function.getOutputParameters());
            //out Greetings!!
            //System.out.println("output...:"+utf8Strings);
//           1.  string list
//            for (Type t:utf8Strings) {
//                System.out.println("output...:"+t.getValue().toString());
//            }
            
            //2.  obj
            log.debug("utf8Strings:{}", formatOut(utf8Strings));
            
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }
    
    @Test
    public void testERC20() {
        final KeyHolder keyHolder = new KeyHolder(PRIVATE_KEY);
        Transaction transaction = new Transaction();
        transaction.setChainId("103");
        transaction.setFrom(ADDRESS);
        transaction.setTo("0xfce16d719cea6c2674457d695a465974411b6fd9");
        transaction.setToChainId("103");
        transaction.setFromChainId("103");
        String nonceVal = web3.getNonce(transaction.getChainId(), transaction.getFrom()) + "";
        transaction.setNonce(nonceVal);
        transaction.setValue("0");
        
        List<Type> list = new ArrayList<>();
        Address recipient = new Address("0x28ef7e4990efa0e6f50f0e4b0204fc5e202b2e2c");
        list.add(recipient);
        list.add(new Uint256(7));
        
        Function function = new Function("transfer", list, Collections.emptyList());
        transaction.setInput(FunctionEncoder.encode(function));
        
        transaction.setPub(keyHolder.getPublicKey());
        transaction.setUseLocal(false);
        transaction.setExtra("");
        transaction.setSig(keyHolder.sign(transaction));
        Map result = web3.sendTx(transaction);
        
        log.debug("result:{}", formatOut(result));
    }
    
    @Test
    public void testCallTransaction() {
        Transaction tx = new Transaction();
        tx.setChainId("1");
        tx.setFrom("0x0000000000000000000000000000000000000000");
        tx.setTo("0x0e50cea0402d2a396b0db1c5d08155bd219cc52e");
        tx.setNonce("15");
        tx.setUseLocal(false);
        tx.setExtra("");
        tx.setValue("0");
        tx.setInput("0xdfc02018");
        tx.setFromChainId("1");
        tx.setToChainId("1");
        Map result = web3.callTransaction(tx);
        log.debug("result:{}", formatOut(result));
    }
    
}
