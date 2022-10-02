/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.utils.function;

import org.aperlambda.lambdacommon.utils.Pair;

import java.util.function.Function;

/**
 * Represents a function that accepts a {@link Pair} argument and produces a result.
 * This is the pair-arity specialization of {@link Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Pair)}.
 *
 * @version 1.4.5
 * @since 1.4.5
 */
@FunctionalInterface
public interface PairFunction<O, P, K, V> extends Function<Pair<O, P>, Pair<K, V>>
{
    /**
     * Applies this function to the given arguments.
     *
     * @param pair The {@link Pair} function argument.
     * @return The function result.
     */
    Pair<K, V> apply(Pair<O, P> pair);
}
