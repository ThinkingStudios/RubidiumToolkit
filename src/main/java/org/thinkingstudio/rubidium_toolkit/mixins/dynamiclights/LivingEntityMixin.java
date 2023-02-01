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
import org.thinkingstudio.rubidium_toolkit.features.dynamiclights.DynamicLightsFeatures;
import org.thinkingstudio.rubidium_toolkit.features.dynamiclights.api.DynamicLightHandlers;
import me.lambdaurora.lambdynlights.config.DynamicLightsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DynamicLightSource
{
    private int lambdynlights_luminance;

    public LivingEntityMixin(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Override
    public void dynamicLightTick()
    {
        if (this.isOnFire() || this.isGlowing()) {
            this.lambdynlights_luminance = 15;
        } else {
            int luminance = 0;
            BlockPos eyePos = new BlockPos(this.getX(), this.getEyeY(), this.getZ());
            boolean submergedInFluid = !this.level.getFluidState(eyePos).isEmpty();
            for (ItemStack equipped : this.getAllSlots()) {
                if (!equipped.isEmpty())
                    luminance = Math.max(luminance, DynamicLightsFeatures.getLuminanceFromItemStack(equipped, submergedInFluid));
            }

            this.lambdynlights_luminance = luminance;
        }

        int luminance = DynamicLightHandlers.getLuminanceFrom(this);
        if (luminance > this.lambdynlights_luminance)
            this.lambdynlights_luminance = luminance;

        if (!ToolkitConfig.EntityLighting.get() && this.getType() != EntityType.PLAYER)
            this.lambdynlights_luminance = 0;
    }

    @Override
    public int getLuminance()
    {
        return this.lambdynlights_luminance;
    }
}
