package dev.artemiscore.api.config;

import dev.artemiscore.api.config.file.properties.PropertiesConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class PropertiesConfigurationTest {
    private PropertiesConfiguration propertiesConfiguration;
    private final File testFile = new File("test.properties");

    @Before
    public void setUp() {
        this.propertiesConfiguration = new PropertiesConfiguration();
    }

    @After
    public void tearDown() {
        this.propertiesConfiguration = null;
    }

    @Test
    public void loadFromFile() {
        try {
            this.propertiesConfiguration.load(this.testFile);
            assert this.propertiesConfiguration.getContents() != null : "Contents should not be null after loading";
            System.out.println(this.propertiesConfiguration.getContents());
        } catch (Exception exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
            assert false : "Loading from file should not throw an exception";
        }
    }

    @Test
    public void dump() {
        String dumpedContent = this.propertiesConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null";
        assert dumpedContent.isEmpty() || dumpedContent.contains("=") : "Dump should contain properties";

        this.propertiesConfiguration.set("string", "Hello World");
        this.propertiesConfiguration.set("int", 42);
        this.propertiesConfiguration.set("float", 3.14f);
        this.propertiesConfiguration.set("double", 2.71828);
        this.propertiesConfiguration.set("boolean", true);
        this.propertiesConfiguration.set("long", 1234567890123L);
        this.propertiesConfiguration.set("stringList", List.of("one", "two", "three"));
        this.propertiesConfiguration.set("intList", List.of(1, 2, 3));
        this.propertiesConfiguration.set("mixedList", List.of("a", 1, true));
        this.propertiesConfiguration.set("map", Map.of(
                "string", "value",
                "int", 99
        ));

        dumpedContent = this.propertiesConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null after setting values";
        System.out.println(dumpedContent);

        this.propertiesConfiguration.save(this.testFile);
    }
}