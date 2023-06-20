package cloud.grabsky.configuration.tests.tests;

import cloud.grabsky.configuration.JsonConfiguration;
import cloud.grabsky.configuration.JsonPath;
import cloud.grabsky.configuration.exception.ConfigurationMappingException;
import cloud.grabsky.configuration.tests.JsonTest;
import org.junit.jupiter.api.Test;

import java.io.File;

import static cloud.grabsky.configuration.tests.util.TestUtil.getFileFromClassPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class TestB extends JsonTest {

    @Test
    public void checkSingleFallback() throws ConfigurationMappingException {
        final File fileB = getFileFromClassPath("test_b.json");
        final File malformedFileB = getFileFromClassPath("test_b.malformed.json");

        if (fileB == null || fileB.exists() == false)
            fail("File 'test_b.json' does not exist.");

        if (malformedFileB == null || malformedFileB.exists() == false)
            fail("File 'test_b.malformed.json' does not exist.");

        // Mapping non-malformed file to populate values
        CONFIGURATION_MAPPER.map(Config.class, fileB);

        assertEquals("OK", Config.NEVER_FAILS);
        assertEquals("OK", Config.FAILS);

        // Mapping malformed file; Fields should remain unchanged because of the parsing failure
        assertThrows(ConfigurationMappingException.class, () -> CONFIGURATION_MAPPER.map(Config.class, malformedFileB));

        assertEquals("OK", Config.NEVER_FAILS);
        assertEquals("OK", Config.FAILS);
    }

    public static final class Config implements JsonConfiguration {

        @JsonPath("neverFails")
        public static String NEVER_FAILS;

        @JsonPath("fails")
        public static String FAILS;

    }
}
