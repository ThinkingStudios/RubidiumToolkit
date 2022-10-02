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
import org.jetbrains.annotations.NotNull;

import static org.aperlambda.lambdacommon.LambdaConstants.GSON_PRETTY;

/**
 * Represents a virtual JSON configuration.
 *
 * @author LambdAurora
 * @version 1.8.0
 * @since 1.3.0
 */
public class VirtualJsonConfig implements BaseJsonConfig
{
    private JsonObject config = new JsonObject();

    public VirtualJsonConfig()
    {
    }

    public VirtualJsonConfig(@NotNull JsonObject root)
    {
        this.config = root;
    }

    @Override
    public <T> T get(String key, T def, Class<T> type)
    {
        return JsonConfig.jsonGet(this.config, key, def, type);
    }

    @Override
    public void set(String key, Object value)
    {
        if (key.contains(".")) {
            String[] path = key.split("\\.");
            // Starts at root.
            JsonObject currentObject = this.config;

            for (int i = 0; i < path.length - 1; i++) {
                String currentKey = path[i];

                if (!currentObject.has(currentKey))
                    currentObject.add(currentKey, new JsonObject());

                currentObject = currentObject.getAsJsonObject(currentKey);
            }

            currentObject.add(path[path.length - 1], GSON_PRETTY.toJsonTree(value));
        } else
            config.add(key, GSON_PRETTY.toJsonTree(value));
    }

    @Override
    public <T> T at(String path, T def, Class<T> type)
    {
        return JsonConfig.jsonAt(this.config, path, def, type);
    }

    @Override
    public boolean isVirtual()
    {
        return true;
    }

    /**
     * Gets the root JSON Object of the configuration.
     *
     * @return The root JSON Object of the config.
     */
    @Override
    public JsonObject getConfig()
    {
        return this.config;
    }
}
