package grabsky.configuration.paper.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class EnchantmentEntry {
    @Getter @NotNull private final Enchantment enchantment;
    @Getter private final int level;
}
