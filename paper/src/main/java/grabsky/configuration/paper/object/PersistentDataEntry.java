package grabsky.configuration.paper.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public final class PersistentDataEntry {
    @Getter @NotNull private final NamespacedKey key;
    @Getter @NotNull private final PersistentDataType<?, ?> persistentDataType;
    @Getter @NotNull private final Object value;
}