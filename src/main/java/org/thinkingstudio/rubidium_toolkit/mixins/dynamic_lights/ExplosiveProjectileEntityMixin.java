package org.thinkingstudio.rubidium_toolkit.mixins.dynamic_lights;

import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import org.thinkingstudio.rubidium_toolkit.features.DynamicLights.DynamicLightSource;
//import com.texstudio.rubidium_toolkit.features.dynamic_lights.config.DynamicLightsConfig;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ExplosiveProjectileEntity.class)
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
