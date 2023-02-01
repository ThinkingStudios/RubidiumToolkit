/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.rubidium_toolkit.mixins.dynamiclights;

import org.thinkingstudio.rubidium_toolkit.features.dynamiclights.DynamicLightSource;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;

@Mixin(DamagingProjectileEntity.class)
public abstract class ExplosiveProjectileEntityMixin implements DynamicLightSource {
    @Override
    public void dynamicLightTick() {
        if (!this.isDynamicLightEnabled())
            this.setDynamicLightEnabled(true);
    }

    @Override
    public int getLuminance() {
        if (ToolkitConfig.EntityLighting.get())
            return 14;
        return 0;
    }
}
