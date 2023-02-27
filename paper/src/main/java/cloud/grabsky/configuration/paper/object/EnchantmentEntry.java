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

import lombok.*;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnchantmentEntry {

    @Getter(AccessLevel.PUBLIC)
    private final @NotNull Enchantment enchantment;

    @Getter(AccessLevel.PUBLIC)
    private final int level;

    /* BUILDER */

    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static final class Builder {

        @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC)
        private Enchantment enchantment;

        @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC)
        private Integer level;

        public boolean isValid() {
            return enchantment != null && level != null && level > 0;
        }

        public EnchantmentEntry build() {
            if (this.isValid() == false)
                return null;
            // ...
            return new EnchantmentEntry(enchantment, level);
        }

    }

}
