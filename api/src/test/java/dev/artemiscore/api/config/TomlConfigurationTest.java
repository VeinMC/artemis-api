package dev.artemiscore.api.config;

import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TomlConfigurationTest {
    private TomlConfiguration tomlConfiguration;
    private final File testFile = new File("test.toml");

    @org.junit.Before
    public void setUp() {
        this.tomlConfiguration = new TomlConfiguration();
    }

    @org.junit.After
    public void tearDown() {
        this.tomlConfiguration = null;
    }

    @Test
    public void loadFromFile() {
        try {
            this.tomlConfiguration.load(testFile);
            assert this.tomlConfiguration.getContents() != null : "Contents should not be null after loading";
            System.out.println(this.tomlConfiguration.getContents());
        } catch (Exception exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
            assert false : "Loading from file should not throw an exception";
        }
    }

    @Test
    public void dump() {
        String dumpedContent = this.tomlConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null";
        assert dumpedContent.isEmpty() : "Dumped content should be empty for a new configuration";

        this.tomlConfiguration.set("string", "Hello World");
        this.tomlConfiguration.set("int", 42);
        this.tomlConfiguration.set("float", 3.14f);
        this.tomlConfiguration.set("double", 2.71828);
        this.tomlConfiguration.set("boolean", true);
        this.tomlConfiguration.set("long", 1234567890123L);
        this.tomlConfiguration.set("stringList", List.of("one", "two", "three"));
        this.tomlConfiguration.set("intList", List.of(1, 2, 3));
        this.tomlConfiguration.set("mixedList", List.of("a", 1, true));
        this.tomlConfiguration.set("map", Map.of(
                "string", "value",
                "int", 99
        ));

        dumpedContent = this.tomlConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null after setting a value";
        System.out.println(dumpedContent);

        this.tomlConfiguration.save(this.testFile);
    }
}