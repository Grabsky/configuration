package grabsky.configuration.paper.exception;

/** Indicates that resource with provided name does not exist in plugin classpath. (main/resources/) */
public final class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException(final String name) {
        super("File named'" + name + "' was not found in plugin resources file.");
    }

}