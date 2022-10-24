package grabsky.configuration.paper;

import grabsky.configuration.serializers.CaseInsensitiveEnumSerializer;
import org.bukkit.inventory.ItemFlag;

/** {@inheritDoc} */
public class ItemFlagSerializer extends CaseInsensitiveEnumSerializer<ItemFlag> {

    /** Default instance of {@link ItemFlagSerializer}. */
    public static final ItemFlagSerializer INSTANCE = new ItemFlagSerializer();
}