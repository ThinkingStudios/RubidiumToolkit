package org.thinkingstudio.rubidium_toolkit.mixins.totaldarkness;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import org.thinkingstudio.rubidium_toolkit.features.TotalDarkness.Darkness;
import org.thinkingstudio.rubidium_toolkit.features.TotalDarkness.TextureAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NativeImageBackedTexture.class)
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
					final int color = Darkness.darken(img.getPixelColor(b, s), b, s);
					img.setPixelColor(b, s, color);
				}
			}
		}
	}

	@Override
	public void darkness_enableUploadHook() {
		enableHook = true;
	}
}
