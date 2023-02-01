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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DynamicLightSource {
    @Shadow
    public abstract boolean isSpectator();

    private int   lambdynlights_luminance;
    private World lambdynlights_lastWorld;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void dynamicLightTick() {
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

        if (this.isSpectator())
            this.lambdynlights_luminance = 0;

        if (this.lambdynlights_lastWorld != this.getCommandSenderWorld()) {
            this.lambdynlights_lastWorld = this.getCommandSenderWorld();
            this.lambdynlights_luminance = 0;
        }
    }

    @Override
    public int getLuminance() {
        return this.lambdynlights_luminance;
    }
}
