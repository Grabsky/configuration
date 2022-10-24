package configuration;

import grabsky.configuration.Configuration;
import grabsky.configuration.JsonPath;

import java.util.UUID;

public final class TestConfigA extends Configuration {
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

    @JsonPath("simpleArray[1]")
    public static String TEST_F;

    @JsonPath("complexArray[0].simpleString")
    public static String TEST_G;

    @JsonPath("uuid")
    public static UUID TEST_H;

    @Override
    public void onReload() {
        WAS_RELOADED = true;
    }
}