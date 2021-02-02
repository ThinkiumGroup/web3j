package org.thinkium.blockchain.web3j.utils;


import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;

/**
 * @author HarryPotter
 * @date 18:46 2020/7/2
 * @email harry@potter.com
 */
public class RevertReasonExtractorTest {
    
    public static void main(String[] args) {
        final TransactionReceipt transactionReceipt = new TransactionReceipt();
        String err = "0x08c379a000000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000012496e73756666696369656e742066756e64730000000000000000000000000000";
        transactionReceipt.setOut(err);
        final String errMsg = RevertReasonExtractor.extractRevertReason(transactionReceipt);
        System.out.println(errMsg);
    }
}
