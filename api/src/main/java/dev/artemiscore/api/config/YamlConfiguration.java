package dev.artemiscore.api.config;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

public class YamlConfiguration extends FileConfiguration {
    private static final DumperOptions DUMPER_OPTIONS = new DumperOptions();

    static {
        DUMPER_OPTIONS.setIndent(2);
        DUMPER_OPTIONS.setAllowUnicode(true);
        DUMPER_OPTIONS.setPrettyFlow(true);
        DUMPER_OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    }

    private final Yaml yaml = new Yaml(DUMPER_OPTIONS);
    private File file;

    @Override
    public void load(@NotNull InputStream inputStream) throws IOException {
        contents = yaml.load(inputStream);
        inputStream.close();
    }

    @Override
    public void load(@NotNull String content) {
        if (content.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        contents = yaml.load(content);
    }

    @Override
    public void load(@NotNull File file) throws IllegalArgumentException, IOException {
        if (!file.exists() || !file.canRead()) {
            throw new IllegalArgumentException("File does not exist or is not readable: %s".formatted(file.getAbsolutePath()));
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            load(inputStream);
            this.file = file; // Store the file reference for saving later
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
        try (Writer writer = new FileWriter(file)) {
            yaml.dump(contents, writer);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to save configuration to file: %s".formatted(file.getAbsolutePath()), exception);
        }
    }

    @Override
    public String dump() {
        if (contents == null || contents.isEmpty()) {
            return "";
        }
        try (StringWriter writer = new StringWriter()) {
            yaml.dump(contents, writer);
            return writer.toString();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to dump configuration content.", exception);
        }
    }
}
