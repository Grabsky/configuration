package grabsky.configuration.paper.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import grabsky.configuration.paper.object.EnchantmentEntry;
import grabsky.configuration.paper.object.PersistentDataEntry;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public final class BukkitHelpers {
    private static final UUID EMPTY_UUID = UUID.nameUUIDFromBytes(new byte[0]);

    @SuppressWarnings("unchecked cast")
    public static @NotNull ItemStack createItemStack(
            @NotNull final Material material,
            @NotNull final Integer amount,
            // Common Meta
            @Nullable final Component name,
            @Nullable final List<Component> lore,
            @Nullable final Integer customModelData,
            @Nullable final List<EnchantmentEntry> enchantments,
            @Nullable final List<ItemFlag> itemFlags,
            // Persistent Data Container
            @Nullable final List<PersistentDataEntry> persistentDataEntries,
            // Enchantment Storage Meta
            @Nullable final List<EnchantmentEntry> enchantmentStorage,
            // Damageable Meta
            @Nullable final Integer durability,
            // Skull Meta
            @Nullable final String skullTexture,
            // Sapwner Meta
            @Nullable final EntityType spawnerEntityType,
            @Nullable final Integer spawnerActivationRange,
            @Nullable final Integer spawnerMinSpawnDelay,
            @Nullable final Integer spawnerMaxSpawnDelay,
            @Nullable final Integer spawnerMaxNearbyEntities,
            @Nullable final Integer spawnerSpawnRange,
            @Nullable final Integer spawnerSpawnCount
            // TO-DO: Enchanted Books, Fireworks
    ) {
        final ItemStack item = new ItemStack(material, amount);
        // Doing item meta stuff...
        item.editMeta(meta -> {
            // Setting display name if present...
            if (name != null)
                meta.displayName(name);

            // Setting lore if present...
            if (lore != null)
                meta.lore(lore);

            // Setting custom model data if present...
            if (customModelData != null)
                meta.setCustomModelData(customModelData);

            // Setting enchantments if present...
            if (enchantments != null)
                enchantments.forEach((e) -> meta.addEnchant(e.getEnchantment(), e.getLevel(), true));

            // Setting item flags if present...
            if (itemFlags != null)
                meta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));

            // Setting persistent data container if present...
            if (persistentDataEntries != null) {
                final PersistentDataContainer container = meta.getPersistentDataContainer();
                persistentDataEntries.forEach((entry) -> {
                    container.set(entry.getKey(), (PersistentDataType<?, Object>) entry.getPersistentDataType(), entry.getValue()); // Should be safe assuming serialization was successful
                });
            }

            // Setting enchantment storage meta if present...
            if (meta instanceof EnchantmentStorageMeta enchantmentStorageMeta && enchantmentStorage != null) {
                enchantmentStorage.forEach((e) -> enchantmentStorageMeta.addStoredEnchant(e.getEnchantment(), e.getLevel(), true));
            }

            // Setting durability if applicable and present...
            if (meta instanceof Damageable damageable && durability != null && durability > -1)
                damageable.setDamage(material.getMaxDurability() - durability);

            // Setting skull texture if applicable and present...
            if (meta instanceof SkullMeta skullMeta && skullTexture != null) {
                // Creating a 'fake' player profile
                final PlayerProfile profile = Bukkit.createProfile(EMPTY_UUID);
                // Setting 'textures' property
                profile.setProperty(new ProfileProperty("textures", skullTexture));
                // Setting player profile in skull meta
                skullMeta.setPlayerProfile(profile);
            }

            // Setting spawner meta if applicable and present...
            if (meta instanceof BlockStateMeta blockState && blockState instanceof CreatureSpawner creatureSpawner) {
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
