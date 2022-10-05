package grabsky.configuration.serializers;

import grabsky.configuration.exceptions.SerializationFailedException;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

import static grabsky.configuration.helpers.Preconditions.requirePresent;

public final class NamespacedKeySerializer implements TypeSerializer<NamespacedKey> {
    /* Singleton. */ public static NamespacedKeySerializer INSTANCE = new NamespacedKeySerializer();

    private NamespacedKeySerializer() { /* INSTANCING NOT ALLOWED */ }

    @Override
    public NamespacedKey deserialize(final Type type, @NotNull final ConfigurationNode node) throws SerializationException {
        // Getting the value or throwing exception when null.
        final String value = requirePresent(node.getString(), new SerializationFailedException(String.class, node, node.rawScalar()));
        // Returning NamespacedKey from obtained value or throwing exception if object creation failed.
        return requirePresent(NamespacedKey.fromString(value), new SerializationFailedException(NamespacedKey.class, node, value));
    }

    @Override
    public void serialize(final Type type, @Nullable final NamespacedKey obj, final ConfigurationNode node) { /* NOT SUPPORTED */ }

}
