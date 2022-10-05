package grabsky.configuration.serializers;

import grabsky.configuration.exceptions.SerializationFailedException;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.function.Function;

import static grabsky.configuration.helpers.Preconditions.requirePresent;

public final class EntityTypeSerializer implements TypeSerializer<EntityType> {
    /* Singleton. */ public static final EntityTypeSerializer INSTANCE = new EntityTypeSerializer();

    private final Function<NamespacedKey, @Nullable EntityType> toEntityType;

    // INTERNAL USE ONLY
    private EntityTypeSerializer() {
        this.toEntityType = Registry.ENTITY_TYPE::get;
    }

    public EntityTypeSerializer(final Function<NamespacedKey, @Nullable EntityType> toEntityType) {
        this.toEntityType = toEntityType;
    }

    @Override
    public EntityType deserialize(final Type type, @NotNull final ConfigurationNode node) throws SerializationException {
        // Getting value as NamespacedKey if possible, throwing exception otherwise.
        final NamespacedKey key = requirePresent(node.get(NamespacedKey.class), new SerializationFailedException(NamespacedKey.class, node, null));
        // Returning EntityType if present, throwing exception otherwise.
        return requirePresent(toEntityType.apply(key), new SerializationFailedException(EntityType.class, node, key.asString()));
    }

    @Override
    public void serialize(final Type type, @Nullable final EntityType obj, final ConfigurationNode node) { /* NOT SUPPORTED */ }
}
