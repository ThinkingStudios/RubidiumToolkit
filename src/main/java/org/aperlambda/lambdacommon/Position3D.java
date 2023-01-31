/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a position in a 3 dimensions coordinate system.
 *
 * @version 1.8.0
 * @see Position
 * @see Position2D
 */
public class Position3D extends Position2D
{
    private int z;

    /**
     * Creates a new position object in 3D.
     *
     * @param x The value of the coordinate X.
     * @param y The value of the coordinate Y.
     * @param z The value of the coordinate Z.
     * @return A new position in 3D.
     * @see Position3D#Position3D(int, int, int)
     */
    public static Position3D of(int x, int y, int z)
    {
        return new Position3D(x, y, z);
    }

    public Position3D(int x, int y, int z)
    {
        super(x, y);
        this.z = z;
    }

    public int getZ()
    {
        return z;
    }

    public void setZ(int z)
    {
        this.z = z;
    }

    @Override
    public int getDimensions()
    {
        return 3;
    }

    @NotNull
    @Override
    public JsonObject toJson()
    {
        JsonObject json = super.toJson();
        json.addProperty("z", z);
        return json;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Position3D other = (Position3D) o;

        return this.z == other.z;
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + z;
        return result;
    }
}
