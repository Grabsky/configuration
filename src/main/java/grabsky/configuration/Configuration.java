package grabsky.configuration;

public interface Configuration {
    default void onReload() { /* NO IMPL BY DEFAULT */ }
}
