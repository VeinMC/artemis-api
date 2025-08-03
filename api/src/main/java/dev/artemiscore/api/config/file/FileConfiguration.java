package dev.artemiscore.api.config.file;

import dev.artemiscore.api.config.memory.MemoryConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class FileConfiguration extends MemoryConfiguration {
    /**
     * Loads a configuration from the given InputStream.
     *
     * @param inputStream the InputStream to read the configuration from
     * @throws IOException if an I/O error occurs while reading the InputStream
     */
    public abstract void load(@NotNull InputStream inputStream) throws IOException;

    /**
     * Loads a configuration from the string
     *
     * @param content the string content of the configuration
     */
    public abstract void load(@NotNull String content);

    /**
     * Loads a configuration from the specified file.
     *
     * @param file the File object representing the configuration file
     * @throws IllegalArgumentException if the file does not exist or is not readable
     * @throws IOException              if an I/O error occurs while reading the file
     */
    public abstract void load(@NotNull File file) throws IllegalArgumentException, IOException;

    /**
     * Saves the current configuration to the specified file path.
     *
     * @throws IllegalStateException if the configuration is not associated with a file.
     */
    public abstract void save();

    /**
     * Saves the current configuration to the specified file.
     *
     * @param file the File object to save the configuration to
     */
    public abstract void save(@NotNull File file);

    /**
     * @return dumps the configuration as a String.
     */
    public abstract String dump();
}
