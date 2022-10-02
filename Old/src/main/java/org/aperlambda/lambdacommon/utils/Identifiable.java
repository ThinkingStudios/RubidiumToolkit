/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.utils;

import org.aperlambda.lambdacommon.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object with a resource name.
 *
 * @version 1.8.0
 * @since 1.4.6
 */
public interface Identifiable extends Nameable
{
    /**
     * Gets the resource name of the object.
     *
     * @return The resource name of the object.
     */
    @NotNull Identifier getIdentifier();

    @Override
    default @NotNull String getName()
    {
        return this.getIdentifier().getName();
    }
}
