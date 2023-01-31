/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.utils;

/**
 * Represents a cancellable object.
 */
public interface Cancellable
{
    /**
     * Checks whether cancelled.
     * @return True if cancelled, else false.
     */
    boolean isCancelled();

    /**
     * Sets whether cancelled or not.
     * @param cancelled True if cancelled, else false.
     */
    void setCancelled(boolean cancelled);
}
