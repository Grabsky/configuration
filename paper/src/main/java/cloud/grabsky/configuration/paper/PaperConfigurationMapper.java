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
import cloud.grabsky.configuration.paper.adapter.ComponentAdapter;
import cloud.grabsky.configuration.paper.adapter.EnchantmentAdapterFactory;
import cloud.grabsky.configuration.paper.adapter.EnchantmentEntryAdapterFactory;
import cloud.grabsky.configuration.paper.adapter.EntityTypeAdapterFactory;
import cloud.grabsky.configuration.paper.adapter.ItemFlagAdapter;
import cloud.grabsky.configuration.paper.adapter.ItemStackAdapterFactory;
import cloud.grabsky.configuration.paper.adapter.MaterialAdapterFactory;
import cloud.grabsky.configuration.paper.adapter.NamespacedKeyAdapter;
import cloud.grabsky.configuration.paper.adapter.PersistentDataEntryAdapterFactory;
import cloud.grabsky.configuration.paper.adapter.PersistentDataTypeAdapterFactory;
import cloud.grabsky.configuration.paper.adapter.SoundAdapterFactory;
import cloud.grabsky.configuration.paper.adapter.SoundSourceAdapter;
import com.squareup.moshi.Moshi;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class PaperConfigurationMapper extends ConfigurationMapper {

    private PaperConfigurationMapper(final Moshi moshi) {
        super(moshi);
    }

    /**
     * Creates pre-configured instance of {@link PaperConfigurationMapper}
     * with all module-provided adapters registered.
     */
    public static @NotNull PaperConfigurationMapper create() {
        return create((Consumer<Moshi.Builder>) null);
    }

    /**
     * Creates pre-configured instance of {@link ConfigurationMapper}
     * with all module-provided adapters registered.
     */
    public static @NotNull PaperConfigurationMapper create(@Nullable final Consumer<Moshi.Builder> consumer) {
        final Moshi.Builder builder = new Moshi.Builder();
        // adapters
        builder.add(Component.class, ComponentAdapter.INSTANCE);
        builder.add(ItemFlag.class, ItemFlagAdapter.INSTANCE);
        builder.add(NamespacedKey.class, NamespacedKeyAdapter.INSTANCE);
        builder.add(Sound.Source.class, SoundSourceAdapter.INSTANCE);
        // adapter factories
        builder.add(EnchantmentAdapterFactory.INSTANCE);
        builder.add(EnchantmentEntryAdapterFactory.INSTANCE);
        builder.add(EntityTypeAdapterFactory.INSTANCE);
        builder.add(ItemStackAdapterFactory.INSTANCE);
        builder.add(MaterialAdapterFactory.INSTANCE);
        builder.add(PersistentDataEntryAdapterFactory.INSTANCE);
        builder.add(PersistentDataTypeAdapterFactory.INSTANCE);
        builder.add(SoundAdapterFactory.INSTANCE);
        // ...
        if (consumer != null)
            consumer.accept(builder);
        // ...
        return new PaperConfigurationMapper(builder.build());
    }

}
