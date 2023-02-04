/*
 * MIT License
 *
 * Copyright (c) 2023 Grabsky
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
package cloud.grabsky.configuration;

import cloud.grabsky.configuration.exception.ConfigurationException;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface ConfigurationMapper {

    /**
     * Maps file contents to {@code public}, {@code static}, {@code non-final} fields declared inside provided class.
     * When method fails due to {@link JsonParseException} - a new {@link ConfigurationException}
     * is thrown and fields of {@link T} remain unchanged.
     *
     * @param configurationClass class with fields to be replaced.
     * @param configurationFile {@link File} containing json configuration.
     * @throws ConfigurationException when configuration fails to load.
     */
    <T extends Configuration> void map(@NotNull final Class<T> configurationClass, @NotNull final File configurationFile) throws ConfigurationException;

    /**
     * Maps contents of all files to {@code public}, {@code static}, {@code non-final} fields declared in relative classes.
     * When method fails due to {@link JsonParseException} - a new {@link ConfigurationException}
     * is thrown and <b><i>all</i></b> fields remain unchanged.
     *
     * @param configurations vararg of {@link ConfigurationHolder} instances
     * @throws ConfigurationException when configuration fails to load.
     */
    void mapCollection(@NotNull final ConfigurationHolder<?>... configurations) throws ConfigurationException;

    /**
     * Creates {@link ConfigurationMapper} instance using provided {@link Gson} instance for re-mapping.
     *
     * @param gson {@link Gson} instance.
     * @return a new instance of {@link ConfigurationMapper}.
     */
    static @NotNull ConfigurationMapper create(@NotNull final Gson gson) {
        return new ConfigurationMapperImpl(gson);
    }

}
