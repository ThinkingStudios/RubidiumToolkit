package com.texstudio.rubidium_toolkit.mixins.DynamicLights;

import com.texstudio.rubidium_toolkit.features.DynamicLights.DynamicLightsFeature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin
{
    @Inject(method = "updateLevelInEngines", at = @At("HEAD"))
    private void onSetWorld(ClientWorld world, CallbackInfo ci) {
        DynamicLightsFeature.clearLightSources();
    }
}
