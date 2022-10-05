package grabsky.configuration;

import grabsky.configuration.exceptions.CannotAccessFieldException;
import grabsky.configuration.exceptions.ResourceNotFoundException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

// TO-DO: Better exception handling including developer-friendly explanation of what went wrong.
public class ConfigurationManagerImpl implements ConfigurationManager {
    private final Plugin plugin;
    private final ConfigurationOptions options;

    protected ConfigurationManagerImpl(final Plugin plugin, final ConfigurationOptions options) {
        this.plugin = plugin;
        this.options = options;
    }

    @Override
    public <T extends Configuration> boolean reload(@NotNull final Class<T> clazz, @NotNull final File file) {
        try {
            // Creating file if does not exist...
            if (file.exists() == false) {
                plugin.saveResource(file.getName(), false);
                // Double-checking...
                if (file.exists() == false)
                    throw new ResourceNotFoundException(file.getName());
            }
            // Creating new instance of HoconConfigurationLoader using provided ConfigurationOptions
            final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                    .file(file)
                    .defaultOptions(options)
                    .build();
            // Loading configuration file to ConfigurationNode object
            final ConfigurationNode config = loader.load();
            // Attempting to replace static non-final class fields with configuration contents...
            for (final Field field : clazz.getDeclaredFields()) {
                // Skipping non-static and non-final fields
                if (isFieldStaticNonFinal(field) == false || field.isAnnotationPresent(ConfigProperty.class) == false) continue;
                // Getting path from @ConfigProperty annotation
                final String[] path = field.getAnnotation(ConfigProperty.class).value();
                // Throwing CannotAccessFieldException if field cannot be accessed (for some reason)
                if (field.canAccess(null) == false)
                    throw new CannotAccessFieldException(clazz, field, path);
                // Setting serialized
                field.set(null, config.node((Object[]) path).get(field.getGenericType()));
            }
            // Executing Configuration#onReload function
            clazz.getDeclaredMethod("onReload").invoke((clazz.isEnum() == true) ? clazz.getEnumConstants()[0] : clazz.getDeclaredConstructor().newInstance());
            // Returning 'true' since configuration reload succeeded
            return true;
        } catch (final ConfigurateException | ResourceNotFoundException e) {
            plugin.getLogger().severe(e.getMessage());
            // Returning 'false' since configuration reload failed
            return false;
        } catch (final InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
            // Returning 'false' since configuration reload failed
            return false;
        }
    }

    // Returns `true` if provided `java.lang.reflect.Field` is static.
    private static boolean isFieldStaticNonFinal(final Field field) {
        return Modifier.isStatic(field.getModifiers()) == true && Modifier.isFinal(field.getModifiers()) == false;
    }
}
