package grabsky.configuration.util;

import grabsky.configuration.serialization.TypeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SerializerContainer {
    private final Map<Class<?>, TypeSerializer<?>> internalMap;

    public SerializerContainer() {
        this.internalMap = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T> TypeSerializer<T> get(@NotNull final Class<T> key) {
        return (TypeSerializer<T>) internalMap.get(key);
    }

    public <T> boolean has(@NotNull Class<T> key) {
        return internalMap.containsKey(key);
    }

    public <T> SerializerContainer set(@NotNull final Class<T> key, @Nullable final TypeSerializer<T> value) {
        internalMap.put(key, value);
        return this;
    }
}
