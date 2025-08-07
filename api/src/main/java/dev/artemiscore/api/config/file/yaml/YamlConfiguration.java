package dev.artemiscore.api.config.file.yaml;

import dev.artemiscore.api.config.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

public class YamlConfiguration extends FileConfiguration {
    private final DumperOptions DUMPER_OPTIONS = new DumperOptions() {{
        this.setIndent(2);
        this.setAllowUnicode(true);
        this.setPrettyFlow(true);
        this.setDefaultFlowStyle(FlowStyle.BLOCK);
    }};
    private final Yaml yaml = new Yaml(this.DUMPER_OPTIONS);
    private File file;

    @Override
    public void load(@NotNull InputStream inputStream) throws IOException {
        this.contents = this.yaml.load(inputStream);
        inputStream.close();
    }

    @Override
    public void load(@NotNull String content) {
        if (content.isEmpty()) throw new IllegalArgumentException("Content cannot be null or empty");
        this.contents = this.yaml.load(content);
    }

    @Override
    public void load(@NotNull File file) throws IllegalArgumentException, IOException {
        if (!file.exists() || !file.canRead()) throw new IllegalArgumentException("File does not exist or is not readable: %s".formatted(file.getAbsolutePath()));
        try (InputStream inputStream = new FileInputStream(file)) {
            this.load(inputStream);
            this.file = file; // Store the file reference for saving later
        }
    }

    @Override
    public void save() {
        if (this.file == null) throw new IllegalStateException("Configuration is not associated with a file.");
        this.save(this.file);
    }

    @Override
    public void save(@NotNull File file) {
        if (this.contents == null || this.contents.isEmpty()) throw new IllegalStateException("No content to save.");
        try (Writer writer = new FileWriter(file)) {
            this.yaml.dump(this.contents, writer);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to save configuration to file: %s".formatted(file.getAbsolutePath()), exception);
        }
    }

    @Override
    public String dump() {
        if (this.contents == null || this.contents.isEmpty()) return "";
        try (StringWriter stringWriter = new StringWriter()) {
            this.yaml.dump(this.contents, stringWriter);
            return stringWriter.toString();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to dump configuration content.", exception);
        }
    }
}
