package org.thinkingstudio.rubidium_toolkit.mixin.dynlights.lightsource;

import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightSource;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.ToolkitDynLights;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.api.DynamicLightHandlers;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.config.DynamicLightsConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PrimedTnt.class)
public abstract class TNTEntityMixin extends Entity implements DynamicLightSource {
	@Shadow
	public abstract int getFuse();

	@Unique
	private int startFuseTimer = 80;
	@Unique
	private int lambdynlights$luminance;

	public TNTEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("TAIL"))
	private void onNew(EntityType<? extends PrimedTnt> entityType, Level world, CallbackInfo ci) {
		this.startFuseTimer = this.getFuse();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		// We do not want to update the entity on the server.
		if (this.getCommandSenderWorld().isClientSide()) {
			if (!ToolkitDynLights.isEnabled())
				return;

			if (this.isRemoved()) {
				this.setDynamicLightEnabled(false);
			} else {
				if (!DynamicLightsConfig.TileEntityLighting.get() || !DynamicLightHandlers.canLightUp(this))
					this.resetDynamicLight();
				else
					this.dynamicLightTick();
				ToolkitDynLights.updateTracking(this);
			}
		}
	}

	@Override
	public void dynamicLightTick() {
		if (this.isOnFire()) {
			this.lambdynlights$luminance = 15;
		} else {
			if (ToolkitDynLights.isEnabled()) {
				var fuse = this.getFuse() / this.startFuseTimer;
				this.lambdynlights$luminance = (int) (-(fuse * fuse) * 10.0) + 10;
			} else {
				this.lambdynlights$luminance = 10;
			}
		}
	}

	@Override
	public int getLuminance() {
		return this.lambdynlights$luminance;
	}
}
