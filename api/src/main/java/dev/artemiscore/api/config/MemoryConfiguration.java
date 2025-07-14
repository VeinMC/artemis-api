package dev.artemiscore.api.config;

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
        if (defaults == null) {
            defaults = new LinkedHashMap<>();
        }

        if (value instanceof Serializable) {
            value = ((Serializable) value).serialize(); // Serialize if the value is Serializable
        }

        defaults.put(key, value);
    }

    @Override
    public void addDefaults(@NotNull Map<String, Object> map) {
        if (defaults == null) {
            defaults = new LinkedHashMap<>();
        }
        defaults.putAll(map);
    }

    @Override
    public void saveDefaults() {
        if (defaults == null || defaults.isEmpty()) {
            return; // No defaults to save
        }

        if (contents == null) {
            contents = new HashMap<>();
        }

        for (Map.Entry<String, Object> entry : defaults.entrySet()) {
            setIfAbsent(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean contains(@NotNull String key) {
        return get(key) != null;
    }

    @Override
    public @Nullable Object get(@NotNull String key) {
        if (contents == null || key.isEmpty()) {
            return null; // No contents loaded
        }
        Map<String, Object> parent = getParent(key, false);
        return parent.get(key.substring(key.lastIndexOf(SEPARATOR) + 1).toLowerCase());
    }

    @Override
    public Object get(@NotNull String key, @NotNull Object defaultValue) {
        Object value = get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public void set(@NotNull String key, @NotNull Object value) {
        Map<String, Object> parent = getParent(key, true);

        if (parent == null) {
            return;
        }

        if (value instanceof Serializable) {
            value = ((Serializable) value).serialize(); // Serialize if the value is Serializable
        }

        parent.put(key.substring(key.lastIndexOf(SEPARATOR) + 1).toLowerCase(), value); // Set the final value
    }

    @Override
    public void set(@NotNull Map<String, Object> map) {
        map.forEach(this::set);
    }

    private void setIfAbsent(String key, Object value) {
        if (key.isEmpty() || contains(key)) return;
        set(key, value);
    }

    @Override
    public boolean remove(@NotNull String key) {
        if (contents == null || key.isEmpty()) {
            return false; // No contents loaded
        }
        Map<String, Object> parent = getParent(key, false);
        return parent.remove(key.substring(key.lastIndexOf(SEPARATOR) + 1).toLowerCase()) != null;
    }

    public Map<String, Object> getParent(String key, boolean createIfAbsent) {
        if (key.isEmpty()) return null;

        if (contents == null) {
            contents = new LinkedHashMap<>();
        }

        String[] parts = key.toLowerCase().split(REGEX_SEPARATOR);

        if (parts.length == 0) {
            return contents;
        }

        Map<String, Object> parent = contents;


        String part;
        for (int i = 0; i < parts.length - 1; i++) {
            part = parts[i];
            if (!parent.containsKey(part)) {
                if (!createIfAbsent) {
                    return null; // Return null if the part doesn't exist and createIfAbsent is false
                }
                Map<String, Object> map = new LinkedHashMap<>();
                parent.put(part, map); // Create a new map if the part doesn't exist
                parent = map;
                continue;
            }

            Object child = parent.get(part);
            if (!(child instanceof Map)) {
                if (!createIfAbsent) {
                    return null; // Return null if the part doesn't exist and createIfAbsent is false
                }
                Map<String, Object> map = new LinkedHashMap<>();
                parent.put(part, map); // Replace non-map value with a new map
                parent = map;
                continue;
            }

            //noinspection unchecked
            parent = (Map<String, Object>) child; // Move to the next level
        }

        return parent; // Return the parent map for the last part
    }

    @Override
    public Map<String, Object> getContents() {
        if (contents == null) {
            throw new IllegalStateException("Configuration contents are not loaded.");
        }
        return Collections.unmodifiableMap(contents);
    }

    @Override
    public Map<String, Object> getDefaults() {
        if (defaults == null) {
            return Collections.emptyMap(); // Return an empty map if no defaults are set
        }
        return Collections.unmodifiableMap(defaults);
    }
}
