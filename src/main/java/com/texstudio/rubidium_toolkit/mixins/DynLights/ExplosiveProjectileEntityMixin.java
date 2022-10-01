package com.texstudio.rubidium_toolkit.mixins.DynLights;

import com.texstudio.rubidium_toolkit.dynamic_lights.DynamicLightSource;
import com.texstudio.rubidium_toolkit.dynamic_lights.config.DynamicLightsConfig;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DamagingProjectileEntity.class)
public abstract class ExplosiveProjectileEntityMixin implements DynamicLightSource
{
    @Override
    public void dynamicLightTick()
    {
        if (!this.isDynamicLightEnabled())
            this.setDynamicLightEnabled(true);
    }

    @Override
    public int getLuminance()
    {
        if (DynamicLightsConfig.EntityLighting.get())
            return 14;
        return 0;
    }
}
