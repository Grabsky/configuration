package grabsky.configuration.paper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import grabsky.configuration.paper.exception.JsonFormatException;
import net.kyori.adventure.sound.Sound;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Type;

/** Converts {@link NamespacedKey} {@code (key)}, {@link Sound.Source} {@code (source)}, {@link Float} {@code (volume)} and {@link Float} {@code (pitch)} to {@link Sound}. */
public final class SoundSerializer implements JsonDeserializer<Sound> {

    /** Default instance of {@link SoundSerializer}. */
    public static final SoundSerializer INSTANCE = new SoundSerializer();

    private SoundSerializer() { /* INSTANTIATING NOT ALLOWED */ }

    @Override
    public Sound deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        // Handling JsonPrimitive; "minecraft:some/sound"
        if (element instanceof JsonPrimitive json) {
            final NamespacedKey key = context.deserialize(json, NamespacedKey.class);
            return Sound.sound(key, Sound.Source.MASTER, 1F, 1F);
        // Handling JsonObject; { key = "minecraft:some/sound", ... }
        } else if (element instanceof JsonObject json) {
            final NamespacedKey key = context.deserialize(json.get("key"), NamespacedKey.class);
            return Sound.sound(
                    key,
                    (json.get("source") != null) ? context.deserialize(json.get("source"), Sound.Source.class) : Sound.Source.MASTER,
                    (json.get("volume") != null) ? json.get("volume").getAsFloat() : 1F,
                    (json.get("pitch") != null) ? json.get("pitch").getAsFloat() : 1F
            );
        }
        // Throwing an exception in case JsonElement is not a JsonPrimitive, therefore definitely not a valid String.
        throw new JsonFormatException(type, element);
    }

}
