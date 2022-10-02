/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.math;

/**
 * Contains math helpers.
 *
 * @version 1.7.2
 * @since 1.7.2
 */
public class MathHelper
{
    private MathHelper()
    {
    }

    public static int floor(double value)
    {
        int res = (int) value;
        return value < (double) res ? res - 1 : res;
    }
}
