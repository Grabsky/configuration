package grabsky.configuration.paper.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public final class PersistentDataTypeHolder<T, K> {
    @Getter @NotNull private final String key;
    @Getter @NotNull private final PersistentDataType<T, K> type;
}