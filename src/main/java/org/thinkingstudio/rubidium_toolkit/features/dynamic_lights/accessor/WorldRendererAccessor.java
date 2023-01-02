package org.thinkingstudio.rubidium_toolkit.features.dynamic_lights.accessor;

/**
 * Represents an accessor for WorldRenderer.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
public interface WorldRendererAccessor {
	/**
	 * Schedules a chunk rebuild.
	 *
	 * @param x X coordinates of the chunk
	 * @param y Y coordinates of the chunk
	 * @param z Z coordinates of the chunk
	 * @param important {@code true} if important, else {@code false}
	 */
	void dynlights_setSectionDirty(int x, int y, int z, boolean important);
}
