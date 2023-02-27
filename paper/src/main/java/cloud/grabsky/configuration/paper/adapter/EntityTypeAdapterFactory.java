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
import lombok.NoArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import static com.squareup.moshi.Types.getRawType;

/**
 * Creates {@link JsonAdapter JsonAdapter&lt;EntityType&gt;} which converts {@link String} (as {@link NamespacedKey}) to {@link EntityType}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityTypeAdapterFactory implements JsonAdapter.Factory {
    /* SINGLETON */ public static final EntityTypeAdapterFactory INSTANCE = new EntityTypeAdapterFactory();

    @Override
    public @Nullable JsonAdapter<EntityType> create(final @NotNull Type type, final @NotNull Set<? extends Annotation> annotations, final @NotNull Moshi moshi) {
        if (EntityType.class.isAssignableFrom(getRawType(type)) == false)
            return null;
        // ...
        final JsonAdapter<NamespacedKey> adapter = moshi.adapter(NamespacedKey.class);
        // ...
        return new JsonAdapter<EntityType>() {

            @Override
            public EntityType fromJson(final @NotNull JsonReader in) throws IOException {
                final NamespacedKey key = adapter.fromJson(in);
                // ...
                if (key != null) {
                    final EntityType entity = Registry.ENTITY_TYPE.get(key);
                    // ...
                    if (entity != null)
                        return entity;
                }
                throw new JsonDataException("Expected " + EntityType.class.getName() + " at " + in.getPath() + " but found: " + key);
            }

            @Override
            public void toJson(final @NotNull JsonWriter out, final EntityType value) {
                throw new UnsupportedOperationException("NOT IMPLEMENTED");
            }

        };
    }

}
