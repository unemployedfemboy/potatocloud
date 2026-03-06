package net.potatocloud.node.utils;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class PropertiesFileUtils {

    private PropertiesFileUtils() {
    }

    @SneakyThrows
    public static Properties loadProperties(Path path) {
        final Properties properties = new Properties();

        try (FileInputStream in = new FileInputStream(path.toFile())) {
            properties.load(in);
            return properties;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @SneakyThrows
    public static void saveProperties(Properties properties, Path filePath) {
        try (OutputStream out = Files.newOutputStream(filePath)) {
            properties.store(out, null);
        }
    }
}
