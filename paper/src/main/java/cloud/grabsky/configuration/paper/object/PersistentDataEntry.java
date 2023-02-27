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
package cloud.grabsky.configuration.paper.object;

import com.squareup.moshi.JsonAdapter;
import lombok.*;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class PersistentDataEntry {

    @Getter(AccessLevel.PUBLIC)
    private final @NotNull NamespacedKey key;

    @Getter(AccessLevel.PUBLIC)
    private final @NotNull PersistentDataType<?, ?> type;

    @Getter(AccessLevel.PUBLIC)
    private final @NotNull JsonAdapter<?> adapter;

    @Getter(AccessLevel.PUBLIC)
    private final @NotNull Object value;

    /* BUILDER */

    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static final class Builder {

        @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC)
        private NamespacedKey key;

        @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC)
        private PersistentDataType<?, ?> dataType;

        @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC)
        private JsonAdapter<?> adapter;

        @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC)
        private Object value;

        public PersistentDataEntry build() {
            if (key == null || dataType == null || adapter == null || value == null)
                return null;
            // ...
            return new PersistentDataEntry(key, dataType, adapter, value);
        }

    }

}