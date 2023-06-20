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

import cloud.grabsky.configuration.util.LazyInit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import static cloud.grabsky.configuration.util.LazyInit.notNull;
import static org.jetbrains.annotations.ApiStatus.Internal;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnchantmentEntry {

    @Getter(AccessLevel.PUBLIC)
    private final @NotNull Enchantment enchantment;

    @Getter(AccessLevel.PUBLIC)
    private final int level;

    /* LAZY INITIALIZER / BUILDER */

    @Internal @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static final class Init implements LazyInit<EnchantmentEntry> {

        public Enchantment enchantment;
        public Integer level;

        @Override
        public EnchantmentEntry init() throws IllegalStateException {
            return new EnchantmentEntry(
                    notNull(enchantment, "enchantment", Enchantment.class),
                    notNull(level, "enchantment", Integer.class)
            );
        }

    }

}
