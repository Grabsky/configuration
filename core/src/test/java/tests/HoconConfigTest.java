package tests;

import grabsky.configuration.HoconConfig;
import grabsky.configuration.exceptions.SerializationException;
import grabsky.configuration.serialization.TypeSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.UUID;

import static grabsky.configuration.util.Conditions.requirePresent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HoconConfigTest {

    void testConfigValues(final HoconConfig config) {
        assertEquals(config.at("testStringDefault").get(String.class, "N/A"), "N/A");
        assertNull(config.at("testStringNonExistent").get(String.class));
        assertEquals(config.at("testString").get(String.class), "Hello!");
        assertEquals(config.at("testBoolean").get(Boolean.class), true);
        assertEquals(config.at("testShort").get(Short.class), (short) 123);
        assertEquals(config.at("testShortUnderflow").get(Short.class), Short.MIN_VALUE);
        assertEquals(config.at("testShortOverflow").get(Short.class), Short.MAX_VALUE);
        assertEquals(config.at("testInteger").get(Integer.class), 123);
        assertEquals(config.at("testIntegerUnderflow").get(Integer.class), Integer.MIN_VALUE);
        assertEquals(config.at("testIntegerOverflow").get(Integer.class), Integer.MAX_VALUE);
        assertEquals(config.at("testLong").get(Long.class), Long.MAX_VALUE);
        assertEquals(config.at("testFloat").get(Float.class), 5.5005F);
        assertEquals(config.at("testDouble").get(Double.class), 5.5000000000005D);
        assertEquals(config.at("testUUID").get(UUID.class), UUID.fromString("23687b43-921b-4c39-b19f-7533120b2919"));
    }

    @Test
    void testConfigFile() {
        final URL resource = this.getClass().getClassLoader().getResource("test.conf");

        if (resource == null)
            Assertions.fail("File 'resources/test.conf' does not exist.");

        final HoconConfig config = HoconConfig.builder(new File(resource.getFile()))
                .withSerializer(UUID.class, UUIDTypeSerializer.INSTANCE)
                .build();

        testConfigValues(config);
    }

    @Test
    void testConfigString() {
        final String string = """
                testString = "Hello!"
                testBoolean = true
                testShort = 123
                testShortUnderflow = -32769 # Short.MIN - 1
                testShortOverflow = 32768 # Short.MAX + 1
                testInteger = 123
                testIntegerUnderflow = -2147483649 # Integer.MIN - 1
                testIntegerOverflow = 2147483648 # Integer.MAX + 1
                testLong = 9223372036854775807
                testFloat = 5.5005
                testDouble = 5.5000000000005
                testUUID = "23687b43-921b-4c39-b19f-7533120b2919"
                """;

        final HoconConfig config = HoconConfig.builder(string)
                .withSerializer(UUID.class, UUIDTypeSerializer.INSTANCE)
                .build();

        testConfigValues(config);
    }

    public enum UUIDTypeSerializer implements TypeSerializer<UUID> {
        /* Singleton. */ INSTANCE;

        @Override
        public UUID fromConfig(Class<UUID> type, HoconConfig config) throws SerializationException {
            final String val = requirePresent(config.get(String.class), new SerializationException(type, null));
            // ...
            return UUID.fromString(val);
        }

    }
}
