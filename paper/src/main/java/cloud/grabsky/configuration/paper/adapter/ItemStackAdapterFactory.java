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

import cloud.grabsky.configuration.paper.object.EnchantmentEntry;
import cloud.grabsky.configuration.paper.object.PersistentDataEntry;
import cloud.grabsky.configuration.util.LazyInit;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import static com.squareup.moshi.Types.getRawType;
import static net.kyori.adventure.text.Component.empty;

/**
 * Creates {@link JsonAdapter JsonAdapter&lt;ItemStack&gt;} which converts JSON object to {@link ItemStack}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemStackAdapterFactory implements JsonAdapter.Factory {
    /* SINGLETON */ public static final ItemStackAdapterFactory INSTANCE = new ItemStackAdapterFactory();

    @Override
    public @Nullable JsonAdapter<ItemStack> create(final @NotNull Type type, final @NotNull Set<? extends Annotation> annotations, final @NotNull Moshi moshi) {
        if (ItemStack.class.isAssignableFrom(getRawType(type)) == false)
            return null;
        // ...
        final var adapter = moshi.adapter(ItemStackSurrogate.class).nullSafe();
        // ...
        return new JsonAdapter<>() {

            @Override
            public ItemStack fromJson(final @NotNull JsonReader in) throws IOException {
                // Creating ItemStack lazy initializer
                final @Nullable ItemStackSurrogate surrogate = adapter.nullSafe().fromJson(in);
                // ...
                if (surrogate == null)
                    throw new JsonDataException("something is wrong bro");
                // ...
                return surrogate.init();
            }

            @Override
            public void toJson(final @NotNull JsonWriter out, final ItemStack value) throws IOException {
                adapter.toJson(out, ItemStackSurrogate.fromItemStack(value));
            }

        };
    }

    @Internal
    @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
    private static final class ItemMetaSurrogate {

        private static final UUID EMPTY_UUID = UUID.nameUUIDFromBytes(new byte[0]);

        @Json(name = "name")
        private final @Nullable Component itemName;

        @Json(name = "custom_name")
        private final @Nullable Component customName;

        @Json(name = "lore")
        private final @NotNull Component @Nullable [] lore;

        @Json(name = "rarity")
        private final @Nullable ItemRarity rarity;

        @Json(name = "custom_model_data")
        private final @Nullable Integer customModelData;

        @Json(name = "model")
        private final @Nullable NamespacedKey model;

        @Json(name = "item_flags")
        private final @NotNull ItemFlag @Nullable [] itemFlags;

        @Json(name = "enchantments")
        private final @NotNull EnchantmentEntry @Nullable [] enchantments;

        @Json(name = "persistent_data_container")
        private final @NotNull PersistentDataEntry @Nullable [] persistentDataEntries;

        @Json(name = "skull_texture")
        private final @Nullable String skullTexture;

        @Json(name = "durability")
        private final @Nullable Integer durability;

        @Json(name = "food")
        private final @Nullable FoodComponentContainer food;

        @Json(name = "components")
        private final @Nullable String components;

        public static @Nullable ItemMetaSurrogate fromItemMeta(final @Nullable ItemMeta meta) {
            if (meta == null)
                return null;
            // ...
            return new ItemMetaSurrogate(
                    (meta.hasItemName() == true) ? meta.itemName() : null,
                    (meta.hasDisplayName() == true && meta.displayName() != null) ? meta.displayName() : null,
                    (meta.hasLore() == true && meta.lore() != null) ? meta.lore().toArray(new Component[0]) : null, // Should never throw NPE.
                    (meta.hasRarity() == true) ? meta.getRarity() : null,
                    (meta.hasCustomModelData() == true && meta.getCustomModelData() != 0) ? meta.getCustomModelData() : null,
                    (meta.hasItemModel() == true && meta.getItemModel() != null) ? meta.getItemModel() : null,
                    (meta.getItemFlags().isEmpty() == false) ? meta.getItemFlags().toArray(new ItemFlag[0]) : null,
                    (meta.hasEnchants() == true && meta.getEnchants().isEmpty() == false) ? (EnchantmentEntry[]) meta.getEnchants().entrySet().stream().map((e) -> new EnchantmentEntry.Init(e.getKey(), e.getValue()).init()).toArray() : null,
                    null, // NOT (YET) SUPPORTED; Complicated due to PDC not storing types.
                    null, // NOT (YET) SUPPORTED; Possible but need to double-check what value is expected there.
                    null, // NOT (YET) SUPPORTED; May need changing durability to damage as ItemMeta has no idea about the max durability.
                    (meta.hasFood() == true) ? new FoodComponentContainer(meta.getFood().getNutrition(), meta.getFood().getSaturation(), meta.getFood().canAlwaysEat()) : null,
                    null  // NOT SUPPORTED; Unlikely to be in the future.

            );
        }

        @SuppressWarnings("unchecked")
        public @NotNull ItemMeta init(final @NotNull ItemStack item) {
            final ItemMeta meta = item.getItemMeta();

            if (itemName != null)
                meta.itemName(itemName);

            if (customName != null)
                meta.displayName(empty().decoration(TextDecoration.ITALIC, false).append(customName));

            if (lore != null)
                meta.lore(Arrays.stream(lore).map(line -> (Component) empty().decoration(TextDecoration.ITALIC, false).append(line)).toList());

            if (rarity != null)
                meta.setRarity(rarity);

            if (customModelData != null)
                meta.setCustomModelData(customModelData);

            if (model != null)
                meta.setItemModel(model);

            if (itemFlags != null)
                meta.addItemFlags(itemFlags);

            if (enchantments != null)
                for (var entry : enchantments)
                    meta.addEnchant(entry.getEnchantment(), entry.getLevel(), true);

            if (persistentDataEntries != null) {
                final PersistentDataContainer container = meta.getPersistentDataContainer();
                for (final var entry : persistentDataEntries)
                    container.set(entry.getKey(), (PersistentDataType<?, Object>) entry.getType(), entry.getValue()); // Cast should be safe assuming serialization was successful
            }

            // for ItemStack > ItemMeta > SkullMeta
            if (skullTexture != null && meta instanceof SkullMeta skullMeta) {
                final PlayerProfile profile = Bukkit.createProfile(EMPTY_UUID);
                profile.setProperty(new ProfileProperty("textures", skullTexture));
                // ...
                skullMeta.setPlayerProfile(profile);
            }

            // for ItemStack > ItemMeta > Damageable
            if (durability != null && meta instanceof Damageable damageable)
                damageable.setDamage(item.getType().getMaxDurability() - Math.max(0, durability));

            // Food Component
            if (food != null) {
                final FoodComponent foodComponent = meta.getFood();
                // ...
                foodComponent.setNutrition(food.nutrition != null ? food.nutrition : (int) 0);
                foodComponent.setSaturation(food.saturation != null ? food.saturation : (float) 0);
                foodComponent.setCanAlwaysEat(food.canAlwaysEat);
                // ...
                meta.setFood(foodComponent);
            }

            // Returning...
            return meta;
        }
    }

    @Internal
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class ItemStackSurrogate implements LazyInit<ItemStack> {

        @Json(name = "material")
        private final @NotNull Material material;

        @Json(name = "amount")
        private final @Nullable Integer amount;

        @Json(name = "meta")
        private final @Nullable ItemMetaSurrogate meta;

        public static @NotNull ItemStackSurrogate fromItemStack(final @NotNull ItemStack item) {
            return new ItemStackSurrogate(
                    item.getType(),
                    item.getAmount(),
                    (item.hasItemMeta() == true && item.getItemMeta() != null) ? ItemMetaSurrogate.fromItemMeta(item.getItemMeta()) : null
            );
        }

        @Override
        public ItemStack init() {
            final ItemStack item = new ItemStack(material, (amount != null) ? amount : 1);
            // ...
            if (meta != null) {
                // Applying components if specified.
                if (meta.components != null)
                    Bukkit.getUnsafe().modifyItemStack(item, item.getType().getKey().asString() + meta.components);
                // Applying other meta.
                item.setItemMeta(meta.init(item));

            }
            // ...
            return item;
        }
    }


    @Internal
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class FoodComponentContainer {

        @Json(name = "nutrition")
        private final @NotNull Integer nutrition;

        @Json(name = "saturation")
        private final @Nullable Float saturation;

        @Json(name = "can_always_eat")
        private boolean canAlwaysEat = false;

    }

}
