package dev.artemiscore.api.config;

import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JsonConfigurationTest {
    private JsonConfiguration jsonConfiguration;
    private final File testFile = new File("test.json");

    @org.junit.Before
    public void setUp() {
        jsonConfiguration = new JsonConfiguration();
    }

    @org.junit.After
    public void tearDown() {
        jsonConfiguration = null;
    }

    @Test
    public void loadFromFile() {
        // Test loading from a file
        try {
            jsonConfiguration.load(testFile);
            assert jsonConfiguration.getContents() != null : "Contents should not be null after loading";
            System.out.println(jsonConfiguration.contents);
        } catch (Exception exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
            assert false : "Loading from file should not throw an exception";
        }
    }

    @Test
    public void dump() {
        // Test the dump method
        String dumpedContent = jsonConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null";
        assert dumpedContent.equalsIgnoreCase("{}") : "Dumped content should be empty for a new configuration";

        jsonConfiguration.set("string", "Hello World");
        jsonConfiguration.set("int", 42);
        jsonConfiguration.set("float", 3.14f);
        jsonConfiguration.set("double", 2.71828);
        jsonConfiguration.set("boolean", true);
        jsonConfiguration.set("long", 1234567890123L);
        jsonConfiguration.set("stringList", List.of("one", "two", "three"));
        jsonConfiguration.set("intList", List.of(1, 2, 3));
        jsonConfiguration.set("mixedList", List.of("a", 1, true));
        jsonConfiguration.set("map", Map.of(
                "string", "value",
                "int", 99
        ));

        dumpedContent = jsonConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null after setting a value";
        System.out.println(dumpedContent);

        jsonConfiguration.save(testFile);
    }
}