package grabsky.configuration;

import lombok.Getter;

import java.io.File;

/** Holds information about single configuration. */
public final class ConfigurationHolder<T extends Configuration> {
    @Getter private final Class<T> configurationClass;
    @Getter private final File file;

    private ConfigurationHolder(final Class<T> configurationClass, final File configurationFile) {
        this.configurationClass = configurationClass;
        this.file = configurationFile;
    }

    /**
     * Creates instance of {@link ConfigurationHolder}.
     *
     * @param configurationClass class with fields to be replaced.
     * @param configurationFile {@link File} containing json configuration.
     * @return a new instance of {@link ConfigurationHolder}.
     */
    public static <T extends Configuration> ConfigurationHolder<T> of(final Class<T> configurationClass, final File configurationFile) {
        return new ConfigurationHolder<>(configurationClass, configurationFile);
    }
}
