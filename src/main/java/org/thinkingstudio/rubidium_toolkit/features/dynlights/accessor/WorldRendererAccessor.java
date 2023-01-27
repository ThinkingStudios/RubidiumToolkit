package org.thinkingstudio.rubidium_toolkit.features.dynlights.accessor;

/**
 * Represents an accessor for WorldRenderer.
 */
public interface WorldRendererAccessor {
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
