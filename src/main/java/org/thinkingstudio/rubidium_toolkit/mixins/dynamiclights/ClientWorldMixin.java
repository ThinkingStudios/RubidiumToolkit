/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.rubidium_toolkit.mixins.dynamiclights;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.LevelEntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thinkingstudio.rubidium_toolkit.features.dynamiclights.DynamicLightSource;

@Mixin(ClientLevel.class)
public abstract class ClientWorldMixin {
	@Shadow
	protected abstract LevelEntityGetter<Entity> getEntities();

	@Inject(method = "removeEntity", at = @At("HEAD"))
	private void onFinishRemovingEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci) {
		var entity = this.getEntities().get(entityId);
		if (entity != null) {
			var dls = (DynamicLightSource) entity;
			dls.setDynamicLightEnabled(false);
		}
	}
}
