package org.thinkium.blockchain.web3j.abi.datatypes;

import org.thinkium.blockchain.web3j.abi.datatypes.generated.AbiTypes;

import java.util.List;

/** Dynamic array type. */
public class DynamicArray<T extends Type> extends Array<T> {
    
    @Deprecated
    @SafeVarargs
    @SuppressWarnings({"unchecked"})
    public DynamicArray(T... values) {
        super((Class<T>) AbiTypes.getType(values[0].getTypeAsString()), values);
    }
    
    @Deprecated
    @SuppressWarnings("unchecked")
    public DynamicArray(List<T> values) {
        super((Class<T>) AbiTypes.getType(values.get(0).getTypeAsString()), values);
    }
    
    @Deprecated
    @SuppressWarnings("unchecked")
    private DynamicArray(String type) {
        super((Class<T>) AbiTypes.getType(type));
    }
    
    @Deprecated
    public static DynamicArray empty(String type) {
        return new DynamicArray(type);
    }
    
    public DynamicArray(Class<T> type, List<T> values) {
        super(type, values);
    }
    
    @Override
    public int bytes32PaddedLength() {
        return super.bytes32PaddedLength() + MAX_BYTE_LENGTH;
    }
    
    @SafeVarargs
    public DynamicArray(Class<T> type, T... values) {
        super(type, values);
    }
    
    @Override
    public String getTypeAsString() {
        return AbiTypes.getTypeAString(getComponentType()) + "[]";
    }
}
