package grabsky.configuration.serializers;

import grabsky.configuration.objects.PersistentDataEntry;
import grabsky.configuration.exceptions.SerializationFailedException;
import grabsky.configuration.registry.PersistentDataRegistry;
import grabsky.configuration.registry.Registry;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

import static grabsky.configuration.helpers.Preconditions.requirePresent;

public final class PersistentDataSerializer implements TypeSerializer<PersistentDataEntry> {
    /* Singleton. */ public static PersistentDataSerializer INSTANCE = new PersistentDataSerializer();
    public static final PersistentDataRegistry REGISTRY = PersistentDataRegistry.INSTANCE;

    private PersistentDataSerializer() { /* INSTANCING NOT ALLOWED */ }

    @Override
    public PersistentDataEntry deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final NamespacedKey key = requirePresent(node.node("key").get(NamespacedKey.class), new SerializationFailedException(NamespacedKey.class, node, null));
        final String dataTypeKey = requirePresent(node.node("type").getString(), new SerializationFailedException(String.class, node, null));
        final PersistentDataType<?, ?> dataType = requirePresent(REGISTRY.get(dataTypeKey), new SerializationFailedException(PersistentDataType.class, node, dataTypeKey));
        final Object value = requirePresent(node.node("value").get(dataType.getComplexType()), new SerializationFailedException(dataType.getComplexType(), node, node.node("value").rawScalar()));
        // ...
        return new PersistentDataEntry(key, dataType, value);
    }

    @Override
    public void serialize(final Type type, @Nullable final PersistentDataEntry obj, final ConfigurationNode node) { /* NOT SUPPORTED */ }
}
