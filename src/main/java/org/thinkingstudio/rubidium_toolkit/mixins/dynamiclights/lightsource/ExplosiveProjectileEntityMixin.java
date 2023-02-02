/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.rubidium_toolkit.mixins.dynamiclights.lightsource;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.dynamiclights.DynamicLightSource;
import org.thinkingstudio.rubidium_toolkit.features.dynamiclights.api.DynamicLightHandlers;

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
		if (ToolkitConfig.blockEntityLighting.get() && DynamicLightHandlers.canLightUp(this))
			return 14;
		return 0;
	}
}
