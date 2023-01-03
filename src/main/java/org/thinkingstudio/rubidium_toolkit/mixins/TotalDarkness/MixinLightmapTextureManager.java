package org.thinkingstudio.rubidium_toolkit.mixins.TotalDarkness;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImageBackedTexture;
import org.thinkingstudio.rubidium_toolkit.features.TotalDarkness.LightmapAccess;
import org.thinkingstudio.rubidium_toolkit.features.TotalDarkness.TextureAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightmapTextureManager.class)
public class MixinLightmapTextureManager implements LightmapAccess
{
	@Final
	@Shadow
	private NativeImageBackedTexture lightTexture;
	@Shadow
	private float blockLightRedFlicker;
	@Shadow
	private boolean updateLightTexture;

	@Inject(method = "<init>*", at = @At(value = "RETURN"))
	private void afterInit(GameRenderer gameRenderer, MinecraftClient minecraftClient, CallbackInfo ci) {
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
