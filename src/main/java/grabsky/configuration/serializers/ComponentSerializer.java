package grabsky.configuration.serializers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

public final class ComponentSerializer implements TypeSerializer<Component> {
    /* Singleton. */ public static final ComponentSerializer INSTANCE = new ComponentSerializer();

    private final Function<String, @Nullable Component> toComponent;

    // INTERNAL USE ONLY
    private ComponentSerializer() {
        this.toComponent = (val) -> (Component) MiniMessage.miniMessage().deserialize(val);
    }

    public ComponentSerializer(final Function<String, @Nullable Component> toComponent) {
        this.toComponent = toComponent;
    }

    @Override
    public Component deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        // Returning empty component if configuration node doesn't exist OR is not a valid string
        if (node == null) return Component.empty();
        // Checking if node is a list
        if (node.isList() == true) {
            final List<String> richStrings = node.getList(String.class);
            // Returning empty component if list is null or empty
            if (richStrings == null || richStrings.isEmpty() == true) return Component.empty();
            // Parsing and returning
            return toComponent.apply(String.join("<newline><reset>", richStrings));
        }
        final String richString = node.get(String.class);
        // Returning empty component if value is null or blank
        if (richString == null || richString.equals("")) return Component.empty();
        // Parsing and returning
        return toComponent.apply(richString);
    }

    @Override
    public void serialize(final Type type, @Nullable final Component obj, final ConfigurationNode node) { /* NOT SUPPORTED */ }
}