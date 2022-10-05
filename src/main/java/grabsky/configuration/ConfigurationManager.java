package grabsky.configuration;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationOptions;

import java.io.File;

public interface ConfigurationManager {

    /** Creates instance of ConfigurationManager with specified ConfigurationOptions. */
    public static ConfigurationManager create(final Plugin plugin, final ConfigurationOptions options) {
        return new ConfigurationManagerImpl(plugin, options);
    }

    /** Overrides static non-final fields annotated with @ConfigProperty inside provided class with configuration values inside `file`.
     *
     * @param clazz Class configuration is mapped to.
     * @param file File to look for configuration contents.
     *
     * @return true if no errors was thrown during reload process, false otherwise.
     */
    public <T extends Configuration> boolean reload(@NotNull final Class<T> clazz, @NotNull final File file);

}
