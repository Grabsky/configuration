package cloud.grabsky.configuration.tests.tests;

import cloud.grabsky.configuration.JsonAdapter;
import cloud.grabsky.configuration.JsonConfiguration;
import cloud.grabsky.configuration.JsonPath;
import cloud.grabsky.configuration.exception.ConfigurationException;
import cloud.grabsky.configuration.tests.JsonTest;
import cloud.grabsky.configuration.tests.util.TestUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestA extends JsonTest {

    @Test
    public void checkTypes() throws ConfigurationException {
        final File file = TestUtil.getFileFromClassPath("test_a.json");

        if (file == null || file.exists() == false)
            fail("File 'test_a.json' does not exist.");

        CONFIGURATION_MAPPER.map(Config.class, file);

        assertEquals("OK", Config.TEST_A);
        assertEquals(7, Config.TEST_B);
        assertEquals(7.777777D, Config.TEST_C);
        assertEquals("OK", Config.TEST_D);
        assertEquals("OK", Config.TEST_E);
        assertEquals("OK", Config.TEST_F.get(1));
        assertEquals(UUID.fromString("456fb8eb-f13d-4a34-8284-67e2469b634d"), Config.TEST_H);

        assertTrue(Config.WAS_RELOADED);

    }

    public static final class Config implements JsonConfiguration {
        public static boolean WAS_RELOADED = false;

        @JsonPath("simpleString")
        public static String TEST_A;

        @JsonPath("simpleInteger")
        public static Integer TEST_B;

        @JsonPath("simpleDouble")
        public static Double TEST_C;

        @JsonPath("simpleObject.simpleString")
        public static String TEST_D;

        @JsonPath("firstObject.secondObject.simpleString")
        public static String TEST_E;

        @JsonPath("simpleArray")
        public static List<String> TEST_F;

        @JsonPath("uuid")
        public static UUID TEST_H;

        @Override
        public void onReload() {
            WAS_RELOADED = true;
        }

    }
}
