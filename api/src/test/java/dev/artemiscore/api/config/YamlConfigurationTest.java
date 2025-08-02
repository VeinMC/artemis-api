package dev.artemiscore.api.config;

import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class YamlConfigurationTest {
    private YamlConfiguration yamlConfiguration;
    private final File testFile = new File("test.yml");

    @org.junit.Before
    public void setUp() {
        yamlConfiguration = new YamlConfiguration();
    }

    @org.junit.After
    public void tearDown() {
        yamlConfiguration = null;
    }

    @Test
    public void loadFromFile() {
        // Test loading from a file
        try {
            yamlConfiguration.load(testFile);
            assert yamlConfiguration.getContents() != null : "Contents should not be null after loading";
            System.out.println(yamlConfiguration.contents);
        } catch (Exception exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
            assert false : "Loading from file should not throw an exception";
        }
    }

    @Test
    public void dump() {
        // Test the dump method
        String dumpedContent = yamlConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null";
        assert dumpedContent.isEmpty() : "Dumped content should be empty for a new configuration";

        yamlConfiguration.set("string", "Hello World");
        yamlConfiguration.set("int", 42);
        yamlConfiguration.set("float", 3.14f);
        yamlConfiguration.set("double", 2.71828);
        yamlConfiguration.set("boolean", true);
        yamlConfiguration.set("long", 1234567890123L);
        yamlConfiguration.set("stringList", List.of("one", "two", "three"));
        yamlConfiguration.set("intList", List.of(1, 2, 3));
        yamlConfiguration.set("mixedList", List.of("a", 1, true));
        yamlConfiguration.set("map", Map.of(
                "string", "value",
                "int", 99
        ));

        dumpedContent = yamlConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null after setting a value";
        System.out.println(dumpedContent);

        yamlConfiguration.save(testFile);
    }
}