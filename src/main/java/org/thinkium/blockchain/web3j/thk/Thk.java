package org.thinkium.blockchain.web3j.thk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkium.blockchain.web3j.thk.models.Account;
import org.thinkium.blockchain.web3j.thk.models.TransactionByHash;
import org.thinkium.blockchain.web3j.thk.models.vo.CashCheque;
import org.thinkium.blockchain.web3j.thk.models.vo.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/** Created by thk on 6/15/19. */
public class Thk {
    private static final Logger log = LoggerFactory.getLogger(Thk.class);
    
    /** RPC */
    private String HOST_URL = "";
    
    public String getHOST_URL() {
        return HOST_URL;
    }
    
    public void setHOST_URL(String HOST_URL) {
        this.HOST_URL = HOST_URL;
    }
    
    public Map getAccount(String chainId, String address) {
        Account account = new Account();
        account.setChainId(chainId);
        account.setAddress(address);
        String jsonObj = JSONObject.toJSONString(account);
        
        String postJson = "{\"method\": \"GetAccount\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL, postJson);
        return (Map) JSON.parse(result);
    }
    
    public Map getTransactionByHash(String chainId, String hash) {
        TransactionByHash info = new TransactionByHash();
        info.setChainId(chainId);
        info.setHash(hash);
        String jsonObj = JSONObject.toJSONString(info);
        String postJson = "{\"method\": \"GetTransactionByHash\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL, postJson);
        return (Map) JSON.parse(result);
    }
    
    public Map getChainStats(String chainId) {
        Map<String, Object> map = new HashMap<>();
        map.put("chainId", chainId);
        String jsonObj = JSONObject.toJSONString(map);
        String postJson = "{\"method\": \"GetStats\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL, postJson);
        return (Map) JSON.parse(result);
    }
    
    public JSONArray GetTransactions(String chainId, String address, String startHeight, String endHeight) {
        Map<String, Object> map = new HashMap<>();
        map.put("chainId", chainId);
        map.put("address", address);
        map.put("startHeight", startHeight);
        map.put("endHeight", endHeight);
        String jsonObj = JSONObject.toJSONString(map);
        String postJson = "{\"method\": \"GetTransactions\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL, postJson);
        return JSON.parseArray(result);
    }
    
    public Map getBlockHeader(String chainId, String height) {
        Map<String, Object> map = new HashMap<>();
        map.put("chainId", chainId);
        map.put("height", height);
        String jsonObj = JSONObject.toJSONString(map);
        String postJson = "{\"method\": \"GetBlockHeader\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL, postJson);
        return (Map) JSON.parse(result);
    }
    
    public Map getBlockTxs(String chainId, String height, String page, String size) {
        Map<String, Object> map = new HashMap<>();
        map.put("chainId", chainId);
        map.put("height", height);
        map.put("page", page);
        map.put("size", size);
        String jsonObj = JSONObject.toJSONString(map);
        String postJson = "{\"method\": \"GetBlockTxs\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL, postJson);
        return (Map) JSON.parse(result);
    }
    
    @Deprecated
    public Map sendTx(String chainId, String fromChainId, String toChainId, String sig, String pub, String from,
                      String to, String nonce, String value, String input) {
        Map<String, Object> map = new HashMap<>();
        map.put("chainId", chainId);
        map.put("fromChainId", fromChainId);
        map.put("toChainId", toChainId);
        map.put("sig", sig);
        map.put("pub", pub);
        map.put("from", from);
        map.put("to", to);
        map.put("nonce", nonce);
        map.put("value", value);
        map.put("input", input);
        //map.put("ExpireHeight",ExpireHeight);
        
        String jsonObj = JSONObject.toJSONString(map);
        String postJson = "{\"method\": \"SendTx\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL, postJson);
        return (Map) JSON.parse(result);
    }
    
    public Map sendTx(Transaction info) {
        Map<String, Object> map = new HashMap<>();
        map.put("chainId", info.getChainId());
        map.put("fromChainId", info.getFromChainId());
        map.put("toChainId", info.getToChainId());
        map.put("sig", info.getSig());
        map.put("pub", info.getPub());
        map.put("from", info.getFrom());
        map.put("to", info.getTo());
        map.put("nonce", info.getNonce());
        map.put("value", info.getValue());
        map.put("input", info.getInput());
        map.put("useLocal", info.getUseLocal());
        map.put("extra", info.getExtra());
        //map.put("ExpireHeight",ExpireHeight);
        
        String jsonObj = JSONObject.toJSONString(map);
        String postJson = "{\"method\": \"SendTx\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL, postJson);
        return (Map) JSON.parse(result);
    }
    
