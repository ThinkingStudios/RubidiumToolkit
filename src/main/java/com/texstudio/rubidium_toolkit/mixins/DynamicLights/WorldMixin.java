package com.texstudio.rubidium_toolkit.mixins.DynamicLights;

import com.texstudio.rubidium_toolkit.DynamicLightSource;
import com.texstudio.rubidium_toolkit.features.DynamicLights.DynamicLightsFeature;
import net.minecraft.profiler.IProfiler;
import net.minecraft.tileentity.TileEntity;
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
    private void onBlockEntityTick(CallbackInfo ci, IProfiler iprofiler, Iterator iterator, TileEntity tileentity, BlockPos blockpos)
    {
        if (this.isClientSide() && DynamicLightsFeature.isEnabled()) {
            ((DynamicLightSource) tileentity).dynamicLightTick();
        }
    }
}



