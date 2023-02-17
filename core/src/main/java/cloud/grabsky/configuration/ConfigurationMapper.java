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
package cloud.grabsky.configuration;

import cloud.grabsky.configuration.exception.ConfigurationException;
import cloud.grabsky.configuration.serializers.CaseInsensitiveEnumSerializer;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigurationMapper {

    private final Gson gson;

    /**
     * Creates {@link ConfigurationMapper} instance using provided {@link Gson} instance for re-mapping.
     *
     * @param gson {@link Gson} instance.
     * @return a new instance of {@link ConfigurationMapper}.
     */
    public static @NotNull ConfigurationMapper create(@NotNull final Gson gson) {
        return new ConfigurationMapper(gson);
    }

    /**
     * Maps file contents to {@code public}, {@code static}, {@code non-final} fields declared inside provided class.
     * When method fails due to {@link JsonParseException} - a new {@link ConfigurationException}
     * is thrown and fields of {@link T} remain unchanged.
     *
     * @param configurationClass class with fields to be replaced.
     * @param configurationFile {@link File} containing json configuration.
     * @throws ConfigurationException when configuration fails to load.
     */
    public <T extends Configuration> void map(@NotNull final Class<T> configurationClass, @NotNull final File configurationFile) throws ConfigurationException {
        try {
            // Converting JSON string (from a BufferedReader) to JsonElement
            final JsonElement root = parseFile(configurationFile);
            // Updating the fields and returning the result
            final FieldDataContainer container = this.collect(configurationClass, root);
            // Inserting collected values to provided class' fields
            this.insert(configurationClass, container);
            // Calling provided class' 'onReload' method
            this.call(configurationClass);
        } catch (final IOException | JsonParseException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException | InstantiationException error) {
            throw new ConfigurationException(configurationClass, configurationFile, error);
        }
    }

    /**
     * Maps contents of all files to {@code public}, {@code static}, {@code non-final} fields declared in relative classes.
     * When method fails due to {@link JsonParseException} - a new {@link ConfigurationException}
     * is thrown and <b><i>all</i></b> fields remain unchanged.
     *
     * @param holders vararg of {@link ConfigurationHolder} instances
     * @throws ConfigurationException when configuration fails to load.
     */
    public void map(@NotNull final ConfigurationHolder<? extends Configuration>... holders) throws ConfigurationException {
        final Map<ConfigurationHolder<?>, FieldDataContainer> configurations = new LinkedHashMap<>();
        // Step 1: Collecting values
        for (final ConfigurationHolder<?> holder : holders) {
            final Class<? extends Configuration> configurationClass = holder.getConfigurationClass();
            final File file = holder.getFile();
            // ...
            try {
                // Reading JsonElement from the file
                final JsonElement json = parseFile(file);
                // Parsing values and collecting them to FieldDataContainer
                final FieldDataContainer container = this.collect(configurationClass, json);
                // Adding container to the map
                configurations.put(holder, container);
            } catch (final IOException | JsonParseException | IllegalArgumentException error) {
                throw new ConfigurationException(configurationClass, file, error);
            }
        }
        // Step 2: Inserting values
        for (var entry : configurations.entrySet()) {
            final ConfigurationHolder<?> holder = entry.getKey();
            final Class<? extends Configuration> configurationClass = holder.getConfigurationClass();
            final FieldDataContainer container = entry.getValue();
            // ...
            try {
                this.insert(configurationClass, container);
                // Step 3: Invoking 'Configuration#onReload' on configuration classes
                this.call(configurationClass);
            } catch (final IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException | InstantiationException error) {
                throw new ConfigurationException(configurationClass, holder.getFile(), error);
            }
        }
    }

    private <T extends Configuration> FieldDataContainer collect(@NotNull final Class<T> configurationClass, @NotNull final JsonElement root) throws JsonParseException {
        final FieldDataContainer container = new FieldDataContainer();
        // For each declared field...
        for (final Field field : configurationClass.getDeclaredFields()) {
            // Skipping non-static / non-final fields, fields missing @Path annotation or inaccessible fields
            if (isStaticNonFinal(field) == false || field.canAccess(null) == false || field.isAnnotationPresent(Path.class) == false)
                continue;
            // Getting 'path' passed to the @Path annotation for that field
            final String path = field.getAnnotation(Path.class).value();
            // Getting the value from JsonElement
            final JsonElement element = getJsonElement(root, path);
            final JsonAdapter<?>
            final Object obj0 = read(getJsonElement(root, path), gson.getAdapter(field.getType()));
            final Object obj = gson.fromJson(

                    (field.getAnnotation(Serialization.class) == null)
                            ? field.getGenericType()
                            : field.getAnnotation(Serialization.class).deserializer()
            );
            // ...
            if (obj == null && field.getAnnotation(Required.class) != null)
                throw new JsonParseException("Json object at path $." + path + " cannot be null.");
            // Creating and adding ModifiedField to the set
            container.add(field.getName(), field.getType(), obj);
        }
        return container;
    }

    private <T extends Configuration> void insert(@NotNull final Class<T> configurationClass, @NotNull final FieldDataContainer fields) throws IllegalAccessException, IllegalArgumentException {
        for (final Field field : configurationClass.getDeclaredFields()) {
            final String fieldName = field.getName();
            // ...
            if (fields.has(fieldName) == true) {
                field.set(null, fields.get(fieldName).getValue());
            }
        }
    }

    private void call(@NotNull final Class<? extends Configuration> configurationClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        createInstance(configurationClass).onReload();
    }

    /* STATIC HELPERS */

    private static boolean isStaticNonFinal(final Field field) {
        final int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers) == true && Modifier.isFinal(modifiers) == false;
    }

    private static JsonElement parseFile(final File file) throws IOException, JsonParseException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        // Converting JSON string (from a BufferedReader) to JsonElement
        return JsonParser.parseReader(reader);
    }

    private static JsonReader newJsonReader(final JsonElement json) {
        return new JsonTreeReader(json);
    }

    private static <T> T read(final JsonElement element, final TypeAdapter<T> type) throws JsonSyntaxException {
        try {
            return type.read(newJsonReader(element));
        } catch (final IOException e) {
            throw new JsonSyntaxException(e);
        }
    }

    // TO-DO: Perhaps there is a better (and smarter) way to do that?
    private static JsonElement getJsonElement(final JsonElement json, final String path) {
        JsonElement result = json;
        for (final String key : path.replace(" ", "").split("[.\\[\\]]")) {
            // Returning 'JsonNull.INSTANCE' if result turns out to (for some reason) be 'null'
            if (result == null)
                return JsonNull.INSTANCE;
            // Skipping empty keys; moving onto the next one
            if (key.length() == 0)
                continue;
            // Handling JsonObject
            if (result instanceof JsonObject jObject) {
                result = jObject.get(key);
                continue;
            }
            // Handling JsonArray
            if (result instanceof JsonArray jArray) {
                result = jArray.get(Integer.parseInt(key));
                continue;
            }
            // All checks failed meaning that 'result' is most likely a value; escaping the loop
            break;
        }
        return result;
    }

    private static <T> T createInstance(final Class<T> clazz) throws IllegalAccessException {
        try {
            return (clazz.isEnum() == true) ? clazz.getEnumConstants()[0] : clazz.getConstructor().newInstance();
        } catch (final InstantiationException | InvocationTargetException | NoSuchMethodException exc) {
            throw new IllegalArgumentException("Could not create instance of " + clazz.getName(), exc);
        }
    }

}