    public int getNonce(String chainId, String address) {
        Account account = new Account();
        account.setChainId(chainId);
        account.setAddress(address);
        String jsonObj = JSONObject.toJSONString(account);
        String postJson = "{\"method\": \"GetAccount\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL, postJson);
        Map maps = (Map) JSON.parse(result);
        if (maps.containsKey("nonce")) {
            return (int) maps.get("nonce");
        }
        return 0;
    }
    
    public Map SaveContract(String address, String Contract) {
        Map<String, Object> map = new HashMap<>();
        map.put("contractaddr", address);
        map.put("contract", Contract);
        String jsonObj = JSONObject.toJSONString(map);
        String postJson = "{\"method\": \"SaveContract\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL, postJson);
        return (Map) JSON.parse(result);
    }
    
    public JSONArray getChainInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("chainIds", "[]");
        String jsonObj = JSONObject.toJSONString(map);
        String postJson = "{\"method\": \"GetChainInfo\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL + "/chaininfo", postJson);
        return JSON.parseArray(result);
    }
    
    public JSONArray getCommittee(String chainId, String epoch) {
        Map<String, Object> map = new HashMap<>();
        map.put("chainId", chainId);
        map.put("epoch", epoch);
        
        String jsonObj = JSONObject.toJSONString(map);
        String postJson = "{\"method\": \"GetCommittee\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL + "/chaininfo", postJson);
        return JSON.parseArray(result);
    }
    
    @Deprecated
    public Map callTransaction(String chainId, String from, String to, String nonce, String value, String input) {
        Map<String, Object> map = new HashMap<>();
        map.put("chainId", chainId);
        map.put("from", from);
        map.put("to", to);
        map.put("nonce", nonce);
        map.put("value", value);
        map.put("input", input);
        map.put("fromChainId", chainId);
        map.put("toChainId", chainId);
        
        String jsonObj = JSONObject.toJSONString(map);
        String postJson = "{\"method\": \"CallTransaction\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL, postJson);
        return (Map) JSON.parse(result);
    }
    
    public Map callTransaction(Transaction info) {
        Map<String, Object> map = new HashMap<>();
        map.put("chainId", info.getChainId());
        map.put("from", info.getFrom());
        map.put("to", info.getTo());
        map.put("nonce", info.getNonce());
        map.put("value", info.getValue());
        map.put("input", info.getInput());
        map.put("fromChainId", info.getFromChainId());
        map.put("toChainId", info.getToChainId());
        map.put("useLocal", info.getUseLocal());
        map.put("extra", info.getExtra());
        String jsonObj = JSONObject.toJSONString(map);
        String postJson = "{\"method\": \"CallTransaction\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL, postJson);
        return (Map) JSON.parse(result);
    }
    
    /**
     * Obtain proof of cashing or revoking a check
     *
     * @param cashCheque   Obtain the necessary parameters for proof. Note that tochain is passed to the ID of the initiating chain when revoking, which is the opposite of generating a check
     * @param cashOrCancel True cash, false undo
     */
    public Map RpcMakeVccProof(CashCheque cashCheque, boolean cashOrCancel) {
        Map<String, Object> map = new HashMap<>();
        map.put("chainId", cashCheque.getChainId() + "");
        map.put("from", cashCheque.getFromAddress());
        map.put("to", cashCheque.getToAddress());
        map.put("fromChainId", cashCheque.getFromChainId() + "");
        map.put("toChainId", cashCheque.getToChainId() + "");
        map.put("value", cashCheque.getAmount().toString());
        map.put("nonce", cashCheque.getNonce() + "");
        map.put("expireheight", cashCheque.getExpireHeight() + "");
        
        String method = cashOrCancel ? "RpcMakeVccProof" : "MakeCCCExistenceProof";
        String jsonObj = JSONObject.toJSONString(map);
        String postJson = "{\"method\": \"" + method + "\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL, postJson);
        return (Map) JSON.parse(result);
    }
    
    public Map Ping(String address) {
        Map<String, Object> map = new HashMap<>();
        map.put("address", address);
        String jsonObj = JSONObject.toJSONString(map);
        String postJson = "{\"method\": \"Ping\",\"params\": " + jsonObj + "}";
        String result = post(HOST_URL + "/chaininfo", postJson);
        return (Map) JSON.parse(result);
    }
    
    private static String post(String strURL, String params) {
        log.debug("params:{} ", params);
        BufferedReader reader = null;
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.append(params);
            out.flush();
            out.close();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            
            String line;
            
            StringBuilder res = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                res.append(line);
            }
            reader.close();
            return res.toString();
        } catch (IOException e) {
            log.error("rpc call error", e);
        }
        return "error";
    }
}
