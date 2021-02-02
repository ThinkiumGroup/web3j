package org.thinkium.blockchain.web3j.thk.contract;

import org.thinkium.blockchain.web3j.abi.datatypes.Type;

import java.util.List;

public class BusinessObjListResult implements Type {
    public List<BusinessObj> getBusinessObjList() {
        return businessObjList;
    }
    
    public void setBusinessObjList(List<BusinessObj> businessObjList) {
        this.businessObjList = businessObjList;
    }
    
    private List<BusinessObj> businessObjList;
    
    @Override
    public Object getValue() {
        return null;
    }
    
    @Override
    public String getTypeAsString() {
        return null;
    }
}
