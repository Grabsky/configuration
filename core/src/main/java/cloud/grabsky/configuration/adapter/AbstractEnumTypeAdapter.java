/*
 * MIT License
 *
 * Copyright (c) 2023 Grabsky <44530932+Grabsky@users.noreply.github.com>
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
package cloud.grabsky.configuration.adapter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonReader.Token;
import com.squareup.moshi.JsonWriter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Converts {@link String} to {@link T} and vice-versa but using case-insensitive strategy.
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public abstract class AbstractEnumTypeAdapter<T extends Enum<T>> extends JsonAdapter<T> {

    @Getter(AccessLevel.PUBLIC)
    protected final Class<T> type;

    @Getter(AccessLevel.PUBLIC)
    protected final boolean isCaseSensitive;

    @Override
    public T fromJson(final @NotNull JsonReader in) throws IOException {
        if (in.peek() == Token.STRING) {
            final String value = in.nextString();
            // Iterating over enum constants for that (enum) type
            for (final T en : type.getEnumConstants()) {
                // Matching... (case sensitive)
                if (isCaseSensitive == true && en.name().equals(value) == true)
                    return en;
                // Matching... (case insensitive; default)
                else if (en.name().equalsIgnoreCase(value) == true)
                    return en;
            }
            // No enum with specified name was found; throwing an exception
            throw new JsonDataException("No enum matching '" + value + "' was found for type '" + type.getSimpleName() + "'.");
        } else if (in.peek() == Token.NUMBER) {
             final int index = in.nextInt();
             // Trying to return enum at specified index
             try {
                 return type.getEnumConstants()[index];
             } catch (final IndexOutOfBoundsException error) {
                 throw new JsonDataException("Enum index out of bounds for " + type.getSimpleName() + "($." + in.getPath() + ")", error);
             }
        }
        // Unexpected value type was found; throwing an exception
        throw new JsonDataException("Expected STRING or NUMBER, but found " + in.peek() + "($." + in.getPath() + ")");
    }

    @Override
    public void toJson(final @NotNull JsonWriter out, final T value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("NOT IMPLEMENTED");
    }

}
