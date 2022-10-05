package grabsky.configuration.exceptions;

import java.lang.reflect.Field;
import java.util.Arrays;

/** Indicates that field could not be accessed. */
public final class CannotAccessFieldException extends IllegalAccessException {

    public CannotAccessFieldException(final Class<?> clazz, final Field field, final String[] configurationPath) {
        super("[" + clazz.getName() + "] Could not access/override field named '" + field.getName() + " with reference to " + Arrays.toString(configurationPath));
    }

}