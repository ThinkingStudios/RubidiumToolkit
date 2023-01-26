package org.thinkingstudio.rubidium_toolkit.mixins.dynlights;

import net.minecraft.client.MinecraftClient;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightsFeature;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
    @Inject(method = "setWorld", at = @At("HEAD"))
    private void onSetWorld(ClientWorld world, CallbackInfo ci) {
        DynamicLightsFeature.clearLightSources();
    }
}
