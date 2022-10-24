import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import configuration.TestConfigA;
import configuration.TestConfigB;
import grabsky.configuration.ConfigurationMapper;
import grabsky.configuration.exception.ConfigurationLoadException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonTest {
    private final Gson gson;
    private final ConfigurationMapper mapper;

    public JsonTest() {
        this.gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(UUID.class, UUIDSerializer.INSTANCE)
                .disableHtmlEscaping()
                .create();
        this.mapper = ConfigurationMapper.create(gson);
    }

    @Test
    public void mapSingle() {
        final File file = getFileFromClassPath("test_a.json");

        if (file == null || file.exists() == false)
            fail("Test files could could not have been found.");

        this.mapper.map(TestConfigA.class, file);

        assertEquals("OK", TestConfigA.TEST_A);
        assertEquals(7, TestConfigA.TEST_B);
        assertEquals(7.777777D, TestConfigA.TEST_C);
        assertEquals("OK", TestConfigA.TEST_D);
        assertEquals("OK", TestConfigA.TEST_E);
        assertEquals("OK", TestConfigA.TEST_F);
        assertEquals("OK", TestConfigA.TEST_G);
        assertEquals(UUID.fromString("456fb8eb-f13d-4a34-8284-67e2469b634d"), TestConfigA.TEST_H);

        assertTrue(TestConfigA.WAS_RELOADED);

    }

    @Test
    public void mapSingleFallback() {
        final File file = getFileFromClassPath("test_b.json");
        final File malformedFile = getFileFromClassPath("test_b.malformed.json");

        if (file == null || file.exists() == false || malformedFile == null || malformedFile.exists() == false)
            fail("Test files could could not have been found.");

        // Mapping non-malformed file to populate values
        this.mapper.map(TestConfigB.class, file);

        assertEquals("OK", TestConfigB.NEVER_FAILS);
        assertEquals("OK", TestConfigB.FAILS);

        // Mapping non-malformed file; Fields should remain unchanged because of the parsing failure
        assertThrows(ConfigurationLoadException.class, () -> mapper.map(TestConfigB.class, malformedFile));

        assertEquals("OK", TestConfigB.NEVER_FAILS);
        assertEquals("OK", TestConfigB.FAILS);

    }

    public static File getFileFromClassPath(final String name) {
        final URL url = JsonTest.class.getClassLoader().getResource(name);
        return (url != null) ? new File(url.getPath()) : null;
    }

}
