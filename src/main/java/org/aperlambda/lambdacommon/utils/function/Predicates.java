/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.utils.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Represents some utility functions for {@link java.util.function.Predicate} and {@link PairPredicate}.
 *
 * @author LambdAurora
 * @version 1.8.0
 * @since 1.7.4
 */
public class Predicates
{
    private Predicates()
    {
        throw new UnsupportedOperationException("Predicates is a full static class!");
    }

    /**
     * Returns a predicate given by the argument.
     * <p>
     * The function seems stupid at first but in the case of, for example: {@code Cancellable::is_cancelled} you can't use the {@link Predicate} methods on it.
     *
     * @param predicate The predicate.
     * @param <T>       The type of arguments to the specified predicate.
     * @return The predicate.
     */
    public static <T> @NotNull Predicate<T> of(@NotNull Predicate<T> predicate)
    {
        return predicate;
    }

    /**
     * Returns a predicate given by the argument.
     * <p>
     * The function seems stupid at first but in the case of, for example: {@code Cancellable::is_cancelled} you can't use the {@link PairPredicate} methods on it.
     *
     * @param predicate The predicate.
     * @param <X>       The type of arguments to the specified predicate.
     * @param <Y>       The type of arguments to the specified predicate.
     * @return The predicate.
     */
    public static <X, Y> @NotNull PairPredicate<X, Y> of(@NotNull PairPredicate<X, Y> predicate)
    {
        return predicate;
    }

    /**
     * Returns a predicate which always return true.
     *
     * @param <T> The type of arguments to the specified predicate.
     * @return The predicate
     */
    public static <T> @NotNull Predicate<T> alwaysTrue()
    {
        return x -> true;
    }

    /**
     * Returns a pair predicate which always return true.
     *
     * @param <X> The type of arguments to the specified predicate.
     * @param <Y> The type of arguments to the specified predicate.
     * @return The predicate
     */
    public static <X, Y> @NotNull PairPredicate<X, Y> pairAlwaysTrue()
    {
        return (x, y) -> true;
    }

    /**
     * Returns a predicate which always return false.
     *
     * @param <T> The type of arguments to the specified predicate.
     * @return The predicate
     */
    public static <T> @NotNull Predicate<T> alwaysFalse()
    {
        return x -> false;
    }

    /**
     * Returns a pair predicate which always return false.
     *
     * @param <X> The type of arguments to the specified predicate.
     * @param <Y> The type of arguments to the specified predicate.
     * @return The predicate
     */
    public static <X, Y> @NotNull PairPredicate<X, Y> pairAlwaysFalse()
    {
        return (x, y) -> false;
    }

    /**
     * Returns a predicate that is the negation of the supplied predicate.
     *
     * @param predicate Predicate to negate.
     * @param <T>       The type of arguments to the specified predicate.
     * @return A predicate that negates the results of the supplied predicate.
     * @see Predicate#negate()
     */
    public static <T> @NotNull Predicate<T> not(@NotNull Predicate<T> predicate)
    {
        return predicate.negate();
    }

    /**
     * Returns a predicate that is the negation of the supplied predicate.
     *
     * @param predicate Predicate to negate.
     * @param <X>       The type of arguments to the specified predicate.
     * @param <Y>       The type of arguments to the specified predicate.
     * @return A predicate that negates the results of the supplied predicate.
     * @see PairPredicate#negate()
     */
    public static <X, Y> @NotNull PairPredicate<X, Y> not(@NotNull PairPredicate<X, Y> predicate)
    {
        return predicate.negate();
    }

    /**
     * Returns a composed predicate that represents the logical AND of the first and second predicates.
     *
     * @param a   The first AND's operand.
     * @param b   The second AND's operand.
     * @param <T> The type of arguments to the specified predicate.
     * @return A predicate that represents the logical AND of the first and second predicates.
     * @see Predicate#and(Predicate)
     */
    public static <T> @NotNull Predicate<T> and(@NotNull Predicate<T> a, @NotNull Predicate<T> b)
    {
        return a.and(b);
    }

    /**
     * Returns a composed predicate that represents the logical AND of the first and second predicates.
     *
     * @param a   The first AND's operand.
     * @param b   The second AND's operand.
     * @param <X> The type of arguments to the specified predicate.
     * @param <Y> The type of arguments to the specified predicate.
     * @return A predicate that represents the logical AND of the first and second predicates.
     * @see PairPredicate#and(PairPredicate)
     */
    public static <X, Y> @NotNull PairPredicate<X, Y> and(@NotNull PairPredicate<X, Y> a, @NotNull PairPredicate<X, Y> b)
    {
        return a.and(b);
    }

    /**
     * Returns a composed predicate that represents the logical OR of the first and second predicates.
     *
     * @param a   The first OR's operand.
     * @param b   The second OR's operand.
     * @param <T> The type of arguments to the specified predicate.
     * @return A predicate that represents the logical OR of the first and second predicates.
     * @see Predicate#or(Predicate)
     */
    public static <T> @NotNull Predicate<T> or(@NotNull Predicate<T> a, @NotNull Predicate<T> b)
    {
        return a.or(b);
    }

    /**
     * Returns a composed predicate that represents the logical OR of the first and second predicates.
     *
     * @param a   The first OR's operand.
     * @param b   The second OR's operand.
     * @param <X> The type of arguments to the specified predicate.
     * @param <Y> The type of arguments to the specified predicate.
     * @return A predicate that represents the logical OR of the first and second predicates.
     * @see PairPredicate#or(PairPredicate)
     */
    public static <X, Y> @NotNull PairPredicate<X, Y> or(@NotNull PairPredicate<X, Y> a, @NotNull PairPredicate<X, Y> b)
    {
        return a.or(b);
    }
}
