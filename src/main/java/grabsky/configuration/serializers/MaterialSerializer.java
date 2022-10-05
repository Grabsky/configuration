package grabsky.configuration.serializers;

import grabsky.configuration.exceptions.SerializationFailedException;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.function.Function;

import static grabsky.configuration.helpers.Preconditions.requirePresent;

public final class MaterialSerializer implements TypeSerializer<Material> {
    /* Singleton. */ public static final MaterialSerializer INSTANCE = new MaterialSerializer();

    private final Function<NamespacedKey, @Nullable Material> toMaterial;

    // INTERNAL USE ONLY
    private MaterialSerializer() {
        this.toMaterial = Registry.MATERIAL::get;
    }

    public MaterialSerializer(final Function<NamespacedKey, @Nullable Material> toMaterial) {
        this.toMaterial = toMaterial;
    }

    @Override
    public Material deserialize(final Type type, @NotNull final ConfigurationNode node) throws SerializationException {
        // Getting value as NamespacedKey if possible, throwing exception otherwise.
        final NamespacedKey key = requirePresent(node.get(NamespacedKey.class), new SerializationFailedException(NamespacedKey.class, node, null));
        // Returning Material if present, throwing exception otherwise.
        return requirePresent(toMaterial.apply(key), new SerializationFailedException(Material.class, node, key.asString()));
    }

    @Override
    public void serialize(final Type type, @Nullable final Material obj, final ConfigurationNode node) { /* NOT SUPPORTED */ }

}
