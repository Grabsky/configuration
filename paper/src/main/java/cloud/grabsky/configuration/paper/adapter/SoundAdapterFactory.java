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

import cloud.grabsky.configuration.util.LazyInit;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.sound.Sound;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import static cloud.grabsky.configuration.util.LazyInit.notNull;
import static com.squareup.moshi.Types.getRawType;
import static org.jetbrains.annotations.ApiStatus.Internal;

/**
 * Creates {@link JsonAdapter JsonAdapter&lt;Sound&gt;} which converts JSON object to {@link Sound}.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SoundAdapterFactory implements JsonAdapter.Factory {
    /* SINGLETON */ public static final SoundAdapterFactory INSTANCE = new SoundAdapterFactory();

    @Override
    public @Nullable JsonAdapter<Sound> create(final @NotNull Type type, final @NotNull Set<? extends Annotation> annotations, final @NotNull Moshi moshi) {
        if (Sound.class.isAssignableFrom(getRawType(type)) == false)
            return null;
        // ...
        final JsonAdapter<NamespacedKey> adapter0 = moshi.adapter(NamespacedKey.class);
        final JsonAdapter<Sound.Source> adapter1 = moshi.adapter(Sound.Source.class);
        // ...
        return new JsonAdapter<>() {
            @Override
            public Sound fromJson(final @NotNull JsonReader in) throws IOException {
                // ...
                in.beginObject();
                // ...
                final SoundInit initializer = new SoundInit();
                // ...
                while (in.hasNext() == true) {
                    switch (in.nextName()) {
                        case "key" -> initializer.key = adapter0.fromJson(in);
                        case "source" -> initializer.source = adapter1.fromJson(in);
                        case "volume" -> initializer.volume = (float) in.nextDouble();
                        case "pitch" -> initializer.pitch = (float) in.nextDouble();
                        default -> in.skipValue();
                    }
                }
                // ...
                in.endObject();
                // ...
                return initializer.init();
            }

            @Override
            public void toJson(final @NotNull JsonWriter out, final Sound value) {
                throw new UnsupportedOperationException("NOT IMPLEMENTED");
            }

        };
    }

    @Internal
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class SoundInit implements LazyInit<Sound> {

        public NamespacedKey key;
        public Sound.Source source;
        public Float volume;
        public Float pitch;

        @Override
        public Sound init() throws IllegalStateException {
            return Sound.sound(
                    notNull(key, "key", NamespacedKey.class),
                    notNull(source, "source", Sound.Source.class),
                    notNull(volume, "volume", Float.class),
                    notNull(pitch, "pitch", Float.class)
            );
        }

    }

}
