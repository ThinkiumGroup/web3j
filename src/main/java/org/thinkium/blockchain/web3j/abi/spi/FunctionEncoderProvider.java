package org.thinkium.blockchain.web3j.abi.spi;

import org.thinkium.blockchain.web3j.abi.FunctionEncoder;

import java.util.function.Supplier;

/** Function encoding Service Provider Interface. */
public interface FunctionEncoderProvider extends Supplier<FunctionEncoder> {
}
