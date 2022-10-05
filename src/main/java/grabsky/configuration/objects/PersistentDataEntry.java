package grabsky.configuration.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

@AllArgsConstructor
public final class PersistentDataEntry {
    @Getter private final NamespacedKey key;
    @Getter private final PersistentDataType<?, ?> persistentDataType;
    @Getter private final Object value;
}
