package cloud.grabsky.configuration.paper;

import cloud.grabsky.configuration.ConfigurationMapper;
import cloud.grabsky.configuration.paper.adapter.*;
import cloud.grabsky.configuration.paper.object.EnchantmentEntry;
import cloud.grabsky.configuration.paper.object.PersistentDataEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;


public final class PaperConfigurationMapper extends ConfigurationMapper {

    private PaperConfigurationMapper(final Gson gson) {
        super(gson);
    }

    /**
     * Creates pre-configured instance of {@link ConfigurationMapper}:
     * <ul>
     *     <li>All module-provided serializers are registered.</li>
     *     <li>Lenient JSON specification is enabled - mainly to allow comments.</li>
     *     <li>HTML escaping disabled - to allow MiniMessage tags without the need of escaping them.</li>
     * </ul>
     */
    public static @NotNull PaperConfigurationMapper create() {
        return create((Consumer<GsonBuilder>) null);
    }

    /**
     * Creates pre-configured instance of {@link ConfigurationMapper}:
     * <ul>
     *     <li>All module-provided serializers are registered.</li>
     *     <li>Lenient JSON specification is enabled - mainly to allow comments.</li>
     *     <li>HTML escaping disabled - to allow MiniMessage tags without the need of escaping them.</li>
     * </ul>
     * Provided {@link Consumer} is applied after serializers are registered, so
     * they can be overridden, but before other configurations are made.
     */
    public static @NotNull PaperConfigurationMapper create(@Nullable final Consumer<GsonBuilder> consumer) {
        final GsonBuilder builder = new GsonBuilder();
        // ...
        builder.registerTypeAdapter(Component.class, ComponentTypeAdapter.INSTANCE);
        builder.registerTypeAdapter(EnchantmentEntry.class, EnchantmentEntrySerializer.INSTANCE);
        builder.registerTypeAdapter(Enchantment.class, EnchantmentSerializer.INSTANCE);
        builder.registerTypeAdapter(EntityType.class, EntityTypeSerializer.INSTANCE);
        builder.registerTypeAdapter(ItemFlag.class, ItemFlagTypeAdapter.INSTANCE);
        builder.registerTypeAdapter(ItemStack.class, ItemStackSerializer.INSTANCE);
        builder.registerTypeAdapter(Material.class, MaterialSerializer.INSTANCE);
        builder.registerTypeAdapter(NamespacedKey.class, NamespacedKeySerializer.INSTANCE);
        builder.registerTypeAdapter(PersistentDataEntry.class, PersistentDataEntrySerializer.DEFAULT);
        builder.registerTypeAdapter(Sound.class, SoundSerializer.INSTANCE);
        builder.registerTypeAdapter(Sound.Source.class, SoundSourceTypeAdapter.INSTANCE);
        // ...
        if (consumer != null) {
            consumer.accept(builder);
        }
        // ...
        builder.setLenient();
        builder.disableHtmlEscaping();
        // ...
        return new PaperConfigurationMapper(builder.create());
    }

}
