package grabsky.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Fields marked with {@link JsonNotNull @JsonNotNull} annotation are expected not to be {@code null}. */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonNotNull { /* MARKER */ }
