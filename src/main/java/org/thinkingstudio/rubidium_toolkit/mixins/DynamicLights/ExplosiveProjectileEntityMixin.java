package org.thinkingstudio.rubidium_toolkit.mixins.DynamicLights;

//import com.texstudio.rubidium_toolkit.features.DynamicLights.config.DynamicLightsConfig;

import net.minecraft.entity.projectile.DamagingProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.DynamicLights.DynamicLightSource;

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
