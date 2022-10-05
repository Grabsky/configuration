package grabsky.configuration.objects;

import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

public final class ItemEnchantment {
    private final Enchantment enchantment;
    private final int level;

    public ItemEnchantment(@NotNull final Enchantment enchantment, final int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public Integer getLevel() {
        return level;
    }
}
