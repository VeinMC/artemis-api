package dev.artemiscore.api.config;

import org.jetbrains.annotations.NotNull;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class TomlConfiguration extends FileConfiguration {
    private File file;

    @Override
    public void load(@NotNull InputStream inputStream) throws IOException {
        String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        load(content);
    }

    @Override
    public void load(@NotNull String content) {
        TomlParseResult tomlParseResult = Toml.parse(content);

        if (tomlParseResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid TOML content: %s".formatted(tomlParseResult.errors()));
        }

        contents = convertTomlTable(tomlParseResult);
    }

    @Override
    public void load(@NotNull File file) throws IllegalArgumentException, IOException {
        if (!file.exists() || !file.canRead()) {
            throw new IllegalArgumentException("File does not exist or is not readable: %s".formatted(file.getAbsolutePath()));
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            load(inputStream);
            this.file = file;
        }
    }

    @Override
    public void save() {
        if (file == null) {
            throw new IllegalStateException("Configuration is not associated with a file.");
        }
        save(file);
    }

    @Override
    public void save(@NotNull File file) {
        if (contents == null || contents.isEmpty()) {
            throw new IllegalStateException("No content to save.");
        }

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            writer.write(dump());
            this.file = file;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to save TOML configuration to file: %s".formatted(file.getAbsolutePath()), exception);
        }
    }

    @Override
    String dump() {
        if (contents == null || contents.isEmpty()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        dumpTomlMap(contents, "", stringBuilder);
        return stringBuilder.toString();
    }

    private Map<String, Object> convertTomlTable(TomlTable table) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (String key : table.keySet()) {
            Object value = table.get(key);
            if (value instanceof TomlTable nested) {
                map.put(key, convertTomlTable(nested));
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    private void dumpTomlMap(Map<String, Object> map, String prefix, StringBuilder builder) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map<?, ?> nested) {
                builder.append("[").append(prefix).append(key).append("]\n");
                //noinspection unchecked
                dumpTomlMap((Map<String, Object>) nested, "%s%s.".formatted(prefix, key), builder);
            } else {
                builder.append(key).append(" = ").append(formatTomlValue(value)).append("\n");
            }
        }
    }

    private String formatTomlValue(Object value) {
        if (value instanceof String) {
            return "\"" + value.toString().replace("\"", "\\\"") + "\"";
        }
        return value.toString();
    }
}
