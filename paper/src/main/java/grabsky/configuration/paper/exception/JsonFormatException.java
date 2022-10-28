package grabsky.configuration.paper.exception;

import com.google.gson.JsonParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class JsonFormatException extends JsonParseException {

    public JsonFormatException(@NotNull final Type type, @Nullable final Object value) {
        super("[" + type.getTypeName() + "] An error occurred during (de)serialization of: " + value);
    }

}
