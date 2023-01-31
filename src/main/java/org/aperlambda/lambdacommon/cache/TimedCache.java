/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.cache;

import org.aperlambda.lambdacommon.utils.LambdaUtils;
import org.aperlambda.lambdacommon.utils.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a cache with a lifetime for the stored objects.
 *
 * @param <T> The typename of the stored objects.
 * @version 1.8.1
 * @since 1.5.0
 */
public class TimedCache<K, T> implements Cache<K, T>
{
    private final Timer                       timer         = new Timer();
    private final int                         lifetime;
    private final HashMap<K, CachedObject<T>> cachedObjects = new HashMap<>();

    public static <K, T> TimedCache<K, T> ofLifetime(int lifetime)
    {
        return new TimedCache<>(lifetime);
    }

    public TimedCache(int lifetime)
    {
        if (lifetime <= 0)
            throw new IllegalArgumentException("Lifetime cannot be negative or null.");
        this.lifetime = lifetime;
        this.timer.scheduleAtFixedRate(LambdaUtils.newTimerTaskFromLambda(this::update), lifetime * 1000, lifetime * 1000);
    }

    /**
     * Gets the lifetime of the cached objects in seconds.
     *
     * @return The lifetime in seconds.
     */
    public long getLifetime()
    {
        return this.lifetime;
    }

    @Override
    public void update()
    {
        List<K> removeQueue = stream().filter(o -> (o.getSecond().getLastUsed() + this.lifetime * 1000) > System.currentTimeMillis()).map(Pair::getFirst).collect(Collectors.toList());
        removeQueue.forEach(this::remove);
    }

    @Override
    public void add(K key, T object, @Nullable Consumer<T> onDestroy)
    {
        this.cachedObjects.put(key, new CachedObject<>(object, onDestroy));
    }

    @Override
    public boolean has(K key)
    {
        return this.cachedObjects.containsKey(key);
    }

    @Override
    public void remove(K key)
    {
        this.cachedObjects.remove(key);
    }

    @Override
    public List<CachedObject<T>> list()
    {
        return new ArrayList<>(this.cachedObjects.values());
    }

    @Override
    public Stream<Pair<K, CachedObject<T>>> stream()
    {
        List<Pair<K, CachedObject<T>>> list = Pair.newListFromMap(this.cachedObjects);
        return list.stream();
    }
}
