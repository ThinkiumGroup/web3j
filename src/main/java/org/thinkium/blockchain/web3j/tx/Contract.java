package org.thinkium.blockchain.web3j.tx;

import org.thinkium.blockchain.web3j.abi.*;
import org.thinkium.blockchain.web3j.abi.datatypes.Address;
import org.thinkium.blockchain.web3j.abi.datatypes.Event;
import org.thinkium.blockchain.web3j.abi.datatypes.Function;
import org.thinkium.blockchain.web3j.abi.datatypes.Type;
import org.thinkium.blockchain.web3j.crypto.Credentials;
import org.thinkium.blockchain.web3j.protocol.RemoteCall;
import org.thinkium.blockchain.web3j.protocol.RemoteFunctionCall;
import org.thinkium.blockchain.web3j.protocol.Web3j;
import org.thinkium.blockchain.web3j.protocol.exceptions.TransactionException;
import org.thinkium.blockchain.web3j.protocol.methods.Log;
import org.thinkium.blockchain.web3j.protocol.methods.TransactionReceipt;
import org.thinkium.blockchain.web3j.tx.exceptions.ContractCallException;
import org.thinkium.blockchain.web3j.tx.gas.ContractGasProvider;
import org.thinkium.blockchain.web3j.tx.gas.StaticGasProvider;
import org.thinkium.blockchain.web3j.utils.RevertReasonExtractor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Solidity contract type abstraction for interacting with smart contracts via native Java types.
 */
@SuppressWarnings({"WeakerAccess", "deprecation"})
public abstract class Contract extends ManagedTransaction {
    
    public static final String BIN_NOT_PROVIDED = "Bin file was not provided";
    public static final String FUNC_DEPLOY = "deploy";
    
    protected final String contractBinary;
    protected String contractAddress;
    protected ContractGasProvider gasProvider;
    protected TransactionReceipt transactionReceipt;
    protected Map<String, String> deployedAddresses;
    
    protected Contract(
            String contractBinary,
            String contractAddress,
            Web3j web3j,
            TransactionManager transactionManager,
            ContractGasProvider gasProvider) {
        super(web3j, transactionManager);
        this.contractAddress = resolveContractAddress(contractAddress);
        this.contractBinary = contractBinary;
        this.gasProvider = gasProvider;
    }
    
