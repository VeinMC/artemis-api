package dev.artemiscore.api.config.file.env.interfaces;

public interface Env {
    void loadEnvOnly();

    void loadSystemPropertiesOnly();

    void loadAll();
}