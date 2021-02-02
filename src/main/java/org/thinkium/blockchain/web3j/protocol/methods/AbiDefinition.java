/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.thinkium.blockchain.web3j.protocol.methods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** AbiDefinition wrapper. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbiDefinition {
    private boolean constant;
    private List<NamedType> inputs = new ArrayList<>();
    private String name;
    private List<NamedType> outputs = new ArrayList<>();
    private String type;
    private boolean payable;

    /**
     * The stateMutability function modifier.
     *
     * <p>this does not factor into the <code>#hashCode()</code> or <code>#equals()</code> logic
     * since multiple functions with the same signature that only differ in mutability are not
     * allowed in Solidity.
     *
     * <p>Valid values are:
     *
     * <ul>
     *   <li>pure
     *   <li>view
     *   <li>nonpayable
     *   <li>payable
     * </ul>
     */
    private String stateMutability;

    public AbiDefinition() {}

    public AbiDefinition(AbiDefinition from) {
        this(
                from.constant,
                clone(from.inputs),
                from.name,
                clone(from.outputs),
                from.type,
                from.payable,
                from.stateMutability);
    }

    public AbiDefinition(
            boolean constant,
            List<NamedType> inputs,
            String name,
            List<NamedType> outputs,
            String type,
            boolean payable) {
        this(constant, inputs, name, outputs, type, payable, null);
    }

    public AbiDefinition(
            boolean constant,
            List<NamedType> inputs,
            String name,
            List<NamedType> outputs,
            String type,
            boolean payable,
            String stateMutability) {
        this.constant = constant;
        this.inputs = inputs;
        this.name = name;
        this.outputs = outputs;
        this.type = type;
        this.payable = payable;
        this.stateMutability = stateMutability;
    }

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    public List<NamedType> getInputs() {
        return inputs;
    }

    public void setInputs(List<NamedType> inputs) {
        this.inputs = inputs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NamedType> getOutputs() {
        return outputs;
    }

    public boolean hasOutputs() {
        return !outputs.isEmpty();
    }

    public void setOutputs(List<NamedType> outputs) {
        this.outputs = outputs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPayable() {
        return payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    public String getStateMutability() {
        return stateMutability;
    }

    public void setStateMutability(String stateMutability) {
        this.stateMutability = stateMutability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbiDefinition)) {
            return false;
        }

        AbiDefinition that = (AbiDefinition) o;

        if (isConstant() != that.isConstant()) {
            return false;
        }
        if (isPayable() != that.isPayable()) {
            return false;
        }
        if (getInputs() != null
                ? !getInputs().equals(that.getInputs())
                : that.getInputs() != null) {
            return false;
        }
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
            return false;
        }
        if (getOutputs() != null
                ? !getOutputs().equals(that.getOutputs())
                : that.getOutputs() != null) {
            return false;
        }
        if (getStateMutability() != null
                ? !getStateMutability().equals(that.getStateMutability())
                : that.getStateMutability() != null) {
            return false;
        }
        return getType() != null ? getType().equals(that.getType()) : that.getType() == null;
    }

    @Override
    public int hashCode() {
        int result = (isConstant() ? 1 : 0);
        result = 31 * result + (getInputs() != null ? getInputs().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getOutputs() != null ? getOutputs().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (isPayable() ? 1 : 0);
        result = 31 * result + (getStateMutability() != null ? getStateMutability().hashCode() : 0);
        return result;
    }

    public static class NamedType {
        private static String DEFAULT_INTERNAL_TYPE = "";

        private String name;
        private String type;
        private List<NamedType> components = new ArrayList<>();
        private String internalType = DEFAULT_INTERNAL_TYPE;
        private boolean indexed;

        public NamedType() {}

        public NamedType(NamedType from) {
            this(from.name, from.type, from.indexed);
        }

        public NamedType(String name, String type) {
            this(name, type, false);
        }

        public NamedType(String name, String type, boolean indexed) {
            this(name, type, Collections.emptyList(), DEFAULT_INTERNAL_TYPE, indexed);
        }

        public NamedType(
                String name,
                String type,
                List<NamedType> components,
                String internalType,
                boolean indexed) {
            this.name = name;
            this.type = type;
            this.components = components;
            this.internalType = internalType;
            this.indexed = indexed;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getInternalType() {
            return internalType;
        }

        public void setInternalType(final String internalType) {
            this.internalType = internalType;
        }

        public boolean isIndexed() {
            return indexed;
        }

        public void setIndexed(boolean indexed) {
            this.indexed = indexed;
        }

        public List<NamedType> getComponents() {
            return components;
        }

        public void setComponents(final List<NamedType> components) {
            this.components = components;
        }

        public int structIdentifier() {
            return ((internalType == null ? type : internalType.isEmpty() ? type : internalType)
                            + components.stream()
                                    .map(namedType -> String.valueOf(namedType.structIdentifier()))
                                    .collect(Collectors.joining()))
                    .hashCode();
        }

        public int nestedness() {
            if (getComponents().size() == 0) {
                return 0;
            }
            return 1 + getComponents().stream().mapToInt(NamedType::nestedness).max().getAsInt();
        }

        public boolean isDynamic() {
            if (getType().equals("string")
                    || getType().equals("bytes")
                    || getType().contains("[]")) {
                return true;
            }
            if (components.stream().anyMatch(NamedType::isDynamic)) {
                return true;
            }
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof NamedType)) {
                return false;
            }

            NamedType namedType = (NamedType) o;

            if (isIndexed() != namedType.isIndexed()) {
                return false;
            }

            if (getName() != null
                    ? !getName().equals(namedType.getName())
                    : namedType.getName() != null) {
                return false;
            }

            if (getComponents() != null
                    ? !getComponents().equals(namedType.getComponents())
                    : namedType.getComponents() != null) {
                return false;
            }

            if (getInternalType() != null
                    ? !getInternalType().equals(namedType.getInternalType())
                    : namedType.getInternalType() != null) {
                return false;
            }

            return getType() != null
                    ? getType().equals(namedType.getType())
                    : namedType.getType() == null;
        }

        @Override
        public int hashCode() {
            int result = getName() != null ? getName().hashCode() : 0;
            result = 31 * result + (getType() != null ? getType().hashCode() : 0);
            result = 31 * result + (isIndexed() ? 1 : 0);
            result = 31 * result + (getComponents() != null ? getComponents().hashCode() : 0);
            result = 31 * result + (getInternalType() != null ? getInternalType().hashCode() : 0);
            return result;
        }
    }

    private static List<NamedType> clone(final List<NamedType> from) {
        return from.stream().map(NamedType::new).collect(Collectors.toList());
    }
}
