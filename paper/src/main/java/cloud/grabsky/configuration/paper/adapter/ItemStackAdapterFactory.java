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
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static cloud.grabsky.configuration.util.LazyInit.notNull;
import static com.squareup.moshi.Types.getRawType;

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
        final var adapter0 = moshi.adapter(Material.class);
        final var adapter1 = moshi.adapter(Component.class);
        final var adapter2 = moshi.adapter(EntityType.class);
        final var adapter3 = moshi.adapter(ItemFlag[].class);
        final var adapter4 = moshi.adapter(Component[].class);
        final var adapter5 = moshi.adapter(EnchantmentEntry[].class);
        final var adapter6 = moshi.adapter(PersistentDataEntry[].class);
        // ...
        return new JsonAdapter<>() {

            @Override
            public ItemStack fromJson(final @NotNull JsonReader in) throws IOException {
                in.beginObject();
                // Creating ItemStack lazy initializer
                final ItemStackInit initializer = new ItemStackInit();
                // Iterating over properties at $.*
                while (in.hasNext() == true) {
                    final String nextName = in.nextName();
                    switch (nextName) {
                        case "material" -> initializer.material = adapter0.fromJson(in);
                        case "amount" -> initializer.amount = in.nextInt();
                        case "meta" -> {
                            in.beginObject();
                            // Iterating over properties at $.meta.*
                            while (in.hasNext() == true) {
                                final String nextNextName = in.nextName();
                                switch (nextNextName) {
                                    case "name" -> initializer.name = adapter1.fromJson(in);
                                    case "lore" -> initializer.lore = adapter4.fromJson(in);
                                    case "custom_model_data" -> initializer.customModelData = in.nextInt();
                                    case "item_flags" -> initializer.itemFlags = adapter3.fromJson(in);
                                    case "enchantments" -> initializer.enchantments = adapter5.fromJson(in);
                                    case "enchantment_storage" -> initializer.enchantmentStorage = adapter5.fromJson(in);
                                    case "durability" -> initializer.durability = in.nextInt();
                                    case "skull_texture" -> initializer.skullTexture = in.nextString();
                                    case "persistent_data_container" -> initializer.persistentDataEntries = adapter6.fromJson(in);
                                    case "spawner_entity_type" -> initializer.spawnerEntityType = adapter2.fromJson(in);
                                    case "spawner_activation_range" -> initializer.spawnerActivationRange = in.nextInt();
                                    case "spawner_min_spawn_delay" -> initializer.spawnerMinSpawnDelay = in.nextInt();
                                    case "spawner_max_spawn_delay" -> initializer.spawnerMaxSpawnDelay = in.nextInt();
                                    case "spawner_max_nearby_entities" -> initializer.spawnerMaxNearbyEntities = in.nextInt();
                                    case "spawner_spawn_range" -> initializer.spawnerSpawnRange = in.nextInt();
                                    case "spawner_spawn_count" -> initializer.spawnerSpawnCount = in.nextInt();
                                    default -> throw new JsonDataException("Unexpected field at " + in.getPath() + ": " + nextNextName);
                                }
                            }
                            in.endObject();
                        }
                        default -> throw new JsonDataException("Unexpected field at " + in.getPath() + ": " + nextName);
                    }
                }
                in.endObject();
                // ...
                return initializer.init();
            }

            @Override
            public void toJson(final @NotNull JsonWriter out, final ItemStack value) {
                throw new UnsupportedOperationException("NOT IMPLEMENTED");
            }

        };
    }

    @Internal @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class ItemStackInit implements LazyInit<ItemStack> {
        // for ItemStack
        public Material material;
        public Integer amount;
        // for ItemStack > ItemMeta
        public Component name;
        public Component[] lore;
        public Integer customModelData;
        public ItemFlag[] itemFlags;
        public EnchantmentEntry[] enchantments;
        public PersistentDataEntry[] persistentDataEntries;
        // for ItemStack > ItemMeta > Damageable
        public Integer durability;
        // for ItemStack > ItemMeta > EnchantmentStorageMeta
        public EnchantmentEntry[] enchantmentStorage;
        // for ItemStack > ItemMeta > SkullMeta
        public String skullTexture;
        // for ItemStack > ItemMeta > BlockStateMeta > CreatureSpawner
        public EntityType spawnerEntityType;
        public Integer spawnerActivationRange;
        public Integer spawnerMinSpawnDelay;
        public Integer spawnerMaxSpawnDelay;
        public Integer spawnerMaxNearbyEntities;
        public Integer spawnerSpawnRange;
        public Integer spawnerSpawnCount;

        private static final UUID EMPTY_UUID = UUID.nameUUIDFromBytes(new byte[0]);

        @Override @SuppressWarnings("unchecked")
        public ItemStack init() throws IllegalStateException {
            // for ItemStack
            final ItemStack item = new ItemStack(
                    notNull(material, "material", Material.class),
                    notNull(amount, "amount", Integer.class)
            );
            // for ItemStack > ItemMeta
            item.editMeta((meta) -> {
                if (name != null)
                    meta.displayName(name);

                if (lore != null)
                    meta.lore(List.of(lore));

                if (customModelData != null)
                    meta.setCustomModelData(customModelData);

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

                // for ItemStack > ItemMeta > Damageable
                if (meta instanceof Damageable damageable && durability != null && durability > -1)
                    damageable.setDamage(material.getMaxDurability() - durability);


                // for ItemStack > ItemMeta > EnchantmentStorageMeta
                if (meta instanceof EnchantmentStorageMeta enchantmentStorageMeta && enchantmentStorage != null)
                    for (final var entry : enchantmentStorage)
                        enchantmentStorageMeta.addStoredEnchant(entry.getEnchantment(), entry.getLevel(), true);

                // for ItemStack > ItemMeta > SkullMeta
                if (meta instanceof SkullMeta skullMeta && skullTexture != null) {
                    final PlayerProfile profile = Bukkit.createProfile(EMPTY_UUID);
                    profile.setProperty(new ProfileProperty("textures", skullTexture));
                    // ...
                    skullMeta.setPlayerProfile(profile);
                }

                // for ItemStack > ItemMeta > BlockStateMeta > CreatureSpawner
                if (meta instanceof BlockStateMeta blockState && blockState.getBlockState() instanceof CreatureSpawner creatureSpawner) {
                    if (spawnerEntityType != null)
                        creatureSpawner.setSpawnedType(spawnerEntityType);
                    if (spawnerActivationRange != null)
                        creatureSpawner.setRequiredPlayerRange(spawnerActivationRange);
                    if (spawnerMinSpawnDelay != null)
                        creatureSpawner.setMinSpawnDelay(spawnerMinSpawnDelay);
                    if (spawnerMaxSpawnDelay != null)
                        creatureSpawner.setMaxSpawnDelay(spawnerMaxSpawnDelay);
                    if (spawnerMaxNearbyEntities != null)
                        creatureSpawner.setMaxNearbyEntities(spawnerMaxNearbyEntities);
                    if (spawnerSpawnRange != null)
                        creatureSpawner.setSpawnRange(spawnerSpawnRange);
                    if (spawnerSpawnCount != null)
                        creatureSpawner.setSpawnCount(spawnerSpawnCount);
                    // ...
                    blockState.setBlockState(creatureSpawner);
                }
            });
            // ...
            return item;
        }
    }

}
