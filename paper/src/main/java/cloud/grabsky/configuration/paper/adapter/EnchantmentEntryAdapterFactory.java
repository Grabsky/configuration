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

import cloud.grabsky.configuration.paper.object.EnchantmentEntry;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import static com.squareup.moshi.Types.getRawType;

/**
 * Creates {@link JsonAdapter JsonAdapter&lt;EnchantmentEntry&gt;} which converts JSON object to {@link EnchantmentEntry}.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnchantmentEntryAdapterFactory implements JsonAdapter.Factory {
    /* SINGLETON */ public static final EnchantmentEntryAdapterFactory INSTANCE = new EnchantmentEntryAdapterFactory();

    @Override
    public @Nullable JsonAdapter<EnchantmentEntry> create(final @NotNull Type type, final @NotNull Set<? extends Annotation> annotations, final @NotNull Moshi moshi) {
        if (EnchantmentEntry.class.isAssignableFrom(getRawType(type)) == false)
            return null;
        // ...
        final JsonAdapter<Enchantment> adapter = moshi.adapter(Enchantment.class);
        // ...
        return new JsonAdapter<>() {

            @Override
            public EnchantmentEntry fromJson(final @NotNull JsonReader in) throws IOException {
                in.beginObject();
                // ...
                final EnchantmentEntry.Init initializer = new EnchantmentEntry.Init();
                // ...
                while (in.hasNext() == true) {
                    final String nextName = in.nextName();
                    switch (nextName) {
                        case "key" -> initializer.enchantment = adapter.fromJson(in);
                        case "level" -> initializer.level = in.nextInt();
                        default -> throw new JsonDataException("Unexpected field at " + in.getPath() + ": " + nextName);
                    }
                }
                in.endObject();
                // ...
                return initializer.init();
            }

            @Override
            public void toJson(final @NotNull JsonWriter out, final EnchantmentEntry value) {
                throw new UnsupportedOperationException("NOT IMPLEMENTED");
            }

        };
    }

}
