package org.thinkingstudio.rubidium_toolkit.mixins.dynamic_lights;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.profiler.Profiler;
import org.thinkingstudio.rubidium_toolkit.features.DynamicLights.DynamicLightSource;
import org.thinkingstudio.rubidium_toolkit.features.DynamicLights.DynamicLightsFeature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(World.class)
public abstract class WorldMixin
{
    @Shadow
    public abstract boolean isClientSide();

    @Inject(
            method = "tickBlockEntities",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/ITickableTileEntity;tick()V", shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void onBlockEntityTick(CallbackInfo ci, Profiler iprofiler, Iterator iterator, BlockEntity tileentity, BlockPos blockpos)
    {
        if (this.isClientSide() && DynamicLightsFeature.isEnabled()) {
            ((DynamicLightSource) tileentity).dynamicLightTick();
        }
    }
}



