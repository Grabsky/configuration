/*
 * MIT License
 *
 * Copyright (c) 2023 Grabsky <44530932+Grabsky@users.noreply.github.com>
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
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfigurationMapper {

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
    public <T extends JsonConfiguration> void map(@NotNull final Class<T> configurationClass, @NotNull final File configurationFile) throws ConfigurationException {
        try {
            // Converting JSON string (from a BufferedReader) to JsonElement
            final JsonElement root = parseFile(configurationFile);
            // Updating the fields and returning the result
            final Map<String, FieldData> container = this.collect(configurationClass, root);
            // Inserting collected values to provided class' fields
            insert(configurationClass, container);
            // Calling provided class' 'onReload' method
            createInstance(configurationClass).onReload();
        } catch (final IOException | JsonParseException | IllegalAccessException | IllegalArgumentException error) {
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
    public void map(@NotNull final ConfigurationHolder<? extends JsonConfiguration>... holders) throws ConfigurationException {
        final Map<ConfigurationHolder<?>, Map<String, FieldData>> configurations = new LinkedHashMap<>();
        // Step 1: Collecting values
        for (final ConfigurationHolder<? extends JsonConfiguration> holder : holders) {
            final Class<? extends JsonConfiguration> configurationClass = holder.getConfigurationClass();
            final File configurationFile = holder.getFile();
            // ...
            try {
                // Reading JsonElement from the file
                final JsonElement json = parseFile(configurationFile);
                // Parsing values and collecting them to FieldDataContainer
                final Map<String, FieldData> container = this.collect(configurationClass, json);
                // Adding container to the map
                configurations.put(holder, container);
            } catch (final IOException | JsonParseException | IllegalArgumentException error) {
                throw new ConfigurationException(configurationClass, configurationFile, error);
            }
        }
        // Step 2: Inserting values
        for (var entry : configurations.entrySet()) {
            final ConfigurationHolder<?> holder = entry.getKey();
            final Class<? extends JsonConfiguration> configurationClass = holder.getConfigurationClass();
            final Map<String, FieldData> container = entry.getValue();
            // ...
            try {
                insert(configurationClass, container);
                // Step 3: Calling #onReload method on each of configuration classes
                createInstance(configurationClass).onReload();
            } catch (final IllegalAccessException | IllegalArgumentException error) {
                throw new ConfigurationException(configurationClass, holder.getFile(), error);
            }
        }
    }

    // Parses and "collects" values defined in configuration class. Fields not annotated with @JsonPath annotation are ignored.
    private <T extends JsonConfiguration> Map<String, FieldData> collect(@NotNull final Class<T> configurationClass, @NotNull final JsonElement root) throws JsonParseException, IllegalArgumentException {
        final Map<String, FieldData> container = new HashMap<>();
        // For each declared field...
        for (final Field field : configurationClass.getDeclaredFields()) {
            // Skipping non-static / non-final fields, fields missing @JsonPath annotation or inaccessible fields
            if (isStaticNonFinal(field) == false || field.canAccess(null) == false || field.isAnnotationPresent(JsonPath.class) == false)
                continue;
            // Getting 'path' passed to the @JsonPath annotation for that field
            final String path = field.getAnnotation(JsonPath.class).value();
            // Getting the value from JsonElement
            try (final JsonReader reader = atPath(new JsonTreeReader(root), path)) {
                // Obtaining correct TypeAdapter<T> based on context
                final TypeAdapter<?> adapter = (field.getAnnotation(JsonAdapter.class) != null)
                        // Creating new instance of TypeAdapter<T> specified using @JsonAdapter
                        ? (TypeAdapter<?>) createInstance(field.getAnnotation(JsonAdapter.class).value())
                        // Getting default adapter for that type otherwise
                        : gson.getAdapter(TypeToken.get(field.getGenericType()));
                // Reading a value by directly using JsonReader and TypeAdapter<T>
                final Object o = read(reader, adapter);
                // Throwing exception if field is NOT marked as @JsonNullable and produced value is null
                if (o == null && field.getAnnotation(JsonNullable.class) == null) {
                    throw new JsonParseException("Json object at path $." + path + " cannot be null");
                }
                // Creating and adding ModifiedField to the set
                container.put(field.getName(), new FieldData(field.getType(), o));
            } catch (final IOException | IllegalAccessException error) {
                throw new JsonParseException(error);
            }
        }
        return container;
    }

    /* STATIC HELPERS */

    // Updates field values to stored those inside FieldDataContainer. Other fields are ignored.
    private static <T extends JsonConfiguration> void insert(@NotNull final Class<T> configurationClass, @NotNull final Map<String, FieldData> fields) throws IllegalAccessException, IllegalArgumentException {
        for (final Field field : configurationClass.getDeclaredFields()) {
            final String fieldName = field.getName();
            // ...
            if (fields.containsKey(fieldName) == true) {
                field.set(null, fields.get(fieldName).getValue());
            }
        }
    }

    // TO-DO: Use bitwise operator instead.
    private static boolean isStaticNonFinal(final Field field) {
        final int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers) == true && Modifier.isFinal(modifiers) == false;
    }

    // Converts JSON content of provided File to JsonElement.
    private static JsonElement parseFile(final File file) throws IOException, JsonParseException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        // Converting JSON string (from a BufferedReader) to JsonElement
        return JsonParser.parseReader(reader);
    }

    // Reads T from provided JsonReader using provided TypeAdapter<T>, or returns null.
    private static <T> @Nullable T read(final JsonReader reader, final TypeAdapter<T> adapter) {
        try {
            return adapter.read(reader);
        } catch (final IOException error) {
            return null;
        }
    }

    // "Puts" JsonReader at a specified path.
    private static JsonReader atPath(final JsonReader reader, final String path) throws IOException {
        final String[] arr = path.replace(" ", "").split("\\.");
        // ...
        for (final String key : arr) {
            // ...
            while (reader.hasNext() == true) {
                if (reader.peek() == JsonToken.BEGIN_OBJECT)
                    reader.beginObject();
                // ...
                if (reader.peek() == JsonToken.END_OBJECT)
                    reader.endObject();
                // ...
                if (reader.peek() == JsonToken.NAME && reader.nextName().equals(key) == true)
                    break;
                // ...
                reader.skipValue();
                // ...
                if (reader.hasNext() == false)
                    throw new JsonParseException("Json object at path $." + path + " does not exist");
            }
        }
        return reader;
    }

    // Creates an instance of provided Class<T> or throws IllegalAccessException if failed.
    private static <T> T createInstance(final Class<T> clazz) throws IllegalAccessException {
        try {
            return (clazz.isEnum() == true) ? clazz.getEnumConstants()[0] : clazz.getConstructor().newInstance();
        } catch (final InstantiationException | InvocationTargetException | NoSuchMethodException exc) {
            throw new IllegalArgumentException("Could not create instance of " + clazz.getName(), exc);
        }
    }

}
