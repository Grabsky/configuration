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
import com.squareup.moshi.*;
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
 * Creates {@link JsonAdapter JsonAdapter&lt;EntityType&gt;} which converts ...
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
        return new JsonAdapter<EnchantmentEntry>() {
            @Override
            public EnchantmentEntry fromJson(final @NotNull JsonReader in) throws IOException {
                // ...
                in.beginObject();
                // ...
                final EnchantmentEntry.Builder builder = new EnchantmentEntry.Builder();
                // ...
                while (in.hasNext() == true) {
                    switch (in.nextName()) {
                        case "key" -> {
                            final Enchantment enchantment = adapter.fromJson(in);
                            builder.setEnchantment(enchantment);
                        }
                        case "level" -> {
                            final int level = in.nextInt();
                            builder.setLevel(level);
                        }
                    }
                }
                // ...
                in.endObject();
                // ...
                if (builder.isValid() == true)
                    return builder.build();
                // ...
                throw new JsonDataException("Expected " + EnchantmentEntry.class.getName() + " at " + in.getPath() + " but found: " + null);
            }

            @Override
            public void toJson(final @NotNull JsonWriter out, final EnchantmentEntry value) {
                throw new UnsupportedOperationException("NOT IMPLEMENTED");
            }

        };
    }

}
