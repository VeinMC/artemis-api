package dev.artemiscore.api.config;

import org.junit.Test;

import java.io.File;

public class YamlConfigurationTest {
    private YamlConfiguration configuration;
    private final File testFile = new File("test.yml");

    @org.junit.Before
    public void setUp() {
        configuration = new YamlConfiguration();
    }

    @org.junit.After
    public void tearDown() {
        configuration = null;
    }

    @Test
    public void loadFromFile() {
        // Test loading from a file
        try {
            configuration.load(testFile);
            assert configuration.getContents() != null : "Contents should not be null after loading";
            System.out.println(configuration.contents);
        } catch (Exception exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
            assert false : "Loading from file should not throw an exception";
        }
    }

    @Test
    public void dump() {
        // Test the dump method
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