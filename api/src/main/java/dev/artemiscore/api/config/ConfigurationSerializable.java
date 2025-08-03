package dev.artemiscore.api.config;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ConfigurationSerializable {
    default @NotNull Map<String, Object> serialize() {
        return Map.of();
    }

    default void deserialize(@NotNull Map<String, Object> serialized) {
    }
}
