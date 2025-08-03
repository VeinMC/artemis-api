package dev.artemiscore.api.config;

import dev.artemiscore.api.config.file.hocon.HoconConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class HoconConfigurationTest {
    private HoconConfiguration hoconConfiguration;
    private final File testFile = new File("test.conf");

    @Before
    public void setUp() {
        this.hoconConfiguration = new HoconConfiguration();
    }

    @After
    public void tearDown() {
        this.hoconConfiguration = null;
    }

    @Test
    public void loadFromFile() {
        try {
            this.hoconConfiguration.load(this.testFile);
            assert this.hoconConfiguration.getContents() != null : "Contents should not be null after loading";
            System.out.println(this.hoconConfiguration.getContents());
        } catch (Exception exception) {
            //noinspection CallToPrintStackTrace
            exception.printStackTrace();
            assert false : "Loading from file should not throw an exception";
        }
    }

    @Test
    public void dump() {
        String dumpedContent = this.hoconConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null";
        assert dumpedContent.isEmpty() || dumpedContent.contains("=") : "Dumped content should look like HOCON";

        this.hoconConfiguration.set("string", "Hello World");
        this.hoconConfiguration.set("int", 42);
        this.hoconConfiguration.set("float", 3.14f);
        this.hoconConfiguration.set("double", 2.71828);
        this.hoconConfiguration.set("boolean", true);
        this.hoconConfiguration.set("long", 1234567890123L);
        this.hoconConfiguration.set("stringList", List.of("one", "two", "three"));
        this.hoconConfiguration.set("intList", List.of(1, 2, 3));
        this.hoconConfiguration.set("mixedList", List.of("a", 1, true));
        this.hoconConfiguration.set("map", Map.of(
                "string", "value",
                "int", 99
        ));

        dumpedContent = this.hoconConfiguration.dump();
        assert dumpedContent != null : "Dumped content should not be null after setting values";
        System.out.println(dumpedContent);

        this.hoconConfiguration.save(this.testFile);
    }
}