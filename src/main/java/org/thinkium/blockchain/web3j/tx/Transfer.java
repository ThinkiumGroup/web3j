package org.thinkium.blockchain.web3j.tx;

import org.thinkium.blockchain.web3j.protocol.Web3j;
import org.thinkium.blockchain.web3j.protocol.exceptions.TransactionException;
import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;
import org.thinkium.blockchain.web3j.utils.Convert;
import org.thinkium.blockchain.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Transfer extends ManagedTransaction {
    
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000);
    
    public Transfer(Web3j web3j, TransactionManager transactionManager) {
        super(web3j, transactionManager);
    }
    
    public TransactionReceipt send(String toAddress, BigDecimal value, Convert.Unit unit) throws IOException, InterruptedException, TransactionException {
        BigInteger gasPrice = requestCurrentGasPrice();
        return send(toAddress, value, unit, gasPrice, GAS_LIMIT);
    }
    
    public TransactionReceipt send(String toAddress, BigDecimal value, Convert.Unit unit, BigInteger gasPrice, BigInteger gasLimit) throws IOException, InterruptedException, TransactionException {
        BigDecimal weiValue = Convert.toWei(value, unit);
        if (!Numeric.isIntegerValue(weiValue)) {
            throw new UnsupportedOperationException(
                    "Non decimal Wei value provided: "
                            + value + " "
                            + unit.toString() + " = "
                            + weiValue + " Wei");
        }
        
        return send(toAddress, "", weiValue.toBigIntegerExact(), gasPrice, gasLimit);
    }
}
