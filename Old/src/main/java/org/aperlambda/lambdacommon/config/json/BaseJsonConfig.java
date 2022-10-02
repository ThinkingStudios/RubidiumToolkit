/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.config.json;

import com.google.gson.JsonObject;
import org.aperlambda.lambdacommon.config.Config;

/**
 * Represents the base of a JSON configuration.
 *
 * @author lambdaurora
 * @version 1.8.0
 * @since 1.4.10
 */
public interface BaseJsonConfig extends Config<JsonObject>
{
    JsonObject getConfig();
}
