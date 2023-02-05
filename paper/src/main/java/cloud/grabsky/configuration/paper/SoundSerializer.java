/*
 * MIT License
 *
 * Copyright (c) 2023 Grabsky
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * HORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cloud.grabsky.configuration.paper;

import cloud.grabsky.configuration.paper.exception.JsonFormatException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.sound.Sound;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Type;

/**
 * Converts {@link NamespacedKey} {@code (key)}, {@link Sound.Source} {@code (source)}, {@link Float} {@code (volume)} and {@link Float} {@code (pitch)} to {@link Sound}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) // NO INSTANTIATING ALLOWED
public final class SoundSerializer implements JsonDeserializer<Sound> {

    /**
     * Default instance of {@link SoundSerializer}.
     */
    public static final SoundSerializer INSTANCE = new SoundSerializer();

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
