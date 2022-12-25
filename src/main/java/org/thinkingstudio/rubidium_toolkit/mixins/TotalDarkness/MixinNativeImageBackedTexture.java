package org.thinkingstudio.rubidium_toolkit.mixins.TotalDarkness;

import org.thinkingstudio.rubidium_toolkit.features.TotalDarkness.Darkness;
import org.thinkingstudio.rubidium_toolkit.features.TotalDarkness.TextureAccess;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DynamicTexture.class)
public class MixinNativeImageBackedTexture implements TextureAccess
{
	@Shadow
	private NativeImage pixels;

	private boolean enableHook = false;

	@Inject(method = "upload", at = @At(value = "HEAD"))
	private void onRenderWorld(CallbackInfo ci) {
		if (enableHook && Darkness.enabled) {
			final NativeImage img = pixels;
			for (int b = 0; b < 16; b++) {
				for (int s = 0; s < 16; s++) {
					final int color = Darkness.darken(img.getPixelRGBA(b, s), b, s);
					img.setPixelRGBA(b, s, color);
				}
			}
		}
	}

	@Override
	public void darkness_enableUploadHook() {
		enableHook = true;
	}
}
