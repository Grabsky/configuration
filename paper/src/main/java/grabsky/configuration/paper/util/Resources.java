package grabsky.configuration.paper.util;

import grabsky.configuration.paper.exception.ResourceNotFoundException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Resources {

    // Copies InputStream to a file
    public static void copy(final InputStream is, final File file) throws IOException {
        // Reading file as InputStream
        final OutputStream os = new FileOutputStream(file);
        // Writing contents of 'InputStream' (source content) to the 'FileOutputStream' (destination file)
        os.write(is.readAllBytes());
        // Closing 'FileOutputStream'
        os.close();
    }

    public static void ensureResourceExistence(final Plugin plugin, final File file) throws ResourceNotFoundException, IOException {
        // Returning if file already exist
        if (file.exists() != false) return;
        // ...
        final InputStream in = plugin.getResource(file.getName());
        // Throwing 'ResourceNotFoundException' if file does not exist in 'main/resources' directory.
        if (in == null)
            throw new ResourceNotFoundException(file.getName());
        //
        file.getParentFile().mkdirs();
        file.createNewFile();
        // ...
        final FileOutputStream out = new FileOutputStream(file); // This should also create the file.
        // Writing contents of 'InputStream' (source content) to the 'FileOutputStream' (destination file)
        out.write(in.readAllBytes());
        // Closing 'FileOutputStream'
        out.close();
    }
}