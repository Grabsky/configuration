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
import it.unimi.dsi.fastutil.Pair;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.squareup.moshi.Types.getRawType;

/**
 * Creates {@link JsonAdapter JsonAdapter&lt;PersistentDataType&gt;} which converts {@link String} to {@link PersistentDataType}.
 */
public final class PersistentDataTypeAdapterFactory implements JsonAdapter.Factory {
    /* DEFAULT */ public static final PersistentDataTypeAdapterFactory INSTANCE = new PersistentDataTypeAdapterFactory();

    private final HashMap<String, PersistentDataType<?, ?>> internalMap;

    @SafeVarargs
    public PersistentDataTypeAdapterFactory(final Pair<String, PersistentDataType<?, ?>>... extras) {
        this.internalMap = new HashMap<>() {{
            put("byte", PersistentDataType.BYTE);
            put("byte_array", PersistentDataType.BYTE_ARRAY);
            put("short", PersistentDataType.SHORT);
            put("integer", PersistentDataType.INTEGER);
            put("integer_array", PersistentDataType.INTEGER_ARRAY);
            put("long", PersistentDataType.LONG);
            put("long_array", PersistentDataType.LONG_ARRAY);
            put("float", PersistentDataType.FLOAT);
            put("double", PersistentDataType.DOUBLE);
            put("string", PersistentDataType.STRING);
            // Adding extra (constructor-specified) entries
            for (var extra : extras) {
                put(extra.left(), extra.right());
            }
        }};
    }

    @Override
    public @Nullable JsonAdapter<PersistentDataType<?, ?>> create(final @NotNull Type type, final @NotNull Set<? extends Annotation> annotations, final @NotNull Moshi moshi) {
        if (PersistentDataType.class.isAssignableFrom(getRawType(type)) == false)
            return null;
        // ...
        return new JsonAdapter<>() {

            @Override
            public PersistentDataType<?, ?> fromJson(final @NotNull JsonReader in) throws IOException {
                final String value = in.nextString();
                // ...
                if (internalMap.containsKey(value) == false)
                    throw new JsonDataException("Expected one of " + internalMap.keySet() + " at " + in.getPath() + " but found: " + value);
                // ...
                return internalMap.get(value);
            }

            @Override
            public void toJson(final @NotNull JsonWriter out, final PersistentDataType<?, ?> value) {
                throw new UnsupportedOperationException("NOT IMPLEMENTED");
            }

        };
    }
}
