package cloud.grabsky.configuration.tests;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

public final class TestIntegerSerializer implements TypeAdapter<Integer> {

    @Override
    public Integer deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        return json.getAsInt() + 10;
    }

    @Override
    public void write(final JsonWriter out, final Integer value) throws IOException {
        throw new IllegalStateException("NOT_IMPLEMENTED");
    }

    @Override
    public Integer read(final JsonReader in) throws IOException {

    }
}
