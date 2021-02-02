package org.thinkium.blockchain.web3j.thk.contract;

/**
 * @author HarryPotter
 * @date 11:33 2020/7/7
 * @email harry@potter.com
 */
public interface ContractConstants {
    /** system contract address */
    interface SystemContractAddress {
        // withdraw tkm to a check
        String WITHDRAW = "0x0000000000000000000000000000000000020000";
        // deposit tkm - From check deposit to account
        String DEPOSIT = "0x0000000000000000000000000000000000030000";
        // cancel a check - return from check to original account
        String CANCEL = "0x0000000000000000000000000000000000040000";
    }
}
