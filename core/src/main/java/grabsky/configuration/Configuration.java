package grabsky.configuration;

/** Only classes extending {@link Configuration} can be used within {@link ConfigurationMapper}. */
public abstract class Configuration {

    /** Called when {@link ConfigurationMapper} finishes inserting field values. */
    public void onReload() { /* EMPTY */ }
}
