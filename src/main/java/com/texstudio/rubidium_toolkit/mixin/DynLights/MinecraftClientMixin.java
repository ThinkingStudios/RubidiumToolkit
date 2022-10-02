package com.texstudio.rubidium_toolkit.mixin.DynLights;

import com.texstudio.rubidium_toolkit.features.DynLights.RubidiumDynLights;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to MinecraftClient.
 * <p>
 * Goal: clear light sources cache when changing world.
 *
 * @author LambdAurora
 * @version 1.3.2
 * @since 1.3.2
 */
@Mixin(Minecraft.class)
public class MinecraftClientMixin {
	@Inject(method = "setLevel", at = @At("HEAD"))
	private void onSetWorld(ClientLevel world, CallbackInfo ci) {
		RubidiumDynLights.get().clearLightSources();
	}
}
