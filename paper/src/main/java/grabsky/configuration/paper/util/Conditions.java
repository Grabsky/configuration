package grabsky.configuration.paper.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Conditions {

    /** Returns provided {@link T} if not null, throws {@link E} otherwise.  */
    public static <T, E extends Throwable> @NotNull T requirePresent(@Nullable final T value, final E exception) throws E {
        // Throwing exception if value is null
        if (value == null) throw exception;
        // Returning value otherwise
        return value;
    }

    /** Returns {@code value} if not null, {@code def} otherwise. */
    public static <T> @NotNull T requirePresent(@Nullable final T value, final T def) {
        return (value != null) ? value : def;
    }

}
