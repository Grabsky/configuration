import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.UUID;

public enum UUIDSerializer implements JsonDeserializer<UUID> {
    /* Singleton. */ INSTANCE;

    @Override
    public UUID deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        if (element instanceof JsonPrimitive json) {
            return UUID.fromString(json.getAsString());
        }
        throw new JsonParseException("Failed to deserialize: expected JsonPrimitive, found something else.");

    }

}
