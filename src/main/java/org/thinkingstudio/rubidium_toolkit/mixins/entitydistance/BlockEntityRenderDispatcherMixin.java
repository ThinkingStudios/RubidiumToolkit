package org.thinkingstudio.rubidium_toolkit.mixins.entitydistance;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.entitydistance.DistanceUtility;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public <E extends BlockEntity> void render(E entity, float val, MatrixStack matrix, VertexConsumerProvider arg3, CallbackInfo ci) {
        if (!RubidiumToolkitConfig.enableDistanceChecks.get())
            return;

        BlockEntityRenderDispatcher thisObj = (BlockEntityRenderDispatcher) (Object) this;

        if (!DistanceUtility.isEntityWithinDistance(
                entity.getPos(),
                thisObj.camera.getPos(),
                RubidiumToolkitConfig.maxBlockEntityRenderDistanceY.get(),
                RubidiumToolkitConfig.maxBlockEntityRenderDistanceSquare.get()
        ))
        {
            ci.cancel();
        }
    }

}
