package grabsky.configuration.exceptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Arrays;

/** ...TO-DO... */
public final class SerializationFailedException extends SerializationException {

    public SerializationFailedException(@NotNull final Class<?> expectedType, @NotNull final ConfigurationNode node, @Nullable final Object value) {
        super("Could not deserialize '" + value + "' to " + expectedType.getName() + " at '" + Arrays.toString(node.path().array()) + "'.");
    }

}
