package grabsky.configuration.serializers;

import grabsky.configuration.exceptions.SerializationFailedException;
import net.kyori.adventure.sound.Sound;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

import static grabsky.configuration.helpers.Preconditions.requirePresent;

public final class SoundSerializer implements TypeSerializer<Sound> {
    /* Singleton. */ public static SoundSerializer INSTANCE = new SoundSerializer();

    private SoundSerializer() { /* INSTANCING NOT ALLOWED */ }

    @Override
    public Sound deserialize(final Type type, @NotNull final ConfigurationNode node) throws SerializationException {
        // Getting value as NamespacedKey if possible, throwing exception otherwise.
        final NamespacedKey key = requirePresent(node.node("key").get(NamespacedKey.class), new SerializationFailedException(NamespacedKey.class, node, null));
        // ...
        final Sound.Source source = node.node("source").get(Sound.Source.class, Sound.Source.MASTER);
        final float volume = node.node("volume").getFloat(1F);
        final float pitch = node.node("pitch").getFloat(1F);
        // Returning Material if present, throwing exception otherwise.
        return Sound.sound(key, source, volume, pitch);
    }

    @Override
    public void serialize(final Type type, @Nullable final Sound obj, final ConfigurationNode node) { /* NOT SUPPORTED */ }

}
