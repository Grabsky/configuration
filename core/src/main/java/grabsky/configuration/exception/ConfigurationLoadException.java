package grabsky.configuration.exception;

import java.io.File;

import static grabsky.configuration.util.ConsoleColor.RED;
import static grabsky.configuration.util.ConsoleColor.RESET;

/**
 * Use {@link #getCause} to get instance of exception that caused this exception to be thrown.
 */
public final class ConfigurationLoadException extends RuntimeException {

    public ConfigurationLoadException(final Class<?> clazz, final File file, final Throwable cause) {
        super("Configuration class '" + RED + clazz.getName() + RESET + "' failed to load contents of '" + RED + file.getPath() + RESET + "'", cause);
    }

}