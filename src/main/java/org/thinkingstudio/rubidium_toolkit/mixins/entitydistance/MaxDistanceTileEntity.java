package org.thinkingstudio.rubidium_toolkit.mixins.entitydistance;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.entitydistance.DistanceUtility;

@Mixin(TileEntityRendererDispatcher.class)
public class MaxDistanceTileEntity {

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public <E extends TileEntity> void render(E entity, float val, MatrixStack matrix, IRenderTypeBuffer p_228850_4_, CallbackInfo ci) {
        if (!ToolkitConfig.enableDistanceChecks.get())
            return;

        TileEntityRendererDispatcher thisObj = (TileEntityRendererDispatcher) (Object) this;

        if (!DistanceUtility.isEntityWithinDistance(
                entity.getBlockPos(),
                thisObj.camera.getPosition(),
                ToolkitConfig.maxTileEntityRenderDistanceY.get(),
                ToolkitConfig.maxTileEntityRenderDistanceSquare.get()
        ))
        {
            ci.cancel();
        }
    }

}
