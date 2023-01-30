package org.thinkingstudio.rubidium_toolkit.mixins.dynlights;

import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightSource;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractHurtingProjectile.class)
public abstract class ExplosiveProjectileEntityMixin implements DynamicLightSource {
    @Override
    public void dynamicLightTick() {
        if (!this.isDynamicLightEnabled())
            this.setDynamicLightEnabled(true);
    }

    @Override
    public int getLuminance() {
        if (ToolkitConfig.entityLighting.get())
            return 14;
        return 0;
    }
}
