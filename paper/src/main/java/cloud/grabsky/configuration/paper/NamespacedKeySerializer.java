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

import com.google.gson.*;
import cloud.grabsky.configuration.paper.exception.JsonFormatException;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Type;

/** Converts {@link String} to {@link org.bukkit.NamespacedKey}. */
public final class NamespacedKeySerializer implements JsonDeserializer<NamespacedKey> {

    /** Default instance of {@link NamespacedKeySerializer}. */
    public static final NamespacedKeySerializer INSTANCE = new NamespacedKeySerializer();

    private NamespacedKeySerializer() { /* INSTANTIATING NOT ALLOWED */ }

    @Override
    public NamespacedKey deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        if (element instanceof JsonPrimitive json) {
            try {
                return NamespacedKey.fromString(json.getAsString().toLowerCase());
            } catch (final IllegalArgumentException e) {
                throw new JsonFormatException(type, json);
            }
        }
        // Throwing an exception in case JsonElement is not a JsonPrimitive, therefore definitely not a valid String.
        throw new JsonFormatException(type, element);
    }

}
