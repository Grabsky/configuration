/*
 * MIT License
 *
 * Copyright (c) 2023 Grabsky
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
package grabsky.configuration.paper;

import com.google.gson.*;
import grabsky.configuration.paper.exception.JsonFormatException;
import grabsky.configuration.paper.object.EnchantmentEntry;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Type;

/** Converts {@link NamespacedKey} {@code (key)} and {@link Integer} {@code (level)} to {@link Enchantment}. */
public final class EnchantmentEntrySerializer implements JsonDeserializer<EnchantmentEntry> {

    /** Default instance of {@link EnchantmentSerializer}. */
    public static final EnchantmentEntrySerializer INSTANCE = new EnchantmentEntrySerializer();

    private EnchantmentEntrySerializer() { /* INSTANTIATING NOT ALLOWED */ }

    @Override
    public EnchantmentEntry deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        if (element instanceof JsonObject json) {
            final Enchantment enchantment = context.deserialize(json.get("key"), Enchantment.class);
            final int level = json.get("level").getAsInt();
            return new EnchantmentEntry(enchantment, level);
        }
        throw new JsonFormatException(type, element);
    }

}