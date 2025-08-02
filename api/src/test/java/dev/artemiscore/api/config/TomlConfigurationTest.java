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
        tomlConfiguration = new TomlConfiguration();
    }

    @org.junit.After
    public void tearDown() {
        tomlConfiguration = null;
    }

    @Test
    public void loadFromFile() {
        try {
            tomlConfiguration.load(testFile);
            assert tomlConfiguration.getContents() != null : "Contents should not be null after loading";
            System.out.println(tomlConfiguration.getContents());
        } catch (Exception exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
            assert false : "Loading from file should not throw an exception";
        }
    }

    @Test
    public void dump() {
        String dumpedContent = tomlConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null";
        assert dumpedContent.isEmpty() : "Dumped content should be empty for a new configuration";

        tomlConfiguration.set("string", "Hello World");
        tomlConfiguration.set("int", 42);
        tomlConfiguration.set("float", 3.14f);
        tomlConfiguration.set("double", 2.71828);
        tomlConfiguration.set("boolean", true);
        tomlConfiguration.set("long", 1234567890123L);
        tomlConfiguration.set("list", List.of("one", "two", "three"));
        tomlConfiguration.set("intList", List.of(1, 2, 3));
        tomlConfiguration.set("mixedList", List.of("a", 1, true));
        tomlConfiguration.set("map", Map.of(
                "string", "value",
                "int", 99
        ));

        dumpedContent = tomlConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null after setting a value";
        System.out.println(dumpedContent);

        tomlConfiguration.save(testFile);
    }
}