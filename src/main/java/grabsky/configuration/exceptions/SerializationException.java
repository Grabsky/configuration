package grabsky.configuration.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SerializationException extends Throwable {
    @Getter private final @NotNull Class<?> type;
    @Getter private final @Nullable String value;

    public SerializationException(@NotNull final Class<?> type, @Nullable final String value) {
        this.type = type;
        this.value = value;
    }


}
