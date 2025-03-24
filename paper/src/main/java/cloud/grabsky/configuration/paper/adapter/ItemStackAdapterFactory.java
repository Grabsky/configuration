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
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.RegistrySet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.val;

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
                // Reading ItemStackSurrogate from JSON.
                final @Nullable ItemStackSurrogate surrogate = adapter.nullSafe().fromJson(in);
                // Throwing exception if surrogate is null.
                if (surrogate == null)
                    throw new JsonDataException("Invalid ItemStack definition found at " + in.getPath() + " path.");
                // Initializing the surrogate.
                return surrogate.init();
            }

            @Override
            public void toJson(final @NotNull JsonWriter out, final ItemStack value) {
                throw new UnsupportedOperationException("NOT_IMPLEMENTED");
            }

        };
    }

    @Internal
    @SuppressWarnings("UnstableApiUsage")
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

        @Json(name = "stored_enchantments")
        private final @NotNull EnchantmentEntry @Nullable [] storedEnchantments;

        @Json(name = "persistent_data_container")
        private final @NotNull PersistentDataEntry @Nullable [] persistentDataEntries;

        @Json(name = "skull_texture")
        private final @Nullable String skullTexture;

        @Json(name = "durability")
        private final @Nullable Integer durability;

        @Json(name = "food")
        private final @Nullable FoodComponentContainer food;

        @Json(name = "consumable")
        private final @Nullable ConsumableComponentContainer consumable;

        @Json(name = "equippable")
        private final @Nullable EquippableComponentContainer equippable;

        @Json(name = "note_block_sound")
        private final @Nullable NamespacedKey noteBlockSound;

        @Json(name = "max_stack_size")
        private final @Nullable Integer maxStackSize;

        @Json(name = "max_damage")
        private final @Nullable Integer maxDamage;

        @Json(name = "enchantment_glint_override")
        private final @Nullable Boolean enchantmentGlintOverride;

        @Json(name = "components")
        private final @Nullable String components;

        @SuppressWarnings("unchecked")
        public @NotNull ItemMeta init(final @NotNull ItemStack item) {
            // Getting the type of the item. Can be null if the material is not a valid item.
            final @Nullable ItemType type = item.getType().asItemType();

            // Throwing IllegalArgumentException when the type is null.
            if (type == null)
                throw new IllegalArgumentException("Specified ItemStack is not a valid item.");

            // minecraft:item_name
            if (itemName != null)
                item.setData(DataComponentTypes.ITEM_NAME, itemName);

            // minecraft:custom_name
            if (customName != null)
                item.setData(DataComponentTypes.CUSTOM_NAME, empty().decoration(TextDecoration.ITALIC, false).append(customName));

            // minecraft:note_block_sound
            if (noteBlockSound != null)
                item.setData(DataComponentTypes.NOTE_BLOCK_SOUND, noteBlockSound);

            // minecraft:rarity
            if (rarity != null)
                item.setData(DataComponentTypes.RARITY, rarity);

            // minecraft:item_model
            if (model != null)
                item.setData(DataComponentTypes.ITEM_MODEL, model);

            // minecraft:max_stack_size
            if (maxStackSize != null)
                item.setData(DataComponentTypes.MAX_STACK_SIZE, maxStackSize);

            // minecraft:max_damage
            if (maxDamage != null)
                item.setData(DataComponentTypes.MAX_DAMAGE, maxDamage);

            // minecraft:enchantment_glint_override
            if (enchantmentGlintOverride != null)
                item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, enchantmentGlintOverride);

            // minecraft:durability
            if (durability != null)
                item.setData(DataComponentTypes.DAMAGE, item.getType().getMaxDurability() - Math.max(0, durability));

            // minecraft:food
            if (food != null) {
                final var component = (type.hasDefaultData(DataComponentTypes.FOOD) == true) ? type.getDefaultData(DataComponentTypes.FOOD).toBuilder() : FoodProperties.food();
                // Overriding values.
                if (food.nutrition != null)
                    component.nutrition(food.nutrition);
                if (food.saturation != null)
                    component.saturation(food.saturation);
                if (food.canAlwaysEat != null)
                    component.canAlwaysEat(food.canAlwaysEat);
                // Setting the component.
                item.setData(DataComponentTypes.FOOD, component);
            }

            // minecraft:consumable
            if (consumable != null) {
                final var component = (type.hasDefaultData(DataComponentTypes.CONSUMABLE) == true) ? type.getDefaultData(DataComponentTypes.CONSUMABLE).toBuilder() : Consumable.consumable();
                // Overriding values.
                if (consumable.consume_seconds != null)
                    component.consumeSeconds(consumable.consume_seconds);
                if (consumable.animation != null)
                    component.animation(ItemUseAnimation.valueOf(consumable.animation.toUpperCase()));
                if (consumable.sound != null)
                    component.sound(consumable.sound);
                if (consumable.hasConsumeParticles != null)
                    component.hasConsumeParticles(consumable.hasConsumeParticles);
                // Setting the component.
                item.setData(DataComponentTypes.CONSUMABLE, component);
            }

            // minecraft:equippable
            if (equippable != null) {
                final var component = (type.hasDefaultData(DataComponentTypes.EQUIPPABLE) == true) ? type.getDefaultData(DataComponentTypes.EQUIPPABLE).toBuilder() : Equippable.equippable(equippable.slot);
                // Overriding values.
                if (equippable.equipSound != null)
                    component.equipSound(equippable.equipSound);
                if (equippable.assetId != null)
                    component.assetId(equippable.assetId);
                if (equippable.allowedEntities != null)
                    component.allowedEntities(RegistrySet.keySetFromValues(RegistryKey.ENTITY_TYPE, equippable.allowedEntities));
                if (equippable.dispensable != null)
                    component.dispensable(equippable.dispensable);
                if (equippable.swappable != null)
                    component.swappable(equippable.swappable);
                if (equippable.damageOnHurt != null)
                    component.damageOnHurt(equippable.damageOnHurt);
                if (equippable.cameraOverlay != null)
                    component.cameraOverlay(equippable.cameraOverlay);
                // Setting the component.
                item.setData(DataComponentTypes.EQUIPPABLE, component);
            }

            // minecraft:profile
            if (skullTexture != null) {
                // Setting textures based on specified value.
                final String textures = (skullTexture.startsWith("http") == true)
                        ? Base64.getEncoder().encodeToString(
                                String.format("""
                                    {
                                        "textures": {
                                            "SKIN": { "url": "%s" }
                                        }
                                    }
                                    """, skullTexture).trim().getBytes())
                        : skullTexture;
                // Setting the component.
                item.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile()
                        .name(null)
                        .uuid(EMPTY_UUID)
                        .addProperty(new ProfileProperty("textures", textures))
                );
            }

            // Getting and modifying the ItemMeta.
            final ItemMeta meta = item.getItemMeta();

            if (customModelData != null)
                meta.setCustomModelData(customModelData);

            if (enchantments != null)
                for (var entry : enchantments)
                    meta.addEnchant(entry.getEnchantment(), entry.getLevel(), true);

            if (storedEnchantments != null) {
                final EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) meta;
                for (var entry : storedEnchantments)
                    enchantmentStorageMeta.addStoredEnchant(entry.getEnchantment(), entry.getLevel(), true);
            }

            if (persistentDataEntries != null) {
                final PersistentDataContainer container = meta.getPersistentDataContainer();
                for (final var entry : persistentDataEntries)
                    container.set(entry.getKey(), (PersistentDataType<?, Object>) entry.getType(), entry.getValue()); // Cast should be safe assuming serialization was successful
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


    @Internal @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class FoodComponentContainer {

        @Json(name = "nutrition")
        private final @Nullable Integer nutrition;

        @Json(name = "saturation")
        private final @Nullable Float saturation;

        @Json(name = "can_always_eat")
        private final @Nullable Boolean canAlwaysEat;

    }

    @Internal @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class ConsumableComponentContainer {

        @Json(name = "consume_seconds")
        private final @Nullable Float consume_seconds;

        @Json(name = "animation")
        private final @Nullable String animation;

        @Json(name = "sound")
        private final @Nullable NamespacedKey sound;

        @Json(name = "has_consume_particles")
        private final @Nullable Boolean hasConsumeParticles;

        // TO-DO: on_consume_effects

    }

    @Internal @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class EquippableComponentContainer {

        @Json(name = "slot")
        private final @NotNull EquipmentSlot slot;

        @Json(name = "equip_sound")
        private final @Nullable NamespacedKey equipSound;

        @Json(name = "asset_id")
        private final @Nullable NamespacedKey assetId;

        @Json(name = "allowed_entities")
        private final @Nullable List<EntityType> allowedEntities;

        @Json(name = "dispensable")
        private final @Nullable Boolean dispensable;

        @Json(name = "swappable")
        private final @Nullable Boolean swappable;

        @Json(name = "damage_on_hurt")
        private final @Nullable Boolean damageOnHurt;

        @Json(name = "camera_overlay")
        private final @Nullable NamespacedKey cameraOverlay;

    }

}
