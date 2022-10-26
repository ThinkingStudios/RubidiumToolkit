package com.texstudio.rubidium_toolkit.mixins.DynamicLights;

import com.texstudio.rubidium_toolkit.features.DynamicLights.DynamicLightSource;
//import com.texstudio.rubidium_toolkit.features.DynamicLights.config.DynamicLightsConfig;
import com.texstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
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
        if (RubidiumToolkitConfig.EntityLighting.get())
            return 14;
        return 0;
    }
}
