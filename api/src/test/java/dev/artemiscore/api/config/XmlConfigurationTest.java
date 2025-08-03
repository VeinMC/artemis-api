package dev.artemiscore.api.config;

import dev.artemiscore.api.config.file.xml.XmlConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class XmlConfigurationTest {
    private XmlConfiguration xmlConfiguration;
    private final File testFile = new File("test.xml");

    @Before
    public void setUp() {
        this.xmlConfiguration = new XmlConfiguration();
    }

    @After
    public void tearDown() {
        this.xmlConfiguration = null;
    }

    @Test
    public void loadFromFile() {
        try {
            this.xmlConfiguration.load(this.testFile);
            assert this.xmlConfiguration.getContents() != null : "Contents should not be null after loading";
            System.out.println(this.xmlConfiguration.getContents());
        } catch (Exception exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
            assert false : "Loading from file should not throw an exception";
        }
    }

    @Test
    public void dump() {
        String dumpedContent = this.xmlConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null";
        assert dumpedContent.contains("<?xml") || dumpedContent.isEmpty() : "Should produce valid or empty XML";

        this.xmlConfiguration.set("string", "Hello World");
        this.xmlConfiguration.set("int", 42);
        this.xmlConfiguration.set("float", 3.14f);
        this.xmlConfiguration.set("double", 2.71828);
        this.xmlConfiguration.set("boolean", true);
        this.xmlConfiguration.set("long", 1234567890123L);
        this.xmlConfiguration.set("stringList", List.of("one", "two", "three"));
        this.xmlConfiguration.set("intList", List.of(1, 2, 3));
        this.xmlConfiguration.set("mixedList", List.of("a", 1, true));
        this.xmlConfiguration.set("map", Map.of(
                "string", "value",
                "int", 99
        ));

        dumpedContent = this.xmlConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null after setting values";
        System.out.println(dumpedContent);

        this.xmlConfiguration.save(this.testFile);
    }
}