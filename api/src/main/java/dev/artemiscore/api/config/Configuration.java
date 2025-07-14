package dev.artemiscore.api.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface Configuration {

    /**
     * Adds a default value to the configuration.
     *
     * @param key   the key for the default value
     * @param value the default value to be added
     */
    void addDefault(@NotNull String key, @NotNull Object value);

    /**
     * Adds a default value to the configuration.
     *
     * @param map a Map containing key-value pairs to be added as defaults
     */
    void addDefaults(@NotNull Map<String, Object> map);

    /**
     * Saves the default values to the configuration file.
     * This method should be called to ensure that default values are written to the file.
     */
    void saveDefaults();

    /**
     * Checks if the configuration contains a specific key.
     *
     * @param key the key to check for
     * @return true if the key exists in the configuration, false otherwise
     */
    boolean contains(@NotNull String key);

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key the key whose value is to be retrieved
     * @return the value associated with the key, or null if the key does not exist
     */
    @Nullable Object get(@NotNull String key);

    /**
     * Retrieves the value associated with the specified key, or returns a default value if the key does not exist.
     *
     * @param key          the key whose value is to be retrieved
     * @param defaultValue the default value to return if the key does not exist
     * @return the value associated with the key, or the default value if the key does not exist
     */
    Object get(@NotNull String key, @NotNull Object defaultValue);

    /**
     * Sets a value for the specified key in the configuration.
     *
     * @param key   the key for which the value is to be set
     * @param value the value to be set for the key
     */
    void set(@NotNull String key, @NotNull Object value);

    /**
     * Sets multiple key-value pairs in the configuration.
     *
     * @param map a Map containing key-value pairs to be set in the configuration
     */
    void set(@NotNull Map<String, Object> map);

    /**
     * Removes the specified key and its associated value from the configuration.
     *
     * @param key the key to be removed
     * @return true if the key was successfully removed, false if the key did not exist
     */
    boolean remove(@NotNull String key);

    /**
     * @return a Map containing the contents of the configuration.
     * Throws IllegalStateException if the configuration is not loaded.
     */
    Map<String, Object> getContents() throws IllegalStateException;

    /**
     * @return a Map containing the default values of the configuration.
     * This method should be used to retrieve the defaults that were set using addDefault or addDefaults.
     */
    Map<String, Object> getDefaults();
}
