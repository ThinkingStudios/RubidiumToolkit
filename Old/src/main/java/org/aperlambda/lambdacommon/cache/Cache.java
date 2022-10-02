/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.cache;

import org.aperlambda.lambdacommon.utils.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Represents a cache.
 *
 * @param <T> The typename of the stored objects.
 * @version 1.8.0
 * @since 1.5.0
 */
public interface Cache<K, T>
{
    /**
     * Updates the cache.
     */
    void update();

    /**
     * Adds an object to cache.
     *
     * @param key    The assigned key.
     * @param object The object to cache.
     */
    default void add(K key, T object)
    {
        this.add(key, object, null);
    }

    /**
     * Adds an object to cache.
     *
     * @param key       The assigned key.
     * @param object    The object to cache.
     * @param onDestroy The function to execute when the object is removed from the cache.
     */
    void add(K key, T object, @Nullable Consumer<T> onDestroy);

    /**
     * Checks whether the cache has an object with the specified key.
     *
     * @param key The key of the cached object.
     * @return True if the cache contains an object with the specified key, else false.
     */
    boolean has(K key);

    /**
     * Checks whether the cache has the specified object.
     *
     * @param object The object to check.
     * @return True if the cache contains the object, else false.
     */
    default boolean hasObject(T object)
    {
        return this.stream().anyMatch(o -> o.equals(object));
    }

    /**
     * Removes a cached object.
     *
     * @param key The key assigned to the cached object.
     */
    void remove(K key);

    List<CachedObject<T>> list();

    Stream<Pair<K, CachedObject<T>>> stream();
}
