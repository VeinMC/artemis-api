package dev.artemiscore.api.config;

import dev.artemiscore.api.config.file.yaml.YamlConfiguration;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class YamlConfigurationTest {
    private YamlConfiguration yamlConfiguration;
    private final File testFile = new File("test.yml");

    @org.junit.Before
    public void setUp() {
        this.yamlConfiguration = new YamlConfiguration();
    }

    @org.junit.After
    public void tearDown() {
        this.yamlConfiguration = null;
    }

    @Test
    public void loadFromFile() {
        // Test loading from a file
        try {
            this.yamlConfiguration.load(this.testFile);
            assert this.yamlConfiguration.getContents() != null : "Contents should not be null after loading";
            System.out.println(this.yamlConfiguration.getContents());
        } catch (Exception exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
            assert false : "Loading from file should not throw an exception";
        }
    }

    @Test
    public void dump() {
        // Test the dump method
        String dumpedContent = this.yamlConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null";
        assert dumpedContent.isEmpty() : "Dumped content should be empty for a new configuration";

        this.yamlConfiguration.set("string", "Hello World");
        this.yamlConfiguration.set("int", 42);
        this.yamlConfiguration.set("float", 3.14f);
        this.yamlConfiguration.set("double", 2.71828);
        this.yamlConfiguration.set("boolean", true);
        this.yamlConfiguration.set("long", 1234567890123L);
        this.yamlConfiguration.set("stringList", List.of("one", "two", "three"));
        this.yamlConfiguration.set("intList", List.of(1, 2, 3));
        this.yamlConfiguration.set("mixedList", List.of("a", 1, true));
        this.yamlConfiguration.set("map", Map.of(
                "string", "value",
                "int", 99
        ));

        dumpedContent = this.yamlConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null after setting a value";
        System.out.println(dumpedContent);

        this.yamlConfiguration.save(this.testFile);
    }
}