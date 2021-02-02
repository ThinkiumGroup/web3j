package org.thinkium.blockchain.web3j.thk.contract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkium.blockchain.web3j.abi.FunctionEncoder;
import org.thinkium.blockchain.web3j.abi.datatypes.Function;
import org.thinkium.blockchain.web3j.abi.datatypes.Type;
import org.thinkium.blockchain.web3j.thk.KeyHolder;
import org.thinkium.blockchain.web3j.thk.Thk;
import org.thinkium.blockchain.web3j.thk.models.vo.Transaction;

import java.util.List;
import java.util.Map;

/**
 * Created by thk on 6/19/19.
 */
public class Contract {
    
    private static final Logger log = LoggerFactory.getLogger(Contract.class);
    
    public static Map deploy(Thk thk, KeyHolder keyHolder, Transaction tx, String binContent, List<Type> parameters) {
        String result = FunctionEncoder.encodeConstructor(parameters);
        log.debug("parameters packed:{}", result);
        String fastr = "";
        if (result.length() > 2) {
            fastr = result.substring(2);
        }
        if (fastr.contains("0x")) {
            result = result.substring(2, fastr.length());
        }
        log.debug("parameters packed:{}", result);
        String input = binContent + result;
        String faInput = input.substring(2);
        if (!faInput.contains("0x")) {
            input = "0x" + input;
        }
        tx.setInput(input);
        tx.setPub(keyHolder.getPublicKey());
        tx.setSig(keyHolder.sign(tx));
        return thk.sendTx(tx);
    }
    
    public static Map send(Thk thk, KeyHolder keyHolder, Transaction tx, Function function) {
        String input = FunctionEncoder.encode(function);
        tx.setInput(input);
        tx.setPub(keyHolder.getPublicKey());
        tx.setSig(keyHolder.sign(tx));
        return thk.sendTx(tx);
    }
    
    public static Map call(Thk thk, Transaction tx, Function function) {
        String input = FunctionEncoder.encode(function);
        tx.setInput(input);
        return thk.callTransaction(tx);
    }
}
