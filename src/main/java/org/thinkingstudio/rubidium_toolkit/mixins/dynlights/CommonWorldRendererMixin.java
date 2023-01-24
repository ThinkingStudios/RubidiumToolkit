package org.thinkingstudio.rubidium_toolkit.mixins.dynlights;

import net.minecraft.client.render.WorldRenderer;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightsFeature;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.accessor.WorldRendererAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WorldRenderer.class, priority = 900)
public abstract class CommonWorldRendererMixin implements WorldRendererAccessor
{
    @Invoker("setSectionDirty")
    @Override
    public abstract void dynlights_setSectionDirty(int x, int y, int z, boolean important);



    @Inject(
            method = "getLightColor(Lnet/minecraft/world/IBlockDisplayReader;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I",
            at = @At("TAIL"),
            cancellable = true
    )
    private static void onGetLightmapCoordinates(BlockRenderView world, BlockState j, BlockPos pos, CallbackInfoReturnable<Integer> cir)
    {
        if (!world.getBlockState(pos).isSolidBlock(world, pos) && DynamicLightsFeature.isEnabled())
        {
            int vanilla = cir.getReturnValue();
            int value = DynamicLightsFeature.getLightmapWithDynamicLight(pos, vanilla);

            cir.setReturnValue(value);
        }
    }
}
