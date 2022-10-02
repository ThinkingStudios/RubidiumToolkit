package com.texstudio.rubidium_toolkit.dynamic_lights.accessor;

/**
 * Represents an accessor for WorldRenderer.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
public interface WorldRendererAccessor
{
    /**
     * Schedules a chunk rebuild.
     *
     * @param x         X coordinates of the chunk.
     * @param y         Y coordinates of the chunk.
     * @param z         Z coordinates of the chunk.
     * @param important True if important, else false.
     */
    void dynlights_setSectionDirty(int x, int y, int z, boolean important);
}
