package dev.artemiscore.api.config.file.hocon;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import dev.artemiscore.api.config.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class HoconConfiguration extends FileConfiguration {
    private File file;

    @Override
    public void load(@NotNull InputStream inputStream) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            this.contents = ConfigFactory.parseReader(inputStreamReader).resolve().root().unwrapped();
        }
    }

    @Override
    public void load(@NotNull String content) {
        this.contents = ConfigFactory.parseString(content).resolve().root().unwrapped();
    }

    @Override
    public void load(@NotNull File file) throws IOException {
        if (!file.exists() || !file.canRead()) throw new IllegalArgumentException("File does not exist or is not readable: %s".formatted(file.getAbsolutePath()));
        this.file = file;
        this.contents = ConfigFactory.parseFile(file).resolve().root().unwrapped();
    }

    @Override
    public void save() {
        if (this.file == null) throw new IllegalStateException("No file associated with configuration");
        this.save(this.file);
    }

    @Override
    public void save(@NotNull File file) {
        if (this.contents == null || this.contents.isEmpty()) throw new IllegalStateException("No content to save.");
        String rendered = ConfigFactory.parseMap(this.contents).root().render(ConfigRenderOptions.defaults()
                .setComments(false)
                .setOriginComments(false)
                .setFormatted(true));
        try (Writer writer = new FileWriter(file)) {
            writer.write(rendered);
            this.file = file;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to save HOCON configuration", exception);
        }
    }

    @Override
    public String dump() {
        if (this.contents == null || this.contents.isEmpty()) return "";
        return ConfigFactory.parseMap(this.contents).root().render(ConfigRenderOptions.defaults()
                .setComments(false)
                .setOriginComments(false)
                .setFormatted(true));
    }
}