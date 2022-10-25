package grabsky.configuration.exception;

import java.io.File;

import static grabsky.configuration.util.ConsoleColor.RESET;
import static grabsky.configuration.util.ConsoleColor.YELLOW;

/** Use {@link #getCause} to get instance of exception that caused this exception to be thrown. */
public class ConfigurationException extends Exception {

    public ConfigurationException(final Class<?> clazz, final File file, final Throwable cause) {
        super("Configuration class " + YELLOW + clazz.getName() + RESET + " failed to load contents of " + YELLOW + file.getPath() + RESET, cause);
    }
}
