package org.thinkingstudio.rubidium_toolkit.mixins.entitydistance;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.entitydistance.DistanceUtility;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void shouldDoRender(E entity, Frustum clippingHelper, double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Boolean> cir) {
        if (!ToolkitConfig.enableDistanceChecks.get())
            return;

        if (!DistanceUtility.isEntityWithinDistance(
                entity,
                cameraX,
                cameraY,
                cameraZ,
                ToolkitConfig.maxEntityRenderDistanceY.get(),
                ToolkitConfig.maxEntityRenderDistanceSquare.get()
        ))
        {
            cir.cancel();
        }
    }
}

