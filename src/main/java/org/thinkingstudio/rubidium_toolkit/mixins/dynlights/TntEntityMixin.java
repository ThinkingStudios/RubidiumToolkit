package org.thinkingstudio.rubidium_toolkit.mixins.dynlights;

import net.minecraft.entity.TntEntity;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightSource;

import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightsFeature;
//import com.texstudio.rubidium_toolkit.features.dynlights.config.DynamicLightsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin extends Entity implements DynamicLightSource {
    @Shadow
    private int fuseTimer;

    //private double lambdynlights_startFuseTimer = 80.0;
    private int lambdynlights_luminance;

    public TntEntityMixin(EntityType<?> type, World world) {
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
        if (this.getEntityWorld().isClient()) {
            if (!RubidiumToolkitConfig.entityLighting.get())
                return;

            if (this.removed) {
                this.setDynamicLightEnabled(false);
            } else {
                this.dynamicLightTick();
                DynamicLightsFeature.updateTracking(this);
            }
        }
    }

    @Override
    public void dynamicLightTick() {
        if (this.isOnFire()) {
            this.lambdynlights_luminance = 15;
        } else {
            this.lambdynlights_luminance = (int) (-(fuseTimer * fuseTimer) * 10.0) + 10;
        }
    }

    @Override
    public int getLuminance() {
        return this.lambdynlights_luminance;
    }
}
