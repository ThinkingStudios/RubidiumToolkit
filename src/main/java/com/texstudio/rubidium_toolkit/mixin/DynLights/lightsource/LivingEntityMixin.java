package com.texstudio.rubidium_toolkit.mixin.DynLights.lightsource;

import com.texstudio.rubidium_toolkit.features.DynLights.DynamicLightSource;
import com.texstudio.rubidium_toolkit.features.DynLights.RubidiumDynLights;
import com.texstudio.rubidium_toolkit.features.DynLights.api.DynamicLightHandlers;
import com.texstudio.rubidium_toolkit.features.DynLights.config.DynamicLightsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DynamicLightSource {
	@Unique
	protected int lambdynlights$luminance;

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	public void dynamicLightTick() {
		if (!DynamicLightsConfig.TileEntityLighting.get() || !DynamicLightHandlers.canLightUp(this)) {
			this.lambdynlights$luminance = 0;
			return;
		}

		if (this.isOnFire() || this.isCurrentlyGlowing()) {
			this.lambdynlights$luminance = 15;
		} else {
			int luminance = 0;
			var eyePos = new BlockPos(this.getX(), this.getEyeY(), this.getZ());
			boolean submergedInFluid = !this.level.getFluidState(eyePos).isEmpty();
			for (var equipped : this.getAllSlots()) {
				if (!equipped.isEmpty())
					luminance = Math.max(luminance, RubidiumDynLights.getLuminanceFromItemStack(equipped, submergedInFluid));
			}

			this.lambdynlights$luminance = luminance;
		}

		int luminance = DynamicLightHandlers.getLuminanceFrom(this);
		if (luminance > this.lambdynlights$luminance)
			this.lambdynlights$luminance = luminance;
	}

	@Override
	public int getLuminance() {
		return this.lambdynlights$luminance;
	}
}
