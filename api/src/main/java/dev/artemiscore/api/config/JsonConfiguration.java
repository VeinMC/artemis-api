package dev.artemiscore.api.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonConfiguration extends FileConfiguration {
    private static final Gson gson = new Gson().newBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private File file;

    @Override
    public void load(@NotNull InputStream inputStream) throws IOException {
        try (inputStream;
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
             JsonReader reader = new JsonReader(bufferedReader)
        ) {
            reader.setLenient(true);
            JsonElement element = Streams.parse(reader);
            if (element.isJsonObject()) {
                //noinspection unchecked
                contents = (Map<String, Object>) gson.fromJson(element, Object.class);
            } else {
                throw new IOException("Invalid JSON format: Expected a JSON object.");
            }
        }
    }

    @Override
    public void load(@NotNull String content) {
        JsonElement element = gson.fromJson(content, JsonElement.class);
        if (element.isJsonObject()) {
            //noinspection unchecked
            contents = (Map<String, Object>) gson.fromJson(element, Object.class);
        } else {
            throw new IllegalArgumentException("Content is not a valid JSON object.");
        }
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
        if (contents == null) {
            throw new IllegalStateException("No content to save.");
        }

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            gson.toJson(contents, writer);
            this.file = file; // Store the file reference for future saves
        } catch (IOException exception) {
            throw new RuntimeException("Failed to save JSON configuration to file: %s".formatted(file.getAbsolutePath()), exception);
        }

    }

    @Override
    public String dump() {
        if (contents == null || contents.isEmpty()) {
            return "{}"; // Return an empty JSON object if contents are null or empty
        }
        return gson.toJson(contents);
    }
}
