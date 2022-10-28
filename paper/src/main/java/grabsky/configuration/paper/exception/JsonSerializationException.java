package grabsky.configuration.paper.exception;

import com.google.gson.JsonParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class JsonSerializationException extends JsonParseException {

    public JsonSerializationException(@NotNull final Type type, @Nullable final Object value) {
        super("[" + type.getTypeName() + "] An error occurred during (de)serialization of: " + value);
    }

}
