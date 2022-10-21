package grabsky.configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import grabsky.configuration.exceptions.SerializationException;
import grabsky.configuration.util.SerializerContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

import static grabsky.configuration.util.Numbers.*;

public class HoconConfigImpl implements HoconConfig {
    private final SerializerContainer serializerContainer;
    private final File file;
    private final Config config;

    private String path;

    protected HoconConfigImpl(final File file, final Config config, final SerializerContainer serializerContainer) {
        this.config = config;
        this.path = "";
        this.file = file;
        this.serializerContainer = serializerContainer;
    }

    @Override
    public HoconConfig at(@NotNull final String path) {
        this.path = path;
        return this;
    }

    @Override
    public <T> @Nullable T get(@NotNull final Class<T> clazz) {
        try {
            return (serializerContainer.has(clazz) == true)
                    ? serializerContainer.get(clazz).fromConfig(clazz, this)
                    : convertSimple(clazz, config.getString(path));
        } catch (final SerializationException e) {
            System.out.println("(" + file.getName() + " | " + path + ") Serialization of '" + e.getValue() + "' to '" + clazz.getName() + "' failed.");
        } catch (final ConfigException.WrongType | ConfigException.Missing e) { /* IGNORE */ }
        // Returning 'null' since value was not resolved.
        return null;
    }

    // Ugly way to handle built-in types. Should be replaced with something type-safe if possible.
    @SuppressWarnings("unchecked")
    private static <T> T convertSimple(final Class<T> clazz, final String str) throws SerializationException {
        try {
            if (clazz == String.class)
                return (T) str;

            if (clazz == Boolean.class)
                return (T) Boolean.valueOf(str);

            if (clazz == Short.class)
                return (T) toShort(str);

            if (clazz == Integer.class)
                return (T) toInteger(str);

            if (clazz == Long.class)
                return (T) Long.valueOf(str);

            if (clazz == Float.class)
                return (T) toFloat(str);

            if (clazz == Double.class)
                return (T) Double.valueOf(str);

        } catch (final NumberFormatException e) {
            // Throwing 'SerializationException' because number could not have been parsed.
            throw new SerializationException(e.getClass(), str);
        }
        // Throwing 'SerializationException' because type is incompatible.
        throw new SerializationException(clazz, str);
    }
}

