/*
 * MIT License
 *
 * Copyright (c) 2023 Grabsky <44530932+Grabsky@users.noreply.github.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * HORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cloud.grabsky.configuration.paper.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Resources {

    /**
     * Writes {@link InputStream} to a specified {@link File}.
     */
    public static void copy(final InputStream is, final File file) throws IOException {
        // Reading file as InputStream
        final OutputStream os = new FileOutputStream(file);
        // Writing contents of 'InputStream' (source content) to the 'FileOutputStream' (destination file)
        os.write(is.readAllBytes());
        // Closing 'FileOutputStream'
        os.close();
    }

    /**
     * Ensures specified {@link File} exists and returns it. In case {@link File} does not exist, it gets copied from {@code classpath/resources/} directory.
     */
    public static File ensureResourceExistence(final Plugin plugin, final File file) throws IOException {
        // Returning if file already exist
        if (file.exists() != false)
            return file;
        // ...
        final InputStream in = plugin.getResource(file.getName());
        // Throwing 'ResourceNotFoundException' if file does not exist in 'main/resources' directory.
        if (in == null)
            throw new IOException("File " + file.getPath() + " does not exist.");
        //
        file.getParentFile().mkdirs();
        file.createNewFile();
        // ...
        final FileOutputStream out = new FileOutputStream(file); // This should also create the file.
        // Writing contents of 'InputStream' (source content) to the 'FileOutputStream' (destination file)
        out.write(in.readAllBytes());
        // Closing 'FileOutputStream'
        out.close();
        // Returning
        return file;
    }

}