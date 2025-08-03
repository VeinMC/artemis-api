package dev.artemiscore.api.config.memory;

import dev.artemiscore.api.config.Configuration;
import dev.artemiscore.api.config.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MemoryConfiguration implements Configuration {
    public static final String REGEX_SEPARATOR = "\\.";
    public static final String SEPARATOR = ".";
    protected Map<String, Object> defaults;
    protected Map<String, Object> contents;

    @Override
    public void addDefault(@NotNull String key, @NotNull Object value) {
        if (this.defaults == null) this.defaults = new LinkedHashMap<>();

        if (value instanceof ConfigurationSerializable) value = ((ConfigurationSerializable) value).serialize(); // Serialize if the value is Serializable

        this.defaults.put(key, value);
    }

    @Override
    public void addDefaults(@NotNull Map<String, Object> map) {
        if (this.defaults == null) this.defaults = new LinkedHashMap<>();
        this.defaults.putAll(map);
    }

    @Override
    public void saveDefaults() {
        if (this.defaults == null || this.defaults.isEmpty()) return; // No defaults to save

        if (this.contents == null) this.contents = new HashMap<>();

        this.defaults.forEach(this::setIfAbsent);
    }

    @Override
    public boolean contains(@NotNull String key) {
        return this.get(key) != null;
    }

    @Override
    public @Nullable Object get(@NotNull String key) {
        if (this.contents == null || key.isEmpty()) return null; // No contents loaded
        return this
                .getParent(key, false)
                .get(key.substring(key.lastIndexOf(SEPARATOR) + 1).toLowerCase());
    }

    @Override
    public Object get(@NotNull String key, @NotNull Object defaultValue) {
        Object value = this.get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public void set(@NotNull String key, @NotNull Object value) {
        Map<String, Object> parent = this.getParent(key, true);

        if (parent == null) return;

        if (value instanceof ConfigurationSerializable) value = ((ConfigurationSerializable) value).serialize(); // Serialize if the value is Serializable

        parent.put(key.substring(key.lastIndexOf(SEPARATOR) + 1).toLowerCase(), value); // Set the final value
    }

    @Override
    public void set(@NotNull Map<String, Object> map) {
        map.forEach(this::set);
    }

    public void setIfAbsent(String key, Object value) {
        if (key.isEmpty() || this.contains(key)) return;
        this.set(key, value);
    }

    @Override
    public boolean remove(@NotNull String key) {
        if (this.contents == null || key.isEmpty()) return false; // No contents loaded
        return this
                .getParent(key, false)
                .remove(key.substring(key.lastIndexOf(SEPARATOR) + 1).toLowerCase()) != null;
    }

    public Map<String, Object> getParent(String key, boolean createIfAbsent) {
        if (key.isEmpty()) return null;
        if (this.contents == null) this.contents = new LinkedHashMap<>();
        String[] parts = key.toLowerCase().split(REGEX_SEPARATOR);
        if (parts.length == 0) return this.contents;
        Map<String, Object> currentMap = contents;
        for (int index = 0; index < parts.length - 1; index++) {
            String part = parts[index];
            Object value = currentMap.get(part);
            if (value instanceof Map) {
                //noinspection unchecked
                currentMap = (Map<String, Object>) value;
            } else if (createIfAbsent) {
                Map<String, Object> newMap = new LinkedHashMap<>();
                currentMap.put(part, newMap);
                currentMap = newMap;
            } else return null;
        }
        return currentMap;
    }

    @Override
    public Map<String, Object> getContents() {
        if (this.contents == null) throw new IllegalStateException("Configuration contents are not loaded.");
        return Collections.unmodifiableMap(this.contents);
    }

    @Override
    public Map<String, Object> getDefaults() {
        if (this.defaults == null) return Collections.emptyMap(); // Return an empty map if no defaults are set
        return Collections.unmodifiableMap(this.defaults);
    }
}