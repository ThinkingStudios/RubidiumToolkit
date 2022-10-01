/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.TimerTask;

/**
 * Lots of utilities.
 *
 * @author LambdAurora
 * @version 1.8.0
 * @since 1.5.0
 */
public class LambdaUtils
{
    private LambdaUtils() throws IllegalAccessException
    {
        throw new IllegalAccessException("Cannot instantiate LambdaUtils: it contains only static methods.");
    }

    /**
     * Makes a new Optional object from a String.
     *
     * @param value The String value.
     * @return An {@code Optional} value, empty: if the value is null or if the value is empty.
     */
    public static Optional<String> makeOptionalFromString(@Nullable String value)
    {
        return Optional.ofNullable(value).filter(str -> !str.isEmpty());
    }

    /**
     * Parses an integer from a string.
     *
     * @param value A {@code String} that represents an integer.
     * @return An {@code Optional} integer.
     */
    public static Optional<Integer> parseOptionalIntFromString(@NotNull String value)
    {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses an integer from a string. If the value is not a integer it returns 0
     *
     * @param value A {@code String} that represents an integer.
     * @return An {@code integer}.
     */
    public static int parseIntFromString(@NotNull String value)
    {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Makes a new {@code TimerTask} from a {@code Runnable} (lambda).
     *
     * @param runnable Code to run.
     * @return A new {@code TimerTask} which run {@code Runnable} when {@code run} function called.
     */
    public static TimerTask newTimerTaskFromLambda(Runnable runnable)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                runnable.run();
            }
        };
    }
}
