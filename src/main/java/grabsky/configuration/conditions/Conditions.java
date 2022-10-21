package grabsky.configuration.conditions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Conditions {

    public static <T> @NotNull T requirePresent(@Nullable final T value, @NotNull final T def) {
        return (value != null) ? value : def;
    }

    public static <T, E extends Throwable> @NotNull T requirePresent(@Nullable final T value, final E exception) throws E {
        // Throwing exception if value is null
        if (value == null) throw exception;
        // Returning value otherwise
        return value;
    }
}
