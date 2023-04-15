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

import cloud.grabsky.configuration.exception.ConfigurationMappingException;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import okio.Okio;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.squareup.moshi.JsonReader.Token;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfigurationMapper {

    private final Moshi moshi;

    /**
     * Creates {@link ConfigurationMapper} instance using provided {@link Moshi} instance for re-mapping.
     *
     * @param moshi {@link Moshi} instance.
     * @return a new instance of {@link ConfigurationMapper}.
     */
    public static @NotNull ConfigurationMapper create(@NotNull final Moshi moshi) {
        return new ConfigurationMapper(moshi);
    }

    /**
     * Maps file contents to {@code public}, {@code static}, {@code non-final} fields declared inside provided class.
     * When method fails due to {@link JsonDataException} - a new {@link ConfigurationMappingException}
     * is thrown and fields of {@link T} remain unchanged.
     *
     * @param configurationClass class with fields to be replaced.
     * @param configurationFile {@link File} containing json configuration.
     * @throws ConfigurationMappingException when configuration fails to load.
     */
    public final <T extends JsonConfiguration> void map(@NotNull final Class<T> configurationClass, @NotNull final File configurationFile) throws ConfigurationMappingException {
        this.map(ConfigurationHolder.of(configurationClass, configurationFile));
    }

    /**
     * Maps contents of all files to {@code public}, {@code static}, {@code non-final} fields declared in relative classes.
     * When method fails due to {@link JsonDataException} - a new {@link ConfigurationMappingException}
     * is thrown and <b><i>all</i></b> fields remain unchanged.
     *
     * @param holders vararg of {@link ConfigurationHolder} instances
     * @throws ConfigurationMappingException when configuration fails to load.
     */
    @SafeVarargs
    public final void map(@NotNull final ConfigurationHolder<? extends JsonConfiguration>... holders) throws ConfigurationMappingException {
        final Map<ConfigurationHolder<?>, Map<String, FieldData>> configurations = new LinkedHashMap<>();
        // Step 1: Collecting values
        for (var holder : holders) {
            var configurationClass = holder.getConfigurationClass();
            final File configurationFile = holder.getFile();
            // ...
            try (final JsonReader reader = JsonReader.of(Okio.buffer(Okio.source(configurationFile)))) {
                // Parsing values and collecting them to FieldDataContainer
                final Map<String, FieldData> container = this.collect(configurationClass, reader);
                // Adding container to the map
                configurations.put(holder, container);
            } catch (final IOException | RuntimeException error) {
                throw new ConfigurationMappingException(configurationClass, configurationFile, error);
            }
        }
        // Step 2: Inserting values
        configurations.forEach((holder, container) -> {
            var configurationClass = holder.getConfigurationClass();
            try {
                insert(configurationClass, container);
                // Step 3: Calling #onReload method on each of configuration classes
                createInstance(configurationClass).onReload();
            } catch (final IllegalAccessException | IllegalArgumentException error) {
                throw new ConfigurationMappingException(configurationClass, holder.getFile(), error);
            }
        });
    }

    // Parses and "collects" values defined in configuration class. Fields not annotated with @JsonPath annotation are ignored.
    private <T extends JsonConfiguration> Map<String, FieldData> collect(@NotNull final Class<T> configurationClass, @NotNull final JsonReader reader) throws IOException, IllegalArgumentException {
        final Map<String, FieldData> container = new HashMap<>();
        // For each declared field...
        for (final Field field : configurationClass.getDeclaredFields()) {
            // Skipping non-static / non-final fields, fields missing @JsonPath annotation or inaccessible fields
            if (isStaticNonFinal(field) == false || field.canAccess(null) == false || field.isAnnotationPresent(JsonPath.class) == false)
                continue;
            // Getting path passed to the @JsonPath annotation for that field
            final String path = field.getAnnotation(JsonPath.class).value();
            // Creating a copy of current reader and placing it at desired path
            final JsonReader contextReader = atPath(reader.peekJson(), path);
            // Obtaining correct TypeAdapter<T> based on context
            final JsonAdapter<?> adapter = (field.getAnnotation(cloud.grabsky.configuration.JsonAdapter.class) != null)
                    // Creating new instance of TypeAdapter<T> specified using @JsonAdapter
                    ? createInstance(field.getAnnotation(cloud.grabsky.configuration.JsonAdapter.class).fromJson())
                    // Getting default adapter for that type otherwise
                    : moshi.adapter(field.getGenericType());
            // Reading a value by directly using JsonReader and TypeAdapter<T>
            final Object o = adapter.nullSafe().lenient().fromJson(contextReader);
            // Throwing exception if field is NOT marked as @JsonNullable and produced value is null
            if (o == null && field.getAnnotation(JsonNullable.class) == null) {
                throw new IllegalArgumentException("Json object at path $." + path + " cannot be null");
            }
            // Creating and adding FieldData to the set
            container.put(field.getName(), new FieldData(field.getType(), o));
            // Closing reader...
            contextReader.close();
        }
        return container;
    }

    /* STATIC HELPERS */

    // Updates field values to stored those inside FieldDataContainer. Other fields are ignored.
    private static <T extends JsonConfiguration> void insert(@NotNull final Class<T> configurationClass, @NotNull final Map<String, FieldData> fields) throws IllegalAccessException, IllegalArgumentException {
        for (final Field field : configurationClass.getDeclaredFields()) {
            final String fieldName = field.getName();
            // Setting values for matching fields
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

    // "Puts" JsonReader at a specified path.
    private static JsonReader atPath(final JsonReader reader, final String path) throws IOException, IllegalArgumentException {
        final String jPath = "$." + path;
        reader.setLenient(true);
        // "Walking" over the JsonReader in search of matching path...
        while (reader.peek() != Token.END_DOCUMENT) {
            // Returning JsonReader as soon as both paths are equal
            if (reader.getPath().equals(jPath) == true)
                return reader;
            // ...
            switch (reader.peek()) {
                case NAME -> reader.nextName(); // JsonReader#skipName() makes path full of nulls for some reason.
                case BEGIN_OBJECT -> reader.beginObject();
                case END_OBJECT -> reader.endObject();
                case BEGIN_ARRAY -> reader.beginArray();
                case END_ARRAY -> reader.endArray();
                default -> reader.skipValue();
            }
        }
        // ...
        throw new IllegalArgumentException("Path " + "$." + path + " does not exist");
    }

    // Creates an instance of provided Class<T> or throws IllegalAccessException if failed.
    private static <T> T createInstance(final Class<T> clazz) throws IllegalArgumentException {
        try {
            return (clazz.isEnum() == true) ? clazz.getEnumConstants()[0] : clazz.getConstructor().newInstance();
        } catch (final InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException error) {
            throw new IllegalArgumentException("Could not create instance of " + clazz.getName(), error);
        }
    }

}
