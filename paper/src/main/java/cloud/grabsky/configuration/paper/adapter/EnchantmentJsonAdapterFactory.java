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

import com.squareup.moshi.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import static cloud.grabsky.configuration.paper.util.Conditions.requirePresent;

/**
 * Creates {@link JsonAdapter JsonAdapter&lt;Enchantment&gt;} which converts {@link String} (as {@link NamespacedKey}) to {@link Enchantment}.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnchantmentJsonAdapterFactory implements JsonAdapter.Factory {
    /* SINGLETON */ public static final EnchantmentJsonAdapterFactory INSTANCE = new EnchantmentJsonAdapterFactory();

    @Override
    public @Nullable JsonAdapter<Enchantment> create(final @NotNull Type type, final @NotNull Set<? extends Annotation> annotations, final @NotNull Moshi moshi) {
        if (Enchantment.class.isAssignableFrom(Types.getRawType(type)) == false)
            return null;
        // ...
        final JsonAdapter<NamespacedKey> namespacedKeyAdapter = requirePresent(
                moshi.adapter(NamespacedKey.class),
                new IllegalArgumentException("No adapter found for type: " + NamespacedKey.class.getName())
        );
        // ...
        return new JsonAdapter<Enchantment>() {

            @Override
            public Enchantment fromJson(final @NotNull JsonReader in) throws IOException {
                final NamespacedKey enchantmentKey = namespacedKeyAdapter.fromJson(in);
                // ...
                if (enchantmentKey != null) {
                    final Enchantment Enchantment = Registry.ENCHANTMENT.get(enchantmentKey);
                    // ...
                    if (Enchantment != null)
                        return Enchantment;
                    // ...
                    throw new JsonDataException("Incorrect Enchantment key: " + enchantmentKey.asString());
                }
                throw new JsonDataException("Incorrect Enchantment key: " + null);
            }

            @Override
            public void toJson(final @NotNull JsonWriter out, final Enchantment value) {
                throw new UnsupportedOperationException("NOT IMPLEMENTED");
            }

        };
    }

}
