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
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonReader.Token;
import com.squareup.moshi.JsonWriter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * Converts {@link String} or {@link List List&lt;String&gt;} to {@link Component} using provided function.
 */
public final class StringComponentJsonAdapter extends JsonAdapter<String> {

    /**
     * Default instance of {@link StringComponentJsonAdapter} uses {@link MiniMessage} as a conversion method.
     */
    public static final StringComponentJsonAdapter INSTANCE = new StringComponentJsonAdapter();

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public String fromJson(final @NotNull JsonReader in) throws IOException {
        if (in.peek() == Token.STRING) {
            return in.nextString();
        } else if (in.peek() == Token.BEGIN_ARRAY) {
            final StringBuilder builder = new StringBuilder();
            // ...
            in.beginArray();
            // ...
            while (in.hasNext() == true && in.peek() == Token.STRING) {
                final String text = in.nextString();
                // ...
                builder.append(text);
                // ...
                if (in.hasNext() == true) {
                    builder.append("<newline><reset>");
                }
            }
            // ...
            in.endArray();
            // ...
            return builder.toString();
        } else {
            in.skipValue();
        }
        return null;
    }

    @Override
    public void toJson(final @NotNull JsonWriter out, final String value) {
        throw new UnsupportedOperationException("NOT IMPLEMENTED");
    }
}
