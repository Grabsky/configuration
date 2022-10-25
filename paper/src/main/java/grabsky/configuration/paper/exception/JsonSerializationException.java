package grabsky.configuration.paper.exception;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class JsonSerializationException extends JsonParseException {

    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";

    public JsonSerializationException(@NotNull final Type expected, @Nullable final JsonElement value) {
        super("Could not create a valid " + YELLOW + expected.getTypeName() + RESET + " from " + YELLOW + value + RESET);
    }

    public JsonSerializationException(@NotNull final Type expected, @Nullable final String value) {
        super("Could not create a valid " + YELLOW + expected.getTypeName() + RESET + " from " + YELLOW + value + RESET);
    }

}
