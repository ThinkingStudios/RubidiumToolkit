package org.thinkingstudio.rubidium_toolkit.mixins.EntityDistance;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.EntityDistance.DistanceUtility;

@Mixin(EntityRendererManager.class)
public class MaxDistanceEntity {
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void shouldDoRender(E entity, ClippingHelper clippingHelper, double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Boolean> cir)
    {
        if (!RubidiumToolkitConfig.enableDistanceChecks.get())
            return;

        if (!DistanceUtility.isEntityWithinDistance(
                entity,
                cameraX,
                cameraY,
                cameraZ,
                RubidiumToolkitConfig.maxEntityRenderDistanceY.get(),
                RubidiumToolkitConfig.maxEntityRenderDistanceSquare.get()
        ))
        {
            cir.cancel();
        }
    }
}
