package grabsky.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import grabsky.configuration.exception.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

/* package private */ final class ConfigurationMapperImpl implements ConfigurationMapper {
    private final Gson gson;

    /* package private */ ConfigurationMapperImpl(final Gson gson) {
        this.gson = gson;
    }

    @Override
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

    @Override
    public void mapCollection(@NotNull final ConfigurationHolder<?>... holders) throws ConfigurationException {
        final Map<ConfigurationHolder<?>, FieldDataContainer> configurations = new LinkedHashMap<>();
        // Step 1: Collecting values
        for (final ConfigurationHolder<?> holder : holders) {
            final Class<?> configurationClass = holder.getConfigurationClass();
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
            final Class<?> configurationClass = holder.getConfigurationClass();
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

    private FieldDataContainer collect(@NotNull final Class<?> configurationClass, @NotNull final JsonElement root) throws JsonParseException {
        final FieldDataContainer container = new FieldDataContainer();
        // ...
        for (final Field field : configurationClass.getDeclaredFields()) {
            // Skipping non-static / non-final fields, fields missing @JsonPath annotation and inaccessible fields
            if (isStaticNonFinal(field) == false || field.canAccess(null) == false || field.isAnnotationPresent(JsonPath.class) == false)
                continue;
            // Getting 'path' passed to the @JsonPath annotation for that field
            final String path = field.getAnnotation(JsonPath.class).value();
            // Getting the value from JsonElement
            final Object obj = gson.fromJson(getJsonElement(root, path), field.getGenericType());
            // ...
            if (obj == null && field.getAnnotation(JsonNotNull.class) != null)
                throw new JsonParseException("Json object at path $." + path + " cannot be null.");
            // Creating and adding ModifiedField to the set
            container.add(field.getName(), field.getType(), obj);
        }
        return container;
    }

    private void insert(@NotNull final Class<?> configurationClass, @NotNull final FieldDataContainer fields) throws IllegalAccessException, IllegalArgumentException {
        for (final Field field : configurationClass.getDeclaredFields()) {
            final String fieldName = field.getName();
            // ...
            if (fields.has(fieldName) == true) {
                field.set(null, fields.get(fieldName).getValue());
            }
        }
    }

    private void call(@NotNull final Class<?> configurationClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        configurationClass.getMethod("onReload").invoke(configurationClass.getConstructor().newInstance());
    }

    private static boolean isStaticNonFinal(final Field field) {
        final int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers) == true && Modifier.isFinal(modifiers) == false;
    }

    private static JsonElement parseFile(final File file) throws IOException, JsonParseException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        // Converting JSON string (from a BufferedReader) to JsonElement
        return JsonParser.parseReader(reader);
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
            if (result instanceof JsonObject jsonObject) {
                result = jsonObject.get(key);
                continue;
            }
            // Handling JsonArray
            if (result instanceof JsonArray jsonArray) {
                result = jsonArray.get(Integer.parseInt(key));
                continue;
            }
            // All checks failed meaning that 'result' is most likely a value; escaping the loop
            break;
        }
        return result;
    }

}
