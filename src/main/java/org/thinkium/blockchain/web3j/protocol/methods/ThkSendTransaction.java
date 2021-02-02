package org.thinkium.blockchain.web3j.protocol.methods;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.thinkium.blockchain.web3j.protocol.ObjectMapperFactory;
import org.thinkium.blockchain.web3j.protocol.Response;

import java.io.IOException;
import java.util.Map;

/**
 * @author HarryPotter
 * @date 22:51 2020/11/15
 * @email harry@potter.com
 */
public class ThkSendTransaction extends Response<ThkSendTransaction.SendResult> {
    @Override
    @JsonDeserialize(using = ResponseDeserializer.class)
    public void setResult(SendResult result) {
        super.setResult(result);
    }
    
    public String getHash() {
        return getResult().getTXhash();
    }
    
    public static class SendResult {
        private String TXhash;
        
        public SendResult() {
        }
        
        public SendResult(String TXhash) {
            this.TXhash = TXhash;
        }
        
        public String getTXhash() {
            return TXhash;
        }
        
        public void setTXhash(String TXhash) {
            this.TXhash = TXhash;
        }
    }
    
    public static class ResponseDeserializer extends JsonDeserializer<SendResult> {
        
        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();
        
        @Override
        public SendResult deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                final Map map = objectReader.readValue(jsonParser, Map.class);
                return new SendResult((String) map.get("TXhash"));
            } else {
                return null; // null is wrapped by Optional in above getter
            }
        }
    }
}