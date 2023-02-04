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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import grabsky.configuration.paper.exception.JsonFormatException;
import grabsky.configuration.paper.object.EnchantmentEntry;
import grabsky.configuration.paper.object.PersistentDataEntry;
import grabsky.configuration.paper.util.BukkitHelpers;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.List;

/** Used to create {@link ItemStack}. */
public final class ItemStackSerializer implements JsonDeserializer<ItemStack> {

    /** Default instance of {@link ItemStackSerializer}. */
    public static final ItemStackSerializer INSTANCE = new ItemStackSerializer();

    private static final Type LIST_COMPONENT = TypeToken.getParameterized(List.class, Component.class).getType();
    private static final Type LIST_ENCHANTMENT_ENTRY = TypeToken.getParameterized(List.class, EnchantmentEntry.class).getType();
    private static final Type LIST_ITEM_FLAG = TypeToken.getParameterized(List.class, ItemFlag.class).getType();
    private static final Type LIST_PDE = TypeToken.getParameterized(List.class, PersistentDataEntry.class).getType();

    private ItemStackSerializer() { /* INSTANTIATING NOT ALLOWED */ }

    @Override
    public ItemStack deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        if (element instanceof JsonObject json) {
            // Getting required ItemStack properties (material, amount)
            final Material material = context.deserialize(json.get("material"), Material.class);
            final int amount = (json.get("amount") != null) ? json.get("amount").getAsInt() : 1;
            // Getting optional ItemStack properties (meta)
            if (json.get("meta") instanceof JsonObject jsonMeta) {
                return BukkitHelpers.createItemStack(material, amount, // <- required

                        (jsonMeta.get("name") instanceof JsonPrimitive raw)
                                ? context.deserialize(raw, Component.class)
                                : null,

                        (jsonMeta.get("lore") instanceof JsonArray raw)
                                ? context.deserialize(raw, LIST_COMPONENT)
                                : null,

                        (jsonMeta.get("custom_model_data") instanceof JsonPrimitive raw)
                                ? raw.getAsInt()
                                : null,

                        (jsonMeta.get("enchantments") instanceof JsonArray raw)
                                ? context.deserialize(raw, LIST_ENCHANTMENT_ENTRY)
                                : null,

                        (jsonMeta.get("flags") instanceof JsonArray raw)
                                ? context.deserialize(raw, LIST_ITEM_FLAG)
                                : null,

                        (jsonMeta.get("persistent_data_container") instanceof JsonArray raw)
                                ? context.deserialize(raw, LIST_PDE)
                                : null,

                        (jsonMeta.get("enchantment_storage") instanceof JsonArray raw)
                                ? context.deserialize(raw, LIST_ENCHANTMENT_ENTRY)
                                : null,

                        (jsonMeta.get("durability") instanceof JsonPrimitive raw)
                                ? raw.getAsInt()
                                : null,

                        (jsonMeta.get("skull_texture") instanceof JsonPrimitive raw)
                                ? raw.getAsString()
                                : null,

                        (jsonMeta.get("spawner_type") instanceof JsonPrimitive raw)
                                ? context.deserialize(raw, EntityType.class)
                                : null,

                        (jsonMeta.get("spawner_activation_range") instanceof JsonPrimitive raw)
                                ? raw.getAsInt()
                                : null,

                        (jsonMeta.get("spawner_min_spawn_delay") instanceof JsonPrimitive raw)
                                ? raw.getAsInt()
                                : null,

                        (jsonMeta.get("spawner_max_spawn_delay") instanceof JsonPrimitive raw)
                                ? raw.getAsInt()
                                : null,

                        (jsonMeta.get("spawner_max_nearby_entities") instanceof JsonPrimitive raw)
                                ? raw.getAsInt()
                                : null,

                        (jsonMeta.get("spawner_spawn_range") instanceof JsonPrimitive raw)
                                ? raw.getAsInt()
                                : null,

                        (jsonMeta.get("spawner_spawn_count") instanceof JsonPrimitive raw)
                                ? raw.getAsInt()
                                : null
                );
            }
            // Meta object not present; normal ItemStack returned
            return new ItemStack(material, amount);
        }
        // Not a JsonObject so definitely not a valid ItemStack, right?
        throw new JsonFormatException(type, element);
    }

}