package org.thinkingstudio.rubidium_toolkit.mixin.dynamic_lights.lightsource;

import org.thinkingstudio.rubidium_toolkit.features.dynamic_lights.DynamicLightSource;
import org.thinkingstudio.rubidium_toolkit.features.dynamic_lights.api.DynamicLightHandlers;
import org.thinkingstudio.rubidium_toolkit.features.dynamic_lights.config.DynamicLightsConfig;
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
