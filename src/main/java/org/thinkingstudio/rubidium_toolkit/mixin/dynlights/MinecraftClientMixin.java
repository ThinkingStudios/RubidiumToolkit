package org.thinkingstudio.rubidium_toolkit.mixin.dynlights;

import org.thinkingstudio.rubidium_toolkit.features.dynlights.ToolkitDynLights;
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
	private void onSetLevel(ClientLevel world, CallbackInfo ci) {
		ToolkitDynLights.get().clearLightSources();
	}
}
