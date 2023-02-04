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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import grabsky.configuration.paper.exception.JsonFormatException;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Type;

import static grabsky.configuration.paper.util.Conditions.requirePresent;

/** Converts {@link NamespacedKey} to {@link Enchantment}. */
public final class EnchantmentSerializer implements JsonDeserializer<Enchantment> {

    /** Default instance of {@link EnchantmentSerializer}. */
    public static final EnchantmentSerializer INSTANCE = new EnchantmentSerializer();

    private EnchantmentSerializer() { /* INSTANTIATING NOT ALLOWED */ }

    @Override
    public Enchantment deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        final NamespacedKey key = context.deserialize(element, NamespacedKey.class);
        // Throwing an exception in case JsonElement is not a JsonPrimitive, therefore definitely not a valid String.
        return requirePresent(Registry.ENCHANTMENT.get(key), new JsonFormatException(type, element));
    }

}
