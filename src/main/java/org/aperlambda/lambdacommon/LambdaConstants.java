/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.aperlambda.lambdacommon.utils.Pair;

import java.awt.*;

public class LambdaConstants
{
    /*
        Resources
     */
    public static final Identifier IDENTIFIER_INVALID   = Identifier.IDENTIFIER_INVALID;
    public static final Identifier IDENTIFIER_NOT_FOUND = Identifier.IDENTIFIER_NOT_FOUND;

    /*
        Colors
     */
    public static final Color BACKGROUND_CRASH_COLOR = new Color(0, 0, 107);

    /*
        JSON
     */
    private static final GsonBuilder BASE_GSON   = new GsonBuilder()
            .registerTypeHierarchyAdapter(Identifier.class, new Identifier.IdentifierJsonSerializer())
            .registerTypeHierarchyAdapter(Pair.class, new Pair.JsonPairSerializer())
            .disableHtmlEscaping();
    public static final  Gson        GSON        = BASE_GSON.create();
    public static final  Gson        GSON_PRETTY = BASE_GSON.setPrettyPrinting().create();
    public static final  JsonParser  JSON_PARSER = new JsonParser();
}
