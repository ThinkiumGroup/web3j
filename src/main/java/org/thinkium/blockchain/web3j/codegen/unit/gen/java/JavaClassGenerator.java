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
package org.thinkium.blockchain.web3j.codegen.unit.gen.java;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.thinkium.blockchain.web3j.codegen.unit.gen.MethodFilter;
import org.thinkium.blockchain.web3j.codegen.unit.gen.UnitClassGenerator;
import org.thinkium.blockchain.web3j.utils.JavaVersion;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

import static org.thinkium.blockchain.web3j.codegen.unit.gen.utils.NameUtils.toCamelCase;

/**
 * Class that generates the unit tests classes for the contracts. The class writes to
 * src/test/java/contracts and each file is named after the contract + "Test" e.g GreeterTest
 */
public class JavaClassGenerator implements UnitClassGenerator {
    private final Class theContract;
    private final String packageName;
    private final String writePath;
    
    public JavaClassGenerator(final Class theContract, final String packageName, String writePath) {
        this.theContract = theContract;
        this.packageName = packageName;
        this.writePath = writePath;
    }
    
    @Override
    public void writeClass() throws IOException {
        ClassName EVM_ANNOTATION = ClassName.get("org.web3j", "EVMTest");
        AnnotationSpec.Builder annotationSpec = AnnotationSpec.builder(EVM_ANNOTATION);
        if (JavaVersion.getJavaVersionAsDouble() < 11) {
            ClassName GethContainer = ClassName.get("org.web3j", "NodeType");
            annotationSpec.addMember("value", "type = $T.GETH", GethContainer);
        }
        TypeSpec testClass =
                TypeSpec.classBuilder(theContract.getSimpleName() + "Test")
                        .addMethods(MethodFilter.generateMethodSpecsForEachTest(theContract))
                        .addAnnotation((annotationSpec).build())
                        .addField(
                                theContract,
                                toCamelCase(theContract),
                                Modifier.PRIVATE,
                                Modifier.STATIC)
                        .build();
        JavaFile javaFile = JavaFile.builder(packageName, testClass).build();
        javaFile.writeTo(new File(writePath));
    }
}
