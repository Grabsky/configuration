package grabsky.configuration.exceptions;

/** Indicates that resource with provided name does not exist in plugin classpath. (main/resources/) */
public final class ResourceNotFoundException extends Throwable {

    public ResourceNotFoundException(final String name) {
        super("File named'" + name + "' was not found in your plugin resources file.");
    }

}