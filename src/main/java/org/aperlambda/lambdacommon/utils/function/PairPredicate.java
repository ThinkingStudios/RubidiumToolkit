/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.utils.function;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a predicate (boolean-valued function) of two arguments.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #test(Object, Object)}.
 *
 * @param <X> The type of the input to the predicate.
 * @param <Y> The type of the input to the predicate.
 * @version 1.7.2
 */
@FunctionalInterface
public interface PairPredicate<X, Y>
{
    /**
     * Evaluates this predicate on the given argument.
     *
     * @param x The first input argument.
     * @param y The second input argument.
     * @return {@code true} if the input argument matches the predicate otherwise {@code false}.
     */
    boolean test(X x, Y y);

    /**
     * Returns a predicate that represents the logical negation of this predicate.
     *
     * @return A predicate that represents the logical negation of this predicate.
     */
    default PairPredicate<X, Y> negate()
    {
        return (x, y) -> !test(x, y);
    }

    /**
     * Returns a composed predicate that represents the logical AND of this predicate and another.
     *
     * @param other The other AND's operand.
     * @return A predicate that represents the logical AND of this predicate and another.
     * @since 1.7.2
     */
    default PairPredicate<X, Y> and(@NotNull PairPredicate<? super X, ? super Y> other)
    {
        Objects.requireNonNull(other);
        return (x, y) -> this.test(x, y) && other.test(x, y);
    }

    /**
     * Returns a composed predicate that represents the logical OR of this predicate and another.
     *
     * @param other The other OR's operand.
     * @return A predicate that represents the logical OR of this predicate and another.
     * @since 1.7.2
     */
    default PairPredicate<X, Y> or(@NotNull PairPredicate<? super X, ? super Y> other)
    {
        Objects.requireNonNull(other);
        return (x, y) -> this.test(x, y) || other.test(x, y);
    }

    /**
     * Returns a predicate that is the negation of the supplied predicate.
     *
     * @param target Predicate to negate.
     * @param <X>    The type of arguments to the specified predicate.
     * @param <Y>    The type of arguments to the specified predicate.
     * @return A predicate that negates the results of the supplied predicate.
     * @since 1.7.2
     */
    static <X, Y> PairPredicate<X, Y> not(@NotNull PairPredicate<X, Y> target)
    {
        Objects.requireNonNull(target);
        return target.negate();
    }
}
