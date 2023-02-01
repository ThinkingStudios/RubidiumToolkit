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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;

@Mixin(TNTEntity.class)
public abstract class TntEntityMixin extends Entity implements DynamicLightSource {
    @Shadow
    private int life;

    //private double lambdynlights_startFuseTimer = 80.0;
    private int lambdynlights_luminance;

    public TntEntityMixin(EntityType<?> type, World world)
    {
        super(type, world);
    }

    //@Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At("TAIL"))
    //private void onNew(EntityType<? extends TNTEntity> entityType, World world, CallbackInfo ci)
    //{
    //    this.lambdynlights_startFuseTimer = this.life;
    //}

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        // We do not want to update the entity on the server.
        if (this.getCommandSenderWorld().isClientSide()) {
            if (!ToolkitConfig.EntityLighting.get())
                return;

            if (this.removed) {
                this.setDynamicLightEnabled(false);
            } else {
                this.dynamicLightTick();
                DynamicLightsFeatures.updateTracking(this);
            }
        }
    }

    @Override
    public void dynamicLightTick() {
        if (this.isOnFire()) {
            this.lambdynlights_luminance = 15;
        } else {
            this.lambdynlights_luminance = (int) (-(life * life) * 10.0) + 10;
        }
    }

    @Override
    public int getLuminance()
    {
        return this.lambdynlights_luminance;
    }
}