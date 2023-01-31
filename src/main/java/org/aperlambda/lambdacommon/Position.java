/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon;

import org.aperlambda.lambdacommon.documents.Element;
import org.aperlambda.lambdacommon.utils.Serializable;

/**
 * Represents a position in a coordinate system.
 */
public interface Position extends Serializable, Element
{
    /**
     * Gets the number of dimensions of the position.
     * Example: 2 for 2D, 3 for 3D, etc...
     *
     * @return The number of dimensions of the position.
     */
    int getDimensions();
}
