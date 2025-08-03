package dev.artemiscore.api.config;

import dev.artemiscore.api.config.file.json.JsonConfiguration;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JsonConfigurationTest {
    private JsonConfiguration jsonConfiguration;
    private final File testFile = new File("test.json");

    @org.junit.Before
    public void setUp() {
        this.jsonConfiguration = new JsonConfiguration();
    }

    @org.junit.After
    public void tearDown() {
        this.jsonConfiguration = null;
    }

    @Test
    public void loadFromFile() {
        // Test loading from a file
        try {
            this.jsonConfiguration.load(this.testFile);
            assert this.jsonConfiguration.getContents() != null : "Contents should not be null after loading";
            System.out.println(this.jsonConfiguration.getContents());
        } catch (Exception exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
            assert false : "Loading from file should not throw an exception";
        }
    }

    @Test
    public void dump() {
        // Test the dump method
        String dumpedContent = this.jsonConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null";
        assert dumpedContent.equalsIgnoreCase("{}") : "Dumped content should be empty for a new configuration";

        this.jsonConfiguration.set("string", "Hello World");
        this.jsonConfiguration.set("int", 42);
        this.jsonConfiguration.set("float", 3.14f);
        this.jsonConfiguration.set("double", 2.71828);
        this.jsonConfiguration.set("boolean", true);
        this.jsonConfiguration.set("long", 1234567890123L);
        this.jsonConfiguration.set("stringList", List.of("one", "two", "three"));
        this.jsonConfiguration.set("intList", List.of(1, 2, 3));
        this.jsonConfiguration.set("mixedList", List.of("a", 1, true));
        this.jsonConfiguration.set("map", Map.of(
                "string", "value",
                "int", 99
        ));

        dumpedContent = this.jsonConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null after setting a value";
        System.out.println(dumpedContent);

        this.jsonConfiguration.save(this.testFile);
    }
}