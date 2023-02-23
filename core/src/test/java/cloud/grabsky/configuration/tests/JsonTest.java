package cloud.grabsky.configuration.tests;

import cloud.grabsky.configuration.ConfigurationMapper;
import com.squareup.moshi.*;
import com.squareup.moshi.JsonReader.Token;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class JsonTest {
    public static ConfigurationMapper CONFIGURATION_MAPPER = ConfigurationMapper.create(
            new Moshi.Builder()
                    .add(UUID.class, UUIDSerializer.INSTANCE)
                    .build()
    );

    public static final class UUIDSerializer extends JsonAdapter<UUID> {

        public static UUIDSerializer INSTANCE = new UUIDSerializer();

        @Override
        public UUID fromJson(final @NotNull JsonReader in) throws IOException {
            if (in.peek() == Token.STRING) {
                return UUID.fromString(in.nextString());
            }
            // Not expecting anything else, skipping value and throwing an exception
            in.skipValue();
            // ...
            throw new JsonDataException("Failed to deserialize: expected STRING, found something else.");
        }

        @Override
        public void toJson(final @NotNull JsonWriter out, final UUID value) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("NOT IMPLEMENTED");
        }
    }
}
