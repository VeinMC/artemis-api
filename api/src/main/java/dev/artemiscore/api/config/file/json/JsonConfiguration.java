package dev.artemiscore.api.config.file.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import dev.artemiscore.api.config.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonConfiguration extends FileConfiguration {
    private final Gson gson = new Gson().newBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private File file;

    @Override
    public void load(@NotNull InputStream inputStream) throws IOException {
        try (inputStream;
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
             JsonReader jsonReader = new JsonReader(bufferedReader)
        ) {
            jsonReader.setLenient(true);
            JsonElement jsonElement = Streams.parse(jsonReader);
            if (jsonElement.isJsonObject()) {
                //noinspection unchecked
                this.contents = (Map<String, Object>) this.gson.fromJson(jsonElement, Object.class);
            } else throw new IOException("Invalid JSON format: Expected a JSON object.");
        }
    }

    @Override
    public void load(@NotNull String content) {
        JsonElement jsonElement = this.gson.fromJson(content, JsonElement.class);
        if (jsonElement.isJsonObject()) {
            //noinspection unchecked
            this.contents = (Map<String, Object>) this.gson.fromJson(jsonElement, Object.class);
        } else throw new IllegalArgumentException("Content is not a valid JSON object.");
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
        if (this.contents == null) throw new IllegalStateException("No content to save.");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            this.gson.toJson(this.contents, writer);
            this.file = file; // Store the file reference for future saves
        } catch (IOException exception) {
            throw new RuntimeException("Failed to save JSON configuration to file: %s".formatted(file.getAbsolutePath()), exception);
        }
    }

    @Override
    public String dump() {
        if (this.contents == null || this.contents.isEmpty()) return "{}"; // Return an empty JSON object if contents are null or empty
        return this.gson.toJson(this.contents);
    }
}
