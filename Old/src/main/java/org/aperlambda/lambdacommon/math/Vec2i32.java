/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.math;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a 2D vector
 *
 * @version 1.8.0
 * @since 1.7.2
 */
public class Vec2i32 implements Comparable<Vec2i32>
{
    public static final Vec2i32 ZERO = new Vec2i32(0, 0);
    public final        int     x;
    public final        int     y;

    public Vec2i32(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Vec2i32(double x, double y)
    {
        this(MathHelper.floor(x), MathHelper.floor(y));
    }

    /**
     * Returns the standard of this vector.
     *
     * @return The standard of this vector.
     */
    public double standard()
    {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    /**
     * Performs the scalar product of this vector and another vector.
     *
     * @param b The other vector.
     * @return The scalar product.
     */
    public int scalarProduct(Vec2i32 b)
    {
        return this.x * b.x + this.y * b.y;
    }

    /**
     * Adds another vector to this vector.
     *
     * @param other The other vector.
     * @return A new vector in result of the addition of the two vectors.
     */
    public Vec2i32 add(Vec2i32 other)
    {
        if (other == null)
            other = ZERO;
        return new Vec2i32(this.x + other.x, this.y + other.y);
    }

    /**
     * Subtracts another vector to this vector.
     *
     * @param other The other vector.
     * @return A new vector in result of the subtraction of the two vectors.
     */
    public Vec2i32 subtract(Vec2i32 other)
    {
        if (other == null)
            other = ZERO;
        return new Vec2i32(this.x - other.x, this.y - other.y);
    }

    /**
     * Negates this vector.
     *
     * @return A new vector which is the negation of this vector.
     */
    public Vec2i32 negate()
    {
        return new Vec2i32(-this.x, -this.y);
    }

    /**
     * Multiplies this vector to a scalar.
     *
     * @param lambda The scalar to multiply.
     * @return A new vector which is the multiplication between the scalar and the vector.
     */
    public Vec2i32 multiply(int lambda)
    {
        return new Vec2i32(lambda * this.x, lambda * this.y);
    }

    @Override
    public int compareTo(@NotNull Vec2i32 vec2i32)
    {
        return this.y == vec2i32.y ? this.x - vec2i32.x : this.y - vec2i32.y;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object) {
            return true;
        } else if (!(object instanceof Vec2i32)) {
            return false;
        } else {
            Vec2i32 other = (Vec2i32) object;
            if (this.x != other.x)
                return false;
            return this.y == other.y;
        }
    }
}
