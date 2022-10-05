package grabsky.configuration.helpers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Preconditions {

    /** Returns provided `value` if not null. Throws `exception` otherwise.  */
    public static <T, E extends Throwable> @NotNull T requirePresent(@Nullable final T value, final E exception) throws E {
        // Throwing exception if value is null
        if (value == null) throw exception;
        // Returning value otherwise
        return value;
    }

    /** Returns either `value` or `def` if `value` is null. */
    public static <T> @NotNull T requirePresent(@Nullable final T value, final T def) {
        return (value != null) ? value : def;
    }

}
