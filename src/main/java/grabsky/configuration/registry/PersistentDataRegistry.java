package grabsky.configuration.registry;

import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public final class PersistentDataRegistry implements Registry<String, PersistentDataType<?, ?>> {
    /* Singleton. */ public static final PersistentDataRegistry INSTANCE = new PersistentDataRegistry();

    private final HashMap<String, PersistentDataType<?, ?>> internalMap = new HashMap<>();

    // INTERNAL USE ONLY; NO INSTANCING ALLOWED
    private PersistentDataRegistry() {
        this.add("byte", PersistentDataType.BYTE);
        this.add("byte_array", PersistentDataType.BYTE_ARRAY);
        this.add("short", PersistentDataType.SHORT);
        this.add("integer", PersistentDataType.INTEGER);
        this.add("integer_array", PersistentDataType.INTEGER_ARRAY);
        this.add("long", PersistentDataType.LONG);
        this.add("long_array", PersistentDataType.LONG_ARRAY);
        this.add("float", PersistentDataType.FLOAT);
        this.add("double", PersistentDataType.DOUBLE);
        this.add("string", PersistentDataType.STRING);
    }

    @Override
    public PersistentDataRegistry add(@NotNull final String key, @NotNull final PersistentDataType<? ,?> type) {
        internalMap.putIfAbsent(key, type);
        return this;
    }

    @Override
    public @Nullable PersistentDataType<?, ?> get(final String key) {
        return internalMap.get(key);
    }
}
