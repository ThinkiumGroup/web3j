package org.thinkium.blockchain.web3j.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by thk on 6/20/19.
 */
public class FilesUtils {
    
    public static String getResourcesFileContent(String fileName) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            return readAllLines(inputStream);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public static String readAllLines(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        for (String line; (line = br.readLine()) != null; ) {
            result.append(line);
        }
        return result.toString();
    }
}
