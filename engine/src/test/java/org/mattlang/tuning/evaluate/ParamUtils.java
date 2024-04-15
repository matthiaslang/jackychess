package org.mattlang.tuning.evaluate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ParamUtils {

    public static void exchangeParam(File file, String name, int value) {
        exchangeParam(file, name, Integer.toString(value));
    }

    public static void exchangeParam(File file, String name, float value) {
        exchangeParam(file, name, Float.toString(value));
    }

    public static void exchangeParam(File file, String name, String value) {
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).startsWith(name + "=")) {
                    fileContent.set(i, name + "=" + value);
                    break;
                }
            }

            Files.write(file.toPath(), fileContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
