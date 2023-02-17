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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

/**
 * Holds information about single configuration.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigurationHolder<T extends Configuration> {

    @Getter(AccessLevel.PUBLIC)
    private final Class<T> configurationClass;

    @Getter(AccessLevel.PUBLIC)
    private final File file;

    /**
     * Creates instance of {@link ConfigurationHolder}.
     *
     * @param configurationClass class with fields to be replaced.
     * @param configurationFile {@link File} containing json configuration.
     * @return a new instance of {@link ConfigurationHolder}.
     */
    public static <T extends Configuration> ConfigurationHolder<T> of(final Class<T> configurationClass, final File configurationFile) {
        return new ConfigurationHolder<>(configurationClass, configurationFile);
    }

}
