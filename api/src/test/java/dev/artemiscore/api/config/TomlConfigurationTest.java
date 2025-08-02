package dev.artemiscore.api.config;

import org.junit.Test;

import java.io.File;

public class TomlConfigurationTest {
    private TomlConfiguration configuration;
    private final File testFile = new File("test.toml");

    @org.junit.Before
    public void setUp() {
        configuration = new TomlConfiguration();
    }

    @org.junit.After
    public void tearDown() {
        configuration = null;
    }

    @Test
    public void loadFromFile() {
        try {
            configuration.load(testFile);
            assert configuration.getContents() != null : "Contents should not be null after loading";
            System.out.println(configuration.getContents());
        } catch (Exception exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
            assert false : "Loading from file should not throw an exception";
        }
    }

    @Test
    public void dump() {
        String dumpedContent = configuration.dump();
        assert dumpedContent != null : "Dumped content should not be null";
        assert dumpedContent.isEmpty() : "Dumped content should be empty for a new configuration";

        configuration.set("test.key", "testValue");

        dumpedContent = configuration.dump();
        assert dumpedContent != null : "Dumped content should not be null after setting a value";
        System.out.println(dumpedContent);

        configuration.save(testFile);
    }
}