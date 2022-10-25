package grabsky.configuration;

import java.util.HashMap;

/* package private */ final class FieldDataContainer {
    private final HashMap<String, FieldData> container = new HashMap<String, FieldData>();

    public void add(final String fieldName, final Class<?> fieldType, final Object value) {
        container.put(fieldName, new FieldData(fieldType, value));
    }

    public FieldData get(final String fieldName) {
        return container.get(fieldName);
    }

    public boolean has(final String fieldName) {
        return container.containsKey(fieldName);
    }

}
