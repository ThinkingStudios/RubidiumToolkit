/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.rubidium_toolkit.mixin.dynlights.lightsource;

import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.dynlights.DynamicLightSource;
import org.thinkingstudio.rubidium_toolkit.dynlights.api.DynamicLightHandlers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractHurtingProjectile.class)
public abstract class ExplosiveProjectileEntityMixin extends Entity implements DynamicLightSource {
	public ExplosiveProjectileEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	public void dynamicLightTick() {
		if (!this.isDynamicLightEnabled())
			this.setDynamicLightEnabled(true);
	}

	@Override
	public int getLuminance() {
		if (ToolkitConfig.TileEntityLighting.get() && DynamicLightHandlers.canLightUp(this))
			return 14;
		return 0;
	}
}