    protected Contract(
            String contractBinary,
            String chainId,
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            ContractGasProvider gasProvider) {
        this(
                contractBinary,
                contractAddress,
                web3j,
                new RawTransactionManager(web3j, credentials, chainId),
                gasProvider);
    }
    
    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }
    
    public String getContractAddress() {
        return contractAddress;
    }
    
    public void setTransactionReceipt(TransactionReceipt transactionReceipt) {
        this.transactionReceipt = transactionReceipt;
    }
    
    public String getContractBinary() {
        return contractBinary;
    }
    
    public void setGasProvider(ContractGasProvider gasProvider) {
        this.gasProvider = gasProvider;
    }
    
    /**
     * Allow {@code gasPrice} to be set.
     *
     * @param newPrice gas price to use for subsequent transactions
     *
     * @deprecated use ContractGasProvider
     */
    public void setGasPrice(BigInteger newPrice) {
        this.gasProvider = new StaticGasProvider(newPrice, gasProvider.getGasLimit());
    }
    
    /**
     * Get the current {@code gasPrice} value this contract uses when executing transactions.
     *
     * @return the gas price set on this contract
     *
     * @deprecated use ContractGasProvider
     */
    public BigInteger getGasPrice() {
        return gasProvider.getGasPrice();
    }
    
    /**
     * Check that the contract deployed at the address associated with this smart contract wrapper
     * is in fact the contract you believe it is.
     */
    public boolean isValid() throws IOException {
        if (contractBinary.equals(BIN_NOT_PROVIDED)) {
            throw new UnsupportedOperationException("Contract binary not present in contract wrapper, "
                    + "please generate your wrapper using -abiFile=<file>");
        }
        
        if (contractAddress.equals("")) {
            throw new UnsupportedOperationException("Contract binary not present, you will need to regenerate your smart "
                    + "contract wrapper with web3j v2.2.0+");
        }
        
        // TODO getCode bytecode
        // There may be multiple contracts in the Solidity bytecode, hence we only check for a
        // match with a subset
        return true;
    }
    
    /**
     * If this Contract instance was created at deployment, the TransactionReceipt associated with
     * the initial creation will be provided, e.g. via a <em>deploy</em> method. This will not
     * persist for Contracts instances constructed via a <em>load</em> method.
     *
     * @return the TransactionReceipt generated at contract deployment
     */
    public Optional<TransactionReceipt> getTransactionReceipt() {
        return Optional.ofNullable(transactionReceipt);
    }
    
    /**
     * Execute constant function call - i.e. a call that does not change state of the contract
     *
     * @param function to call
     *
     * @return {@link List} of values returned by function call
     */
    private List<Type> executeCall(Function function) throws IOException {
        String encodedFunction = FunctionEncoder.encode(function);
        
        String value = call(contractAddress, encodedFunction);
        
        return FunctionReturnDecoder.decode(value, function.getOutputParameters());
    }
    
    @SuppressWarnings("unchecked")
    protected <T extends Type> T executeCallSingleValueReturn(Function function) throws IOException {
        List<Type> values = executeCall(function);
        if (!values.isEmpty()) {
            return (T) values.get(0);
        } else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    protected <T extends Type, R> R executeCallSingleValueReturn(Function function, Class<R> returnType) throws IOException {
        T result = executeCallSingleValueReturn(function);
        if (result == null) {
            throw new ContractCallException("Empty value (0x) returned from contract");
        }
        
        Object value = result.getValue();
        if (returnType.isAssignableFrom(result.getClass())) {
            return (R) result;
        } else if (returnType.isAssignableFrom(value.getClass())) {
            return (R) value;
        } else if (result.getClass().equals(Address.class) && returnType.equals(String.class)) {
            return (R) result.toString(); // cast isn't necessary
        } else {
            throw new ContractCallException("Unable to convert response: "
                    + value
                    + " to expected type: "
                    + returnType.getSimpleName());
        }
    }
    
    protected List<Type> executeCallMultipleValueReturn(Function function) throws IOException {
        return executeCall(function);
    }
    
    protected TransactionReceipt executeTransaction(Function function) throws IOException, TransactionException {
        return executeTransaction(function, BigInteger.ZERO);
    }
    
    private TransactionReceipt executeTransaction(Function function, BigInteger weiValue) throws IOException, TransactionException {
        return executeTransaction(FunctionEncoder.encode(function), weiValue, function.getName());
    }
    
    TransactionReceipt executeTransaction(String data, BigInteger weiValue, String funcName) throws TransactionException, IOException {
        return executeTransaction(data, weiValue, funcName, false);
    }
    
    /**
     * Given the duration required to execute a transaction.
     *
     * @param data     to send in transaction
     * @param weiValue in Wei to send in transaction
     *
     * @return {@link Optional} containing our transaction receipt
     *
     * @throws IOException          if the call to the node fails
     * @throws TransactionException if the transaction was not mined while waiting
     */
    TransactionReceipt executeTransaction(String data, BigInteger weiValue, String funcName, boolean constructor) throws TransactionException, IOException {
        TransactionReceipt receipt =
                send(contractAddress,
                        data,
                        weiValue,
                        gasProvider.getGasPrice(funcName),
                        gasProvider.getGasLimit(funcName),
                        constructor);
        
        if (!receipt.isStatusOK()) {
            throw new TransactionException(
                    String.format("Transaction %s has failed with status: %s. "
                                    + "Gas used: %s. "
                                    + "Revert reason: '%s'.",
                            receipt.getTransactionHash(),
                            receipt.getStatus(),
                            receipt.getGasUsed(),
                            RevertReasonExtractor.extractRevertReason(receipt)), receipt);
        }
        return receipt;
    }
    
    protected <T extends Type> RemoteFunctionCall<T> executeRemoteCallSingleValueReturn(Function function) {
        return new RemoteFunctionCall<>(function, () -> executeCallSingleValueReturn(function));
    }
    
    protected <T> RemoteFunctionCall<T> executeRemoteCallSingleValueReturn(Function function, Class<T> returnType) {
        return new RemoteFunctionCall<>(
                function, () -> executeCallSingleValueReturn(function, returnType));
    }
    
    protected RemoteFunctionCall<List<Type>> executeRemoteCallMultipleValueReturn(Function function) {
        return new RemoteFunctionCall<>(function, () -> executeCallMultipleValueReturn(function));
    }
    
    protected RemoteFunctionCall<TransactionReceipt> executeRemoteCallTransaction(Function function) {
        return new RemoteFunctionCall<>(function, () -> executeTransaction(function));
    }
    
    protected RemoteFunctionCall<TransactionReceipt> executeRemoteCallTransaction(Function function, BigInteger weiValue) {
        return new RemoteFunctionCall<>(function, () -> executeTransaction(function, weiValue));
    }
    
    private static <T extends Contract> T create(T contract, String binary, String encodedConstructor, BigInteger value) throws IOException, TransactionException {
        TransactionReceipt transactionReceipt = contract.executeTransaction(binary + encodedConstructor, value, FUNC_DEPLOY, true);
        
        String contractAddress = transactionReceipt.getContractAddress();
        if (contractAddress == null) {
            throw new RuntimeException("Empty contract address returned");
        }
        contract.setContractAddress(contractAddress);
        contract.setTransactionReceipt(transactionReceipt);
        
        return contract;
    }
    
    protected static <T extends Contract> T deploy(
            Class<T> type,
            Web3j web3j,
            String chainId,
            Credentials credentials,
            ContractGasProvider contractGasProvider,
            String binary,
            String encodedConstructor,
            BigInteger value)
            throws RuntimeException, TransactionException {
        
        try {
            Constructor<T> constructor = type.getDeclaredConstructor(String.class, String.class, Web3j.class, Credentials.class, ContractGasProvider.class);
            constructor.setAccessible(true);
            T contract = constructor.newInstance(chainId, "", web3j, credentials, contractGasProvider);
            return create(contract, binary, encodedConstructor, value);
        } catch (TransactionException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    protected static <T extends Contract> T deploy(
            Class<T> type,
            Web3j web3j,
            TransactionManager transactionManager,
            ContractGasProvider contractGasProvider,
            String binary,
            String encodedConstructor,
            BigInteger value)
            throws RuntimeException, TransactionException {
        
        try {
            Constructor<T> constructor = type.getDeclaredConstructor(String.class, Web3j.class, TransactionManager.class, ContractGasProvider.class);
            constructor.setAccessible(true);
            T contract = constructor.newInstance("", web3j, transactionManager, contractGasProvider);
            return create(contract, binary, encodedConstructor, value);
        } catch (TransactionException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Web3j web3j,
            String chainId,
            Credentials credentials,
            ContractGasProvider contractGasProvider,
            String binary,
            String encodedConstructor) {
        return new RemoteCall<>(
                () -> deploy(
                        type,
                        web3j,
                        chainId,
                        credentials,
                        contractGasProvider,
                        binary,
                        encodedConstructor,
                        BigInteger.ZERO));
    }
    
    public static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Web3j web3j,
            TransactionManager transactionManager,
            ContractGasProvider contractGasProvider,
            String binary,
            String encodedConstructor) {
        return new RemoteCall<>(()
                -> deploy(
                type,
                web3j,
                transactionManager,
                contractGasProvider,
                binary,
                encodedConstructor,
                BigInteger.ZERO));
    }
    
    public static EventValues staticExtractEventParameters(Event event, Log log) {
        final List<String> topics = log.getTopics();
        String encodedEventSignature = EventEncoder.encode(event);
        if (topics == null || topics.size() == 0 || !topics.get(0).equals(encodedEventSignature)) {
            return null;
        }
        
        List<Type> indexedValues = new ArrayList<>();
        List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters());
        
        List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
        for (int i = 0; i < indexedParameters.size(); i++) {
            Type value = FunctionReturnDecoder.decodeIndexedValue(topics.get(i + 1), indexedParameters.get(i));
            indexedValues.add(value);
        }
        return new EventValues(indexedValues, nonIndexedValues);
    }
    
    protected String resolveContractAddress(String contractAddress) {
        // FIXME  return ensResolver.resolve(contractAddress);
        return contractAddress;
    }
    
    protected EventValues extractEventParameters(Event event, Log log) {
        return staticExtractEventParameters(event, log);
    }
    
    protected List<EventValues> extractEventParameters(Event event, TransactionReceipt transactionReceipt) {
        return transactionReceipt.getLogs().stream()
                .map(log -> extractEventParameters(event, log))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    protected EventValuesWithLog extractEventParametersWithLog(Event event, Log log) {
        return staticExtractEventParametersWithLog(event, log);
    }
    
    protected static EventValuesWithLog staticExtractEventParametersWithLog(Event event, Log log) {
        final EventValues eventValues = staticExtractEventParameters(event, log);
        return (eventValues == null) ? null : new EventValuesWithLog(eventValues, log);
    }
    
    protected List<EventValuesWithLog> extractEventParametersWithLog(Event event, TransactionReceipt transactionReceipt) {
        return transactionReceipt.getLogs().stream()
                .map(log -> extractEventParametersWithLog(event, log))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    /**
     * Subclasses should implement this method to return pre-existing addresses for deployed
     * contracts.
     *
     * @param networkId the network id, for example "1" for the main-net, "3" for ropsten, etc.
     *
     * @return the deployed address of the contract, if known, and null otherwise.
     */
    protected String getStaticDeployedAddress(String networkId) {
        return null;
    }
    
    public final void setDeployedAddress(String networkId, String address) {
        if (deployedAddresses == null) {
            deployedAddresses = new HashMap<>();
        }
        deployedAddresses.put(networkId, address);
    }
    
    public final String getDeployedAddress(String networkId) {
        String addr = null;
        if (deployedAddresses != null) {
            addr = deployedAddresses.get(networkId);
        }
        return addr == null ? getStaticDeployedAddress(networkId) : addr;
    }
    
    /** Adds a log field to {@link EventValues}. */
    public static class EventValuesWithLog {
        private final EventValues eventValues;
        private final Log log;
        
        private EventValuesWithLog(EventValues eventValues, Log log) {
            this.eventValues = eventValues;
            this.log = log;
        }
        
        public List<Type> getIndexedValues() {
            return eventValues.getIndexedValues();
        }
        
        public List<Type> getNonIndexedValues() {
            return eventValues.getNonIndexedValues();
        }
        
        public Log getLog() {
            return log;
        }
    }
    
    @SuppressWarnings("unchecked")
    protected static <S extends Type, T> List<T> convertToNative(List<S> arr) {
        List<T> out = new ArrayList<>();
        for (final S s : arr) {
            out.add((T) s.getValue());
        }
        return out;
    }
}
