package grabsky.configuration.serialization;

import com.typesafe.config.ConfigException;
import grabsky.configuration.HoconConfig;
import grabsky.configuration.exceptions.SerializationException;

public interface TypeSerializer<T> {

    T fromConfig(final Class<T> type, final HoconConfig config) throws SerializationException, ConfigException;

}
