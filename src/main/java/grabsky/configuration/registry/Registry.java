package grabsky.configuration.registry;

import org.jetbrains.annotations.Nullable;

public interface Registry<K, V> {

    /** ...TO-DO... */
    public Registry<K, V> add(final K key, V value);

    /** ...TO-DO... */
    public @Nullable V get(final K key);
}
