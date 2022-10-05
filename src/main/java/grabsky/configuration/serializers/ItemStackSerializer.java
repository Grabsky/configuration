package grabsky.configuration.serializers;

import grabsky.configuration.helpers.ItemFactory;
import grabsky.configuration.objects.ItemEnchantment;
import grabsky.configuration.objects.PersistentDataEntry;
import grabsky.configuration.exceptions.SerializationFailedException;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

import static grabsky.configuration.helpers.Preconditions.requirePresent;

public final class ItemStackSerializer implements TypeSerializer<ItemStack> {
    /* Singleton. */ public static ItemStackSerializer INSTANCE = new ItemStackSerializer();

    private ItemStackSerializer() { /* INSTANCING NOT ALLOWED */ }

    @Override
    public ItemStack deserialize(final Type type, @NotNull final ConfigurationNode node) throws SerializationException {
        final Material material = requirePresent(node.node("material").get(Material.class), new SerializationFailedException(Material.class, node, null));
        // Constructing and returnign Itemstack object
        return ItemFactory.create(
                material,
                node.node("amount").getInt(1),
                // Common Meta
                node.node("meta", "name").get(Component.class),
                node.node("meta", "lore").getList(Component.class),
                node.node("meta", "custom_model_data").get(Integer.class),
                node.node("meta", "enchantments").getList(ItemEnchantment.class),
                node.node("meta", "flags").getList(ItemFlag.class),
                node.node("meta", "persistent_data_container").getList(PersistentDataEntry.class),
                // Enchantment Storage Meta
                node.node("meta", "enchantment_storage").getList(ItemEnchantment.class),
                // Damageable Meta
                node.node("meta", "durability").get(Integer.class),
                // Skull Meta
                node.node("meta", "skull_texture").getString(),
                // Spawner Meta
                node.node("meta", "spawner_type").get(EntityType.class),
                node.node("meta", "spawner_activation_range").get(Integer.class),
                node.node("meta", "spawner_min_spawn_delay").get(Integer.class),
                node.node("meta", "spawner_max_spawn_delay").get(Integer.class),
                node.node("meta", "spawner_max_nearby_entities").get(Integer.class),
                node.node("meta", "spawner_spawn_range").get(Integer.class),
                node.node("meta", "spawner_spawn_count").get(Integer.class)
        );
    }

    @Override
    public void serialize(final Type type, @Nullable final ItemStack obj, final ConfigurationNode node) { /* NOT SUPPORTED */ }
}
