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
import com.squareup.moshi.JsonWriter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Converts {@link String} to {@link NamespacedKey}.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class NamespacedKeyJsonAdapter extends JsonAdapter<NamespacedKey> {
    /* SINGLETON */ public static final NamespacedKeyJsonAdapter INSTANCE = new NamespacedKeyJsonAdapter();

    @Override
    public NamespacedKey fromJson(final @NotNull JsonReader in) throws IOException {
        final String value = in.nextString();
        final String[] parts = value.split(":");
        // ...
        if (parts.length < 1 || parts.length > 2)
            throw new JsonDataException("Invalid NamespacedKey: " + value);
        // ...
        return (parts.length == 1)
                ? new NamespacedKey(NamespacedKey.MINECRAFT, parts[0]) // defaults to 'minecraft:' namespace
                : new NamespacedKey(parts[0], parts[1]);
    }

    @Override
    public void toJson(final @NotNull JsonWriter out, final NamespacedKey value) {
        throw new UnsupportedOperationException("NOT IMPLEMENTED");
    }

}
