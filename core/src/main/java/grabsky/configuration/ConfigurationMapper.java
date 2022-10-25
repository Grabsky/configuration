package grabsky.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import grabsky.configuration.exception.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface ConfigurationMapper {

    /**
     * Maps file contents to {@code public}, {@code static}, {@code non-final} fields declared inside provided class.
     * When method fails due to {@link JsonParseException} - a new {@link ConfigurationException}
     * is thrown and fields of {@link T} remain unchanged.
     *
     * @param configurationClass class with fields to be replaced.
     * @param file {@link File} containing json configuration.
     * @throws ConfigurationException when configuration fails to load.
     */
    <T extends Configuration> void map(@NotNull final Class<T> configurationClass, @NotNull final File file) throws ConfigurationException;

    /**
     * Maps contents of all files to {@code public}, {@code static}, {@code non-final} fields declared in relative classes.
     * When method fails due to {@link JsonParseException} - a new {@link ConfigurationException}
     * is thrown and <b><i>all</i></b> fields remain unchanged.
     *
     * @param configurations vararg of {@link ConfigurationHolder} instances
     * @throws ConfigurationException when configuration fails to load.
     */
    void mapCollection(@NotNull final ConfigurationHolder<?>... configurations) throws ConfigurationException;

    /**
     * Creates {@link ConfigurationMapper} instance using provided {@link Gson} instance for re-mapping.
     *
     * @param gson {@link Gson} instance.
     * @return a new instance of {@link ConfigurationMapper}.
     */
    static @NotNull ConfigurationMapper create(@NotNull final Gson gson) {
        return new ConfigurationMapperImpl(gson);
    }

}
