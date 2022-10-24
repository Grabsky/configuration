package grabsky.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies json path of the value. Used for field mapping. <br>
 * <pre>
 *   {
 *     "customer": {
 *       "name": "John Smith",
 *       "invoices": [
 *         { "id": 28097483, ... },
 *         { "id": 84578434, ... }
 *       ]
 *     }
 *   }
 * </pre>
 * Element: {@code @JsonPath("customer.name")} <br>
 * Whole array: {@code @JsonPath("customer.invoices")} <br>
 * Element from array: {@code @JsonPath("customer.invoices[0]")}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonPath {
    String value();
}
