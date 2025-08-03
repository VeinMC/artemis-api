package dev.artemiscore.api.config.file.properties;

import dev.artemiscore.api.config.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PropertiesConfiguration extends FileConfiguration {
    private final Properties properties = new Properties();
    private File file;

    @Override
    public void load(@NotNull InputStream inputStream) throws IOException {
        try (inputStream) {
            this.properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            this.contents = new HashMap<>();
            this.properties.stringPropertyNames().forEach(f -> this.contents.put(f, this.properties.getProperty(f)));
        }
    }

    @Override
    public void load(@NotNull String content) {
        try {
            this.properties.load(new StringReader(content));
            this.contents = new HashMap<>();
            this.properties.stringPropertyNames().forEach(f -> this.contents.put(f, this.properties.getProperty(f)));
        } catch (IOException exception) {
            throw new IllegalArgumentException("Invalid .properties content", exception);
        }
    }

    @Override
    public void load(@NotNull File file) throws IOException {
        if (!file.exists() || !file.canRead()) throw new IllegalArgumentException("File does not exist or is not readable: %s".formatted(file.getAbsolutePath()));
        this.file = file;
        try (InputStream inputStream = new FileInputStream(file)) {
            this.load(inputStream);
        }
    }

    @Override
    public void save() {
        if (this.file == null) throw new IllegalStateException("Configuration is not associated with a file.");
        this.save(this.file);
    }

    @Override
    public void save(@NotNull File file) {
        if (this.contents == null) throw new IllegalStateException("No content to save.");
        try (OutputStream outputStream = new FileOutputStream(file)) {
            Properties properties = new Properties();
            this.contents.forEach((key, value) -> properties.setProperty(key, value.toString()));
            properties.store(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), null);
            this.file = file;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to save .properties file", exception);
        }
    }

    @Override
    public String dump() {
        if (this.contents == null || this.contents.isEmpty()) return "";
        StringWriter stringWriter = new StringWriter();
        try {
            Properties properties = new Properties();
            this.contents.forEach((key, value) -> properties.setProperty(key, value.toString()));
            properties.store(stringWriter, null);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to dump properties", exception);
        }
        return stringWriter.toString();
    }
}
