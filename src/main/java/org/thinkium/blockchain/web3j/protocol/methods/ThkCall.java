package org.thinkium.blockchain.web3j.protocol.methods;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.thinkium.blockchain.web3j.abi.FunctionReturnDecoder;
import org.thinkium.blockchain.web3j.abi.TypeReference;
import org.thinkium.blockchain.web3j.abi.datatypes.Type;
import org.thinkium.blockchain.web3j.abi.datatypes.Utf8String;
import org.thinkium.blockchain.web3j.abi.datatypes.generated.AbiTypes;
import org.thinkium.blockchain.web3j.protocol.ObjectMapperFactory;
import org.thinkium.blockchain.web3j.protocol.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class ThkCall extends Response<String> {
    
    // Numeric.toHexString(Hash.sha3("Error(string)".getBytes())).substring(0, 10)
    private static final String errorMethodId = "0x08c379a0";
    
    @SuppressWarnings("unchecked")
    private static final List<TypeReference<Type>> revertReasonType = Collections.singletonList(TypeReference.create((Class<Type>) AbiTypes.getType("string")));
    
    @Override
    @JsonDeserialize(using = ResponseDeserializer.class)
    public void setResult(String result) {
        super.setResult(result);
    }
    
    public String getValue() {
        return getResult();
    }
    
    public boolean isReverted() {
        return hasError() || isErrorInResult();
    }
    
    private boolean isErrorInResult() {
        return getValue() != null && getValue().startsWith(errorMethodId);
    }
    
    public String getRevertReason() {
        if (isErrorInResult()) {
            String hexRevertReason = getValue().substring(errorMethodId.length());
            List<Type> decoded = FunctionReturnDecoder.decode(hexRevertReason, revertReasonType);
            Utf8String decodedRevertReason = (Utf8String) decoded.get(0);
            return decodedRevertReason.getValue();
        } else if (hasError()) {
            return getError().getMessage();
        }
        return null;
    }
    
    public static class ResponseDeserializer extends JsonDeserializer<String> {
        
        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();
        
        @Override
        public String deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                final Map map = objectReader.readValue(jsonParser, Map.class);
                return (String) map.get("out");
            } else {
                return null; // null is wrapped by Optional in above getter
            }
        }
    }
}
