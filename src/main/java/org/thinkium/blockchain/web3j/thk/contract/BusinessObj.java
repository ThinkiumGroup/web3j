package org.thinkium.blockchain.web3j.thk.contract;

import org.thinkium.blockchain.web3j.abi.datatypes.Type;

public class BusinessObj implements Type {
    
    private String dataNo;
    private String data;
    
    public String getDataNo() {
        return dataNo;
    }
    
    public void setDataNo(String dataNo) {
        this.dataNo = dataNo;
    }
    
    public String getData() {
        return data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    @Override
    public Object getValue() {
        return null;
    }
    
    @Override
    public String getTypeAsString() {
        return "(string,string)";
    }
    
    
    public static class BusinessObjResult extends BusinessObj {
        public Integer getOffset() {
            return offset;
        }
        
        public void setOffset(Integer offset) {
            this.offset = offset;
        }
        
        private Integer offset;
    }
}
