package org.thinkingstudio.rubidium_toolkit.mixins.totaldarkness;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import org.thinkingstudio.rubidium_toolkit.features.TotalDarkness.Darkness;
import org.thinkingstudio.rubidium_toolkit.features.TotalDarkness.LightmapAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer
{
	@Final
	@Shadow
	private MinecraftClient minecraft;

	@Final
	@Shadow
	private LightmapTextureManager lightTexture;

	@Inject(method = "renderWorld", at = @At(value = "HEAD"))
	private void onRenderWorld(float tickDelta, long nanos, MatrixStack matrixStack, CallbackInfo ci) {
		final LightmapAccess lightmap = (LightmapAccess) lightTexture;

		if (lightmap.darkness_isDirty()) {
			minecraft.getProfiler().push("lightTex");
			Darkness.updateLuminance(tickDelta, minecraft, (GameRenderer) (Object) this, lightmap.darkness_prevFlicker());
			minecraft.getProfiler().pop();
		}
	}
}
