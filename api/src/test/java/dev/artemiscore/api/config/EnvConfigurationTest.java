package dev.artemiscore.api.config;

import dev.artemiscore.api.config.file.env.EnvConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EnvConfigurationTest {
    private EnvConfiguration envConfiguration;

    @Before
    public void setUp() {
        this.envConfiguration = new EnvConfiguration();
    }

    @After
    public void tearDown() {
        this.envConfiguration = null;
    }

    @Test
    public void testLoadEnvOnly() {
        this.envConfiguration.loadEnvOnly();
        assert this.envConfiguration.getContents() != null : "Contents should not be null";
        System.out.println("--- ENV VARIABLES ---");
        this.envConfiguration.getContents().forEach((key, value) -> System.out.printf("%s = %s%n", key, value));
    }

    @Test
    public void testLoadSystemPropertiesOnly() {
        this.envConfiguration.loadSystemPropertiesOnly();
        assert this.envConfiguration.getContents() != null : "Contents should not be null";
        System.out.println("--- SYSTEM PROPERTIES ---");
        this.envConfiguration.getContents().forEach((key, value) -> System.out.printf("%s = %s%n", key, value));
    }

    @Test
    public void testLoadAll() {
        this.envConfiguration.loadAll();
        assert this.envConfiguration.getContents() != null : "Contents should not be null";
        System.out.println("--- ENV + SYSTEM PROPERTIES ---");
        this.envConfiguration.getContents().forEach((key, value) -> System.out.printf("%s = %s%n", key, value));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSaveThrowsException() {
        this.envConfiguration.save();
    }
}