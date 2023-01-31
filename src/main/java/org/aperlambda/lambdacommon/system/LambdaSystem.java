/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.system;

import java.io.File;

public final class LambdaSystem
{
    private static final File USER_DIR = new File(getUserDirStr());

    private LambdaSystem()
    {}

    public static OS getOs()
    {
        return OS.getCurrentPlatform();
    }

    public static String getUserDirStr()
    {
        return System.getProperty("user.home");
    }

    public static File getUserDir()
    {
        return USER_DIR;
    }
}
