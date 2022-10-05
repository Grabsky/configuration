package grabsky.configuration.serializers;

import grabsky.configuration.objects.ItemEnchantment;
import grabsky.configuration.exceptions.SerializationFailedException;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

import static grabsky.configuration.helpers.Preconditions.requirePresent;

public final class ItemEnchantmentSerializer implements TypeSerializer<ItemEnchantment> {
    /* Singleton. */ public static ItemEnchantmentSerializer INSTANCE = new ItemEnchantmentSerializer();

    private ItemEnchantmentSerializer() { /* INSTANCING NOT ALLOWED */ }

    @Override
    public ItemEnchantment deserialize(final Type type, @NotNull final ConfigurationNode node) throws SerializationException {
        // Getting the value or throwing exception when null.
        final Enchantment enchantment = requirePresent(node.node("key").get(Enchantment.class), new SerializationFailedException(Enchantment.class, node, null));
        // Returning ItemEnchantment from obtained value or throwing exception if object creation failed.
        return new ItemEnchantment(enchantment, node.node("level").getInt(1));
    }

    @Override
    public void serialize(final Type type, @Nullable final ItemEnchantment obj, final ConfigurationNode node) { /* NOT SUPPORTED */ }
}
