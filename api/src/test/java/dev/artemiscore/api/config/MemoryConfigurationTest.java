package dev.artemiscore.api.config;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryConfigurationTest {
    private MemoryConfiguration configuration;

    @org.junit.Before
    public void setUp() throws Exception {
        configuration = new MemoryConfiguration();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        configuration = null;
    }

    @Test
    public void addDefault() {
        configuration.addDefault("test.key", "defaultValue");

        assert configuration.getDefaults().containsKey("test.key") : "Default key should be present";

        assert "defaultValue".equals(configuration.getDefaults().get("test.key")) : "Default value should match";
    }

    @Test
    public void addDefaults() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("test.key1", "defaultValue1");
        defaults.put("test.key2", "defaultValue2");

        configuration.addDefaults(defaults);

        assert configuration.getDefaults().containsKey("test.key1") : "Default key1 should be present";
        assert "defaultValue1".equals(configuration.getDefaults().get("test.key1")) : "Default value1 should match";

        assert configuration.getDefaults().containsKey("test.key2") : "Default key2 should be present";
        assert "defaultValue2".equals(configuration.getDefaults().get("test.key2")) : "Default value2 should match";
    }

    @Test
    public void saveDefaults() {
        configuration.addDefault("test.key", "defaultValue");
        configuration.addDefault("test.existingKey", "existingValue");
        configuration.set("test.existingKey", "existingValue2");
        System.out.println("before saving: " + configuration.getContents());
        configuration.saveDefaults();
        System.out.println("after saving: " + configuration.getContents());
        assert configuration.contains("test.key") : "Key should be present after saving defaults";
        assert configuration.contains("test.existingKey") : "Existing key should still be present after saving defaults";
        assert Objects.equals(configuration.get("test.existingKey"), "existingValue2") : "Existing key should retain its value after saving defaults";
    }

    @Test
    public void remove() {
        configuration.set("test.existingKey", "existingValue2");
        System.out.println("Contents before removing: " + configuration.getContents());
        configuration.remove("test.existingKey");
        System.out.println("Contents after removing: " + configuration.getContents());
        assert !configuration.contains("test.existingKey") : "Key should not be present after removal";
    }

    @Test
    public void set() {
        configuration.set("test.key", "value");
        System.out.println("Contents after setting: " + configuration.getContents());
        assert configuration.contains("test.key") : "Key should be present after setting value";
    }

    @Test
    public void serializeAndDeserialize() {
        TestSerializable testSerializable = new TestSerializable();
        testSerializable.id = 42;

        configuration.set("test.serializable", testSerializable);

        assert configuration.contains("test.serializable") : "Key should be present after setting serializable object";

        TestSerializable deserialized = new TestSerializable();
        //noinspection DataFlowIssue
        deserialized.deserialize((Map<String, Object>) configuration.get("test.serializable"));
        assert deserialized.id == 42 : "Deserialized id should match the original";
    }

    static class TestSerializable implements Serializable {

        private int id;

        @Override
        public @NotNull Map<String, Object> serialize() {
            return new HashMap<>() {{
                put("id", id);
            }};
        }

        @Override
        public void deserialize(@NotNull Map<String, Object> data) {
            if (data.containsKey("id")) {
                this.id = (int) data.get("id");
            }
        }
    }

}