package configuration;

import grabsky.configuration.Configuration;
import grabsky.configuration.JsonPath;

public final class TestConfigB extends Configuration {

    @JsonPath("neverFails")
    public static String NEVER_FAILS;

    @JsonPath("fails")
    public static String FAILS;

}