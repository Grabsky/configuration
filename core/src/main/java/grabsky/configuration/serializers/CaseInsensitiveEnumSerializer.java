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
package grabsky.configuration.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/** Converts {@link String} to {@link T} and vice-versa but using case-insensitive strategy. */
public interface CaseInsensitiveEnumSerializer<T extends Enum<T>> extends JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    default JsonElement serialize(final T src, final Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override @SuppressWarnings("unchecked")
    default T deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        if (element.isJsonPrimitive() == false)
            throw new JsonParseException("Could not convert '" + element + "' to an enum.");
        // Unwrapping value as String
        final String cons = element.getAsString();
        // Iterating over enum constants
        for (final Enum<T> e : ((Class<Enum<T>>) type).getEnumConstants()) {
            // Comparing and returning if matches
            if (e.toString().equalsIgnoreCase(cons))
                return (T) e;
        }
        // No enum was found
        throw new JsonParseException("No enum matching '" + cons + "' was found for type '" + type.getTypeName() + "'.");
    }
}
