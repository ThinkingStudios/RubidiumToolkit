/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.rubidium_toolkit.features.dynamiclights.api;

import org.thinkingstudio.rubidium_toolkit.features.dynamiclights.api.item.ItemLightSources;
import org.thinkingstudio.rubidium_toolkit.features.dynamiclights.api.item.ItemLightSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * Represents the entrypoint for LambDynamicLights API.
 *
 * @author LambdAurora
 * @version 1.3.2
 * @since 1.3.2
 */
public interface DynamicLightsInitializer {
	/**
	 * Method called when LambDynamicLights is initialized to register custom dynamic light handlers and item light sources.
	 *
	 * @see DynamicLightHandlers#registerDynamicLightHandler(EntityType, DynamicLightHandler)
	 * @see DynamicLightHandlers#registerDynamicLightHandler(BlockEntityType, DynamicLightHandler)
	 * @see ItemLightSources#registerItemLightSource(ItemLightSource)
	 */
	void onInitializeDynamicLights();
}
