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
package cloud.grabsky.configuration.paper;

import cloud.grabsky.configuration.ConfigurationMapper;
import cloud.grabsky.configuration.paper.adapter.*;
import cloud.grabsky.configuration.paper.moshi_adapters.ComponentJsonAdapter;
import cloud.grabsky.configuration.paper.object.EnchantmentEntry;
import cloud.grabsky.configuration.paper.object.PersistentDataEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.moshi.Moshi;
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

    private PaperConfigurationMapper(final Moshi moshi) {
        super(moshi);
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
        return create((Consumer<Moshi.Builder>) null);
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
    public static @NotNull PaperConfigurationMapper create(@Nullable final Consumer<Moshi.Builder> consumer) {
        final Moshi.Builder builder = new Moshi.Builder();
        // ...
        builder.add(Component.class, ComponentJsonAdapter.INSTANCE);
        // ...
        if (consumer != null) {
            consumer.accept(builder);
        }
        // ...
        return new PaperConfigurationMapper(builder.build());
    }

}
