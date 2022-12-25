package org.thinkingstudio.rubidium_toolkit.mixins.TotalDarkness;

import org.thinkingstudio.rubidium_toolkit.features.TotalDarkness.LightmapAccess;
import org.thinkingstudio.rubidium_toolkit.features.TotalDarkness.TextureAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightTexture.class)
public class MixinLightmapTextureManager implements LightmapAccess
{
	@Final
	@Shadow
	private DynamicTexture lightTexture;
	@Shadow
	private float blockLightRedFlicker;
	@Shadow
	private boolean updateLightTexture;

	@Inject(method = "<init>*", at = @At(value = "RETURN"))
	private void afterInit(GameRenderer gameRenderer, Minecraft minecraftClient, CallbackInfo ci) {
		((TextureAccess) lightTexture).darkness_enableUploadHook();
	}

	@Override
	public float darkness_prevFlicker() {
		return blockLightRedFlicker;
	}

	@Override
	public boolean darkness_isDirty() {
		return updateLightTexture;
	}
}
