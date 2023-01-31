/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.config;

import org.aperlambda.lambdacommon.utils.Serializable;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a configuration.
 *
 * @param <C> The configuration object type.
 * @author LambdAurora
 * @version 1.8.0
 * @since 1.3.0
 */
public interface Config<C>
{
    /**
     * Gets a value from the config.
     *
     * @param key The key of the value.
     * @return The found value or null.
     */
    default @Nullable
    String get(String key)
    {
        return this.get(key, (String) null);
    }

    /**
     * Gets a value from the config.
     *
     * @param key The key of the value.
     * @param def The default value if not found.
     * @return The found value or the specified default one if not found.
     */
    default String get(String key, String def)
    {
        return this.get(key, def, String.class);
    }

    /**
     * Gets a value from the config.
     *
     * @param key  The key of the value.
     * @param def  The default value if not found.
     * @param type The type of the object to get.
     * @param <T>  The type of the object to get.
     * @return The found value or the specified default one if not found.
     */
    <T> T get(String key, T def, Class<T> type);

    /**
     * Sets a value of the config.
     * The value is a serializable object, it sets the value as a string using {@link Serializable#serialize()}.
     *
     * @param key          The key of the value to set.
     * @param serializable The serializable value to set.
     */
    default void set(String key, Serializable serializable)
    {
        this.set(key, serializable.serialize());
    }

    /**
     * Sets a value of the config.
     *
     * @param key   The key of the value to set.
     * @param value The value to set.
     */
    void set(String key, Object value);

    default @Nullable
    String at(String path)
    {
        return this.at(path, (String) null);
    }

    default String at(String path, String def)
    {
        return this.at(path, def, String.class);
    }

    default <T> T at(String path, Class<T> type)
    {
        return this.at(path, null, type);
    }

    /**
     * Gets the value from a given path.<br><br>
     * <p>
     * The method works recursively: it goes up the config tree.
     *
     * @param path The path of the value.
     * @param def  The default value if not found.
     * @param type The type of the value to get.
     * @param <T>  The type of the value to get.
     * @return The value if found else the default value.
     */
    <T> T at(String path, T def, Class<T> type);

    /**
     * Checks whether the config is virtual.
     *
     * @return True if virtual else false.
     */
    boolean isVirtual();

    /**
     * Gets the configuration object.
     *
     * @return The configuration object.
     */
    C getConfig();
}
