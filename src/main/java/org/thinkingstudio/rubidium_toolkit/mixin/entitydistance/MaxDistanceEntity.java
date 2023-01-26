package org.thinkingstudio.rubidium_toolkit.mixin.entitydistance;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfigClient;
import org.thinkingstudio.rubidium_toolkit.util.DistanceUtility;

@Mixin(EntityRenderDispatcher.class)
public class MaxDistanceEntity
{
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void shouldDoRender(E entity, Frustum clippingHelper, double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Boolean> cir)
    {
        if (!RubidiumToolkitConfigClient.enableDistanceChecks.get())
            return;

        if (!DistanceUtility.isEntityWithinDistance(
                entity,
                cameraX,
                cameraY,
                cameraZ,
                RubidiumToolkitConfigClient.maxEntityRenderDistanceY.get(),
                RubidiumToolkitConfigClient.maxEntityRenderDistanceSquare.get()
        ))
        {
            cir.cancel();
        }
    }
}
