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
 * Represents a 3D vector
 *
 * @version 1.8.0
 * @since 1.7.2
 */
public class Vec3i32 implements Comparable<Vec3i32>
{
    public static final Vec3i32 ZERO = new Vec3i32(0, 0, 0);
    public final        int     x;
    public final        int     y;
    public final        int     z;

    public Vec3i32(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3i32(double x, double y, double z)
    {
        this(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

    /**
     * Returns the standard of this vector.
     *
     * @return The standard of this vector.
     */
    public double standard()
    {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    /**
     * Performs the scalar product of this vector and another vector.
     *
     * @param b The other vector.
     * @return The scalar product.
     */
    public int scalarProduct(Vec3i32 b)
    {
        return this.x * b.x + this.y * b.y + this.z * b.z;
    }

    /**
     * Adds another vector to this vector.
     *
     * @param other The other vector.
     * @return A new vector in result of the addition of the two vectors.
     */
    public Vec3i32 add(Vec3i32 other)
    {
        if (other == null)
            other = ZERO;
        return new Vec3i32(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Subtracts another vector to this vector.
     *
     * @param other The other vector.
     * @return A new vector in result of the subtraction of the two vectors.
     */
    public Vec3i32 subtract(Vec3i32 other)
    {
        if (other == null)
            other = ZERO;
        return new Vec3i32(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Negates this vector.
     *
     * @return A new vector which is the negation of this vector.
     */
    public Vec3i32 negate()
    {
        return new Vec3i32(-this.x, -this.y, -this.z);
    }

    /**
     * Multiplies this vector to a scalar.
     *
     * @param lambda The scalar to multiply.
     * @return A new vector which is the multiplication between the scalar and the vector.
     */
    public Vec3i32 multiply(int lambda)
    {
        return new Vec3i32(lambda * this.x, lambda * this.y, lambda * this.z);
    }

    @Override
    public int compareTo(@NotNull Vec3i32 vec3i32)
    {
        if (this.y == vec3i32.y) {
            return this.z == vec3i32.z ? this.x - vec3i32.x : this.z - vec3i32.z;
        }
        return this.y - vec3i32.y;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object) {
            return true;
        } else if (!(object instanceof Vec3i32)) {
            return false;
        } else {
            Vec3i32 other = (Vec3i32) object;
            if (this.x != other.x)
                return false;
            if (this.z != other.z)
                return false;
            return this.y == other.y;
        }
    }
}
