package org.thinkium.blockchain.web3j.abi.spi;

import org.thinkium.blockchain.web3j.abi.FunctionReturnDecoder;

import java.util.function.Supplier;

/** Function decoding Service Provider Interface. */
public interface FunctionReturnDecoderProvider extends Supplier<FunctionReturnDecoder> {
}
