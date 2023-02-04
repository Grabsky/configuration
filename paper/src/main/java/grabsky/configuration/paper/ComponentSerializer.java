/*
 * MIT License
 *
 * Copyright (c) 2023 Grabsky
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * HORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

    /**
     * Default instance of {@link ComponentSerializer} uses {@link MiniMessage#miniMessage()} as a conversion function.
     * Make sure to call {@link GsonBuilder#disableHtmlEscaping()} when creating a {@link Gson} instance. MiniMessage tags won't work otherwise.
     */
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
