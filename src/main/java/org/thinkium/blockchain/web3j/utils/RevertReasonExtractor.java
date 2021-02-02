package org.thinkium.blockchain.web3j.utils;

import org.apache.commons.lang3.StringUtils;
import org.thinkium.blockchain.web3j.abi.FunctionReturnDecoder;
import org.thinkium.blockchain.web3j.abi.TypeReference;
import org.thinkium.blockchain.web3j.abi.datatypes.Type;
import org.thinkium.blockchain.web3j.abi.datatypes.Utf8String;
import org.thinkium.blockchain.web3j.abi.datatypes.generated.AbiTypes;
import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;

import java.util.Collections;
import java.util.List;

/** Revert reason extraction and retrieval functions. */
public class RevertReasonExtractor {
    
    private static final String errorMethodId = "0x08c379a0";
    private static final List<TypeReference<Type>> revertReasonType = Collections.singletonList(TypeReference.create((Class<Type>) AbiTypes.getType("string")));
    private static final String MISSING_REASON = "N/A";
    
    
    private static boolean isErrorInResult(TransactionReceipt transactionReceipt) {
        return StringUtils.isNotBlank(transactionReceipt.getOut()) && transactionReceipt.getOut().startsWith(errorMethodId);
    }
    
    public static String extractRevertReason(TransactionReceipt transactionReceipt) {
        if (isErrorInResult(transactionReceipt)) {
            String hexRevertReason = transactionReceipt.getOut().substring(errorMethodId.length());
            List<Type> decoded = FunctionReturnDecoder.decode(hexRevertReason, revertReasonType);
            Utf8String decodedRevertReason = (Utf8String) decoded.get(0);
            return decodedRevertReason.getValue();
        }
        return MISSING_REASON;
    }
}
