package grabsky.configuration.paper;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

/** Converts {@link String} or {@link List List&lt;String&gt;} to {@link Component} using provided function. */
public final class ComponentSerializer implements JsonDeserializer<Component> {

    /** Default instance of {@link ComponentSerializer} uses {@link MiniMessage#miniMessage()} as a conversion function. */
    public static final ComponentSerializer DEFAULT = new ComponentSerializer(
            (str) -> MiniMessage.miniMessage().deserialize(str),
            (list) -> String.join("<newline><reset>", list)
    );

    private final Function<String, @Nullable Component> toComponent;
    private final Function<List<String>, @Nullable String> toJoinedString;

    public ComponentSerializer(final Function<String, @Nullable Component> toComponent, final Function<List<String>, String> toJoinedString) {
        this.toComponent = toComponent;
        this.toJoinedString = toJoinedString;
    }

    @Override
    public Component deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        // Handling JsonPrimitive
        if (element instanceof JsonPrimitive json) {
            final String richString = json.getAsString();
            // Returning empty component if value is null or blank
            if ("".equals(richString) == true)
                return Component.empty();
            // Parsing and returning
            return toComponent.apply(richString);

        // Handling JsonArray; ["One", "Two"] becomes "One<newline><reset>Two" (by default)
        } else if (element instanceof JsonArray json) {
            final List<String> richStrings = context.deserialize(json, TypeToken.getParameterized(List.class, String.class).getType());
            // Returning empty component if list is null or empty
            if (richStrings == null || richStrings.isEmpty() == true)
                return Component.empty();
            // Parsing and returning
            return toComponent.apply(toJoinedString.apply(richStrings));
        }
        // Neither JsonArray or JsonPrimitive? It's definitely not a String then!
        return Component.empty();
    }

}
