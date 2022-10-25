package grabsky.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
/* package private */ final class FieldData {
    @Getter private final Class<?> type;
    @Getter private final Object value;
}
