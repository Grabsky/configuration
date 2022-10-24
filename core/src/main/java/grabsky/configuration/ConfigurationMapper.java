package grabsky.configuration;

import com.google.gson.Gson;
import grabsky.configuration.exception.ConfigurationLoadException;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface ConfigurationMapper {

    /**
     * Maps contents of {@link File} to public, static, non-final fields declared inside provided class.
     * When method fails due to {@link com.google.gson.JsonParseException} - fields of {@link T} remain unchanged.
     *
     * @param configurationClass class with fields to be replaced.
     * @param file {@link File} containing json configuration.
     * @throws ConfigurationLoadException when configuration fails to load.
     */
    <T extends Configuration> void map(@NotNull final Class<T> configurationClass, @NotNull final File file) throws ConfigurationLoadException;

    /**
     * Maps contents of all {@link ConfigurationHolder} files to their relative classes.
     * When method fails due to {@link com.google.gson.JsonParseException} - fields of relative classes remain unchanged.
     *
     * @param configurations vararg of {@link ConfigurationHolder} instances
     * @throws ConfigurationLoadException when configuration fails to load.
     */
    void mapCollection(@NotNull final ConfigurationHolder<?>... configurations) throws ConfigurationLoadException;

    /**
     * Creates {@link ConfigurationMapper} instance using provided {@link Gson} instance for re-mapping.
     *
     * @param gson {@link Gson} instance.
     * @return new instance of {@link ConfigurationMapper}.
     */
    static @NotNull ConfigurationMapper create(@NotNull final Gson gson) {
        return new ConfigurationMapperImpl(gson);
    }

}
