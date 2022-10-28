package grabsky.configuration.paper;

import grabsky.configuration.serializers.CaseInsensitiveEnumSerializer;
import org.bukkit.inventory.ItemFlag;

/** Converts {@link String} to {@link ItemFlag} and vice-versa but using case-insensitive strategy. */
public final class ItemFlagSerializer implements CaseInsensitiveEnumSerializer<ItemFlag> {

    /** Default instance of {@link ItemFlagSerializer}. */
    public static final ItemFlagSerializer INSTANCE = new ItemFlagSerializer();
}