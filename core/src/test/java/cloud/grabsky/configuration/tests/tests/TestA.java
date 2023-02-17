package cloud.grabsky.configuration.tests.tests;

import cloud.grabsky.configuration.Serialization;
import cloud.grabsky.configuration.Path;
import cloud.grabsky.configuration.Configuration;
import cloud.grabsky.configuration.exception.ConfigurationException;
import cloud.grabsky.configuration.tests.TestIntegerSerializer;
import cloud.grabsky.configuration.tests.util.TestUtil;
import cloud.grabsky.configuration.tests.JsonTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
        assertEquals("OK", Config.TEST_F);
        assertEquals("OK", Config.TEST_G);
        assertEquals(UUID.fromString("456fb8eb-f13d-4a34-8284-67e2469b634d"), Config.TEST_H);

        assertTrue(Config.WAS_RELOADED);

    }

    public static final class Config implements Configuration {
        public static boolean WAS_RELOADED = false;

        @Path("simpleString")
        public static String TEST_A;

        @Path("simpleInteger") @Serialization(deserializer = TestIntegerSerializer.class)
        public static Integer TEST_B;

        @Path("simpleDouble")
        public static Double TEST_C;

        @Path("simpleObject.simpleString")
        public static String TEST_D;

        @Path("firstObject.secondObject.simpleString")
        public static String TEST_E;

        @Path("simpleArray[1]")
        public static String TEST_F;

        @Path("complexArray[0].simpleString")
        public static String TEST_G;

        @Path("uuid")
        public static UUID TEST_H;

        @Override
        public void onReload() {
            WAS_RELOADED = true;
            System.out.println(TEST_B);
        }

    }
}
