package grabsky.configuration;

/**
 * Configuration files you want use with {@link ConfigurationMapper} must extend {@link Configuration}.
 */
public abstract class Configuration {

    /**
     *  Called when {@link ConfigurationMapper} finishes inserting values.
     *  This method is ignored if not overridden.
     */
    public void onReload() { /* EMPTY */ }
}
