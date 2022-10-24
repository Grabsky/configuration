package grabsky.configuration.paper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import grabsky.configuration.paper.exception.JsonSerializationException;
import grabsky.configuration.paper.object.PersistentDataEntry;
import grabsky.configuration.paper.object.PersistentDataTypeHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Type;
import java.util.HashMap;

/** Converts {@link NamespacedKey} {@code (key)}, {@link PersistentDataType} {@code (type)} and {@link Object} {@code (value)} to {@link PersistentDataEntry}. */
public final class PersistentDataEntrySerializer implements JsonDeserializer<PersistentDataEntry> {

    /**
     * Default instance of {@link EnchantmentSerializer}.<br>
     * <br>
     * Supported types:<br>
     * - {@link PersistentDataType#BYTE} {@code (byte)}<br>
     * - {@link PersistentDataType#BYTE_ARRAY} {@code (byte_array)}<br>
     * - {@link PersistentDataType#SHORT} {@code (short)}<br>
     * - {@link PersistentDataType#INTEGER} {@code (integer)}<br>
     * - {@link PersistentDataType#INTEGER_ARRAY} {@code (integer_array)}<br>
     * - {@link PersistentDataType#LONG} {@code (long)}<br>
     * - {@link PersistentDataType#LONG_ARRAY} {@code (long_array)}<br>
     * - {@link PersistentDataType#DOUBLE} {@code (double)}<br>
     * - {@link PersistentDataType#FLOAT} {@code (float)}<br>
     * - {@link PersistentDataType#STRING} {@code (string)}<br>
     * Create new instance using constructor if you want to use custom types.
     */
    public static final PersistentDataEntrySerializer DEFAULT = new PersistentDataEntrySerializer();

    private final HashMap<String, PersistentDataType<?, ?>> internalMap = new HashMap<>();

    public PersistentDataEntrySerializer(final PersistentDataTypeHolder<?, ?>... extras) {
        this.internalMap.put("byte", PersistentDataType.BYTE);
        this.internalMap.put("byte_array", PersistentDataType.BYTE_ARRAY);
        this.internalMap.put("short", PersistentDataType.SHORT);
        this.internalMap.put("integer", PersistentDataType.INTEGER);
        this.internalMap.put("integer_array", PersistentDataType.INTEGER_ARRAY);
        this.internalMap.put("long", PersistentDataType.LONG);
        this.internalMap.put("long_array", PersistentDataType.LONG_ARRAY);
        this.internalMap.put("float", PersistentDataType.FLOAT);
        this.internalMap.put("double", PersistentDataType.DOUBLE);
        this.internalMap.put("string", PersistentDataType.STRING);
        // ...
        for (var holder : extras) {
            this.internalMap.put(holder.getKey(), holder.getType());
        }
    }

    @Override
    public PersistentDataEntry deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        if (element instanceof JsonObject json) {
            final NamespacedKey key = context.deserialize(json.get("key"), NamespacedKey.class);
            final String dataType = json.get("type").getAsString();
            // ...
            if (internalMap.containsKey(dataType) == false)
                throw new JsonSerializationException(PersistentDataType.class, dataType);
            // ...
            final PersistentDataType<?, ?> persistentDataType = internalMap.get(dataType);
            // ...
            final Object value = context.deserialize(json.get("value"), persistentDataType.getComplexType());
            // ...
            return new PersistentDataEntry(key, persistentDataType, value);
        }
        throw new JsonSerializationException(type, element);
    }

}