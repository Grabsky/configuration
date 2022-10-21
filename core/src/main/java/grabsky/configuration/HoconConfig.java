package grabsky.configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import grabsky.configuration.serialization.TypeSerializer;
import grabsky.configuration.util.SerializerContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

import static grabsky.configuration.util.Conditions.requirePresent;

public interface HoconConfig {

    /**
     * Sets HoconConfig instance to specified path.
     *
     * @param path path.
     * @return HoconConfig instance at specified path.
     */
    HoconConfig at(@NotNull final String path);

    /**
     * Returns value parsed to specified class or null if serialization failed.
     *
     * @param type type
     * @return value parsed to specified class or null if serialization failed.
     */
    <T> @Nullable T get(@NotNull final Class<T> type);

    /**
     * Returns value parsed to specified class or default.
     *
     * @param type type
     * @return value parsed to specified class or null if serialization failed.
     */
    default <T> @NotNull T get(@NotNull final Class<T> type, @NotNull final T def) {
        return requirePresent(this.get(type), def);
    }

    static HoconConfig.Builder builder(final File file) {
        return new HoconConfig.Builder(file);
    }

    static HoconConfig.Builder builder(final String string) {
        return new HoconConfig.Builder(string);
    }

    static HoconConfig standard(final File file) {
        return builder(file).build();
    }

    static HoconConfig standard(final String string) {
        return builder(string).build();
    }

    /** Builder class */
    class Builder {
        private final File file;
        private final Config config;
        private final SerializerContainer serializerContainer = new SerializerContainer();

        protected Builder(final File file) {
            this.file = file;
            this.config = ConfigFactory.parseFile(file);
        }

        protected Builder(final String string) {
            this.file = null;
            this.config = ConfigFactory.parseString(string);
        }

        public <T> Builder withSerializer(@NotNull final Class<T> type, @NotNull final TypeSerializer<T> serializer) {
            serializerContainer.set(type, serializer);
            return this;
        }

        public HoconConfig build() {
            return new HoconConfigImpl(file, config, serializerContainer);
        }
    }
}
