package dev.artemiscore.api.config;

import dev.artemiscore.api.config.memory.MemoryConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryConfigurationTest {
    private MemoryConfiguration configuration;

    @org.junit.Before
    public void setUp() {
        this.configuration = new MemoryConfiguration();
    }

    @org.junit.After
    public void tearDown() {
        this.configuration = null;
    }

    @Test
    public void addDefault() {
        this.configuration.addDefault("test.key", "defaultValue");

        assert this.configuration.getDefaults().containsKey("test.key") : "Default key should be present";

        assert "defaultValue".equals(this.configuration.getDefaults().get("test.key")) : "Default value should match";
    }

    @Test
    public void addDefaults() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("test.key1", "defaultValue1");
        defaults.put("test.key2", "defaultValue2");

        this.configuration.addDefaults(defaults);

        assert this.configuration.getDefaults().containsKey("test.key1") : "Default key1 should be present";
        assert "defaultValue1".equals(this.configuration.getDefaults().get("test.key1")) : "Default value1 should match";

        assert this.configuration.getDefaults().containsKey("test.key2") : "Default key2 should be present";
        assert "defaultValue2".equals(this.configuration.getDefaults().get("test.key2")) : "Default value2 should match";
    }

    @Test
    public void saveDefaults() {
        this.configuration.addDefault("test.key", "defaultValue");
        this.configuration.addDefault("test.existingKey", "existingValue");
        this.configuration.set("test.existingKey", "existingValue2");
        System.out.printf("before saving: %s%n", this.configuration.getContents());
        this.configuration.saveDefaults();
        System.out.printf("after saving: %s%n", this.configuration.getContents());
        assert this.configuration.contains("test.key") : "Key should be present after saving defaults";
        assert this.configuration.contains("test.existingKey") : "Existing key should still be present after saving defaults";
        assert Objects.equals(this.configuration.get("test.existingKey"), "existingValue2") : "Existing key should retain its value after saving defaults";
    }

    @Test
    public void remove() {
        this.configuration.set("test.existingKey", "existingValue2");
        System.out.printf("Contents before removing: %s%n", this.configuration.getContents());
        this.configuration.remove("test.existingKey");
        System.out.printf("Contents after removing: %s%n", this.configuration.getContents());
        assert !this.configuration.contains("test.existingKey") : "Key should not be present after removal";
    }

    @Test
    public void set() {
        this.configuration.set("test.key", "value");
        System.out.printf("Contents after setting: %s%n", this.configuration.getContents());
        assert this.configuration.contains("test.key") : "Key should be present after setting value";
    }

    @Test
    public void serializeAndDeserialize() {
        TestConfigurationSerializable testSerializable = new TestConfigurationSerializable();
        testSerializable.id = 42;

        this.configuration.set("test.serializable", testSerializable);

        assert this.configuration.contains("test.serializable") : "Key should be present after setting serializable object";

        TestConfigurationSerializable testConfigurationSerializable = new TestConfigurationSerializable();
        //noinspection DataFlowIssue,unchecked
        testConfigurationSerializable.deserialize((Map<String, Object>) this.configuration.get("test.serializable"));
        assert testConfigurationSerializable.id == 42 : "Deserialized id should match the original";
    }

    static class TestConfigurationSerializable implements ConfigurationSerializable {
        private int id;

        @Override
        public @NotNull Map<String, Object> serialize() {
            return new HashMap<>() {{
                put("id", id);
            }};
        }

        @Override
        public void deserialize(@NotNull Map<String, Object> data) {
            if (data.containsKey("id")) this.id = (int) data.get("id");
        }
    }
}