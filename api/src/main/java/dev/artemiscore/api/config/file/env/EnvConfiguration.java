package dev.artemiscore.api.config.file.env;

import dev.artemiscore.api.config.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EnvConfiguration extends FileConfiguration {
    private final Map<String, String> env = System.getenv();
    private final Properties properties = System.getProperties();

    @Override
    public void load(@NotNull File file) throws IOException {
    }

    @Override
    public void load(@NotNull String content) {
    }

    @Override
    public void load(@NotNull InputStream inputStream) throws IOException {
    }

    public void loadEnvOnly() {
        if (this.contents == null) this.contents = new HashMap<>();
        this.contents.putAll(this.env);
    }

    public void loadSystemPropertiesOnly() {
        if (this.contents == null) this.contents = new HashMap<>();
        this.properties.stringPropertyNames().forEach(f -> this.contents.put(f, this.properties.getProperty(f)));
    }

    public void loadAll() {
        this.loadEnvOnly();
        this.loadSystemPropertiesOnly();
    }

    @Override
    public void save() {
    }

    @Override
    public void save(@NotNull File file) {
    }

    @Override
    public String dump() {
        if (this.contents == null || this.contents.isEmpty()) return "";
        StringBuilder stringBuilder = new StringBuilder();
        this.contents.forEach((key, value) -> stringBuilder.append(key).append("=").append(value).append("\n"));
        return stringBuilder.toString();
    }
}