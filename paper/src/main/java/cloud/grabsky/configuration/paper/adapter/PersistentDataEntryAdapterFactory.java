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

import cloud.grabsky.configuration.paper.object.PersistentDataEntry;
import com.squareup.moshi.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import static com.squareup.moshi.Types.getRawType;

/**
 * Creates {@link JsonAdapter JsonAdapter&lt;PersistentDataEntry&gt;} which converts JSON object to {@link PersistentDataType}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PersistentDataEntryAdapterFactory implements JsonAdapter.Factory {
    /* DEFAULT */ public static final PersistentDataEntryAdapterFactory INSTANCE = new PersistentDataEntryAdapterFactory();

    @Override
    public @Nullable JsonAdapter<PersistentDataEntry> create(final @NotNull Type type, final @NotNull Set<? extends Annotation> annotations, final @NotNull Moshi moshi) {
        if (PersistentDataEntry.class.isAssignableFrom(getRawType(type)) == false)
            return null;
        // ...
        return new JsonAdapter<>() {

            @Override
            public PersistentDataEntry fromJson(final @NotNull JsonReader in) throws IOException {
                in.beginObject();
                // ...
                final PersistentDataEntry.Init initializer = new PersistentDataEntry.Init();
                // ...
                while (in.hasNext() == true) {
                    final String name = in.nextName();
                    switch (name) {
                        case "key" -> initializer.key = moshi.adapter(NamespacedKey.class).fromJson(in);
                        case "type" -> initializer.type = moshi.adapter(PersistentDataType.class).fromJson(in);
                        case "value" -> {
                            if (initializer.type == null)
                                throw new JsonDataException("Property 'value' must be specified AFTER 'type' at " + in.getPath());
                            initializer.value = moshi.adapter(initializer.type.getComplexType()).fromJson(in);
                        }
                        default -> in.skipValue();
                    }
                }
                // ...
                in.endObject();
                // ...
                return initializer.init();
            }

            @Override
            public void toJson(final @NotNull JsonWriter out, final PersistentDataEntry value) {
                throw new UnsupportedOperationException("NOT IMPLEMENTED");
            }

        };
    }
}
