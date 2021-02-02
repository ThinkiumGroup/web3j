package org.thinkium.blockchain.web3j.codegen;

import org.junit.Test;
import org.thinkium.blockchain.web3j.utils.Strings;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.thinkium.blockchain.web3j.codegen.FunctionWrapperGenerator.JAVA_TYPES_ARG;

public class SolidityFunctionWrapperGeneratorTest {
    
    private String solidityBaseDir = new File("src/test/resources/solidity").getAbsolutePath();
    private String outDirPath = new File("src/test/java").getAbsolutePath();
    
    @Test
    public void testHumanStandardTokenGeneration() throws Exception {
        String packageName = "org.thinkium.blockchain.web3j.codegen";
        List<String> files = Arrays.asList(
                "HumanStandardToken",
                "Token");
        for (String file : files) {
            testCodeGeneration(file, packageName, true, false);
        }
    }
    
    private void testCodeGeneration(String inputFileName, String packageName, boolean useBin, boolean verify) throws Exception {
        List<String> options = new ArrayList<>();
        options.add(JAVA_TYPES_ARG);
        if (useBin) {
            options.add("-b");
            options.add(solidityBaseDir + File.separator
                    + "build" + File.separator
                    + inputFileName + ".bin");
        }
        options.add("-a");
        options.add(solidityBaseDir + File.separator
                + "build" + File.separator
                + inputFileName + ".abi");
        options.add("-p");
        options.add(packageName);
        options.add("-o");
        options.add(outDirPath);
        
        SolidityFunctionWrapperGenerator.main(options.toArray(new String[options.size()]));
        
        if (verify) {
            verifyGeneratedCode(outDirPath + File.separator
                    + packageName.replace('.', File.separatorChar) + File.separator
                    + Strings.capitaliseFirstLetter(inputFileName) + ".java");
        }
    }
    
    private void verifyGeneratedCode(String sourceFile) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(sourceFile));
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
            boolean result = task.call();
            
            System.out.println(diagnostics.getDiagnostics());
            assertTrue("Generated contract contains compile time error", result);
        }
    }
}
