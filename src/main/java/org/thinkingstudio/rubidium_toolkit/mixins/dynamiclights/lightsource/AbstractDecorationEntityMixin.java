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
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.dynamiclights.DynamicLightSource;
import org.thinkingstudio.rubidium_toolkit.features.dynamiclights.DynLightsFeatures;
import org.thinkingstudio.rubidium_toolkit.features.dynamiclights.api.DynamicLightHandlers;

@Mixin(HangingEntity.class)
public abstract class AbstractDecorationEntityMixin extends Entity implements DynamicLightSource {
	public AbstractDecorationEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		// We do not want to update the entity on the server.
		if (this.getCommandSenderWorld().isClientSide()) {
			if (this.isRemoved()) {
				this.setDynamicLightEnabled(false);
			} else {
				if (!RubidiumToolkitConfig.blockEntityLighting.get() || !DynamicLightHandlers.canLightUp(this))
					this.resetDynamicLight();
				else
					this.dynamicLightTick();
				DynLightsFeatures.updateTracking(this);
			}
		}
	}
}
