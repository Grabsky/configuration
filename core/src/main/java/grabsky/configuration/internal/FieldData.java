package grabsky.configuration.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
@AllArgsConstructor
public final class FieldData {
    @Getter private final Class<?> type;
    @Getter private final Object value;
}
