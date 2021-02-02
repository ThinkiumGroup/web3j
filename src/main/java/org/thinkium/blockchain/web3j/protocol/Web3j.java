package org.thinkium.blockchain.web3j.protocol;

import org.thinkium.blockchain.web3j.protocol.core.JsonRpcWeb3;

/**
 * @author HarryPotter
 * @date 15:00 2020/11/16
 * @email harry@potter.com
 */
public interface Web3j extends IThk {
    
    /**
     * Construct a new Web3j instance.
     *
     * @param web3jService web3j service instance - i.e. HTTP or IPC
     *
     * @return new Web3j instance
     */
    static Web3j load(Web3jService web3jService) {
        return new JsonRpcWeb3(web3jService);
    }
    
    /** Shutdowns a Web3j instance and closes opened resources. */
    void shutdown();
}
