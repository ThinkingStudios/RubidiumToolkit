package com.texstudio.rubidium_toolkit.mixins.TotalDarkness;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.texstudio.rubidium_toolkit.features.TotalDarkness.Darkness;
import com.texstudio.rubidium_toolkit.features.TotalDarkness.LightmapAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
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
	private Minecraft minecraft;

	@Final
	@Shadow
	private LightTexture lightTexture;

	@Inject(method = "renderLevel", at = @At(value = "HEAD"))
	private void onRenderWorld(float tickDelta, long nanos, MatrixStack matrixStack, CallbackInfo ci) {
		final LightmapAccess lightmap = (LightmapAccess) lightTexture;

		if (lightmap.darkness_isDirty()) {
			minecraft.getProfiler().push("lightTex");
			Darkness.updateLuminance(tickDelta, minecraft, (GameRenderer) (Object) this, lightmap.darkness_prevFlicker());
			minecraft.getProfiler().pop();
		}
	}
}
