package grabsky.configuration.exception;

import java.io.File;

/** Use {@link #getCause} to get instance of exception that caused this exception to be thrown. */
public class ConfigurationException extends RuntimeException {

    public ConfigurationException(final Class<?> configurationClass, final File file, final Throwable cause) {
        super("[" + configurationClass.getName() + "]  An error occurred during mapping of: " + file.getPath(), cause);
    }
}
