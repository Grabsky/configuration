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
package cloud.grabsky.configuration.paper.adapter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonReader.Token;
import com.squareup.moshi.JsonWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Converts {@link String} or {@link String String[]} to concatenated (joined with newlines) ({@link MiniMessage}) {@link String}.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public final class StringComponentAdapter extends JsonAdapter<String> {
    /* SINGLETON */ public static final StringComponentAdapter INSTANCE = new StringComponentAdapter();

    @Override
    public String fromJson(final @NotNull JsonReader in) throws IOException {
        final Token peek = in.peek();
        return switch (peek) {
            case STRING -> in.nextString();
            case BEGIN_ARRAY -> {
                final StringBuilder builder = new StringBuilder();
                // ...
                in.beginArray();
                // ...
                while (in.hasNext() == true && in.peek() == Token.STRING) {
                    builder.append(in.nextString());
                    // ...
                    if (in.hasNext() == true) {
                        builder.append("<newline><reset>");
                    }
                }
                // ...
                in.endArray();
                // ...
                yield builder.toString();
            }
            case NULL -> null;
            default -> throw new JsonDataException("Expected STRING or BEGIN_ARRAY at " + in.getPath() + " but found: " + peek);
        };
    }

    @Override
    public void toJson(final @NotNull JsonWriter out, final String value) {
        throw new UnsupportedOperationException("NOT IMPLEMENTED");
    }

}
