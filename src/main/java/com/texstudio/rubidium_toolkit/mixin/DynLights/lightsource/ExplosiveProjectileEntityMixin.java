package com.texstudio.rubidium_toolkit.mixin.DynLights.lightsource;

import com.texstudio.rubidium_toolkit.features.DynLights.DynamicLightSource;
import com.texstudio.rubidium_toolkit.features.DynLights.api.DynamicLightHandlers;
import com.texstudio.rubidium_toolkit.features.DynLights.config.DynamicLightsConfig;
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
		if (DynamicLightsConfig.TileEntityLighting.get() && DynamicLightHandlers.canLightUp(this))
			return 14;
		return 0;
	}
}
