package org.thinkingstudio.rubidium_toolkit.mixins.TotalDarkness;

import net.minecraft.client.render.SkyProperties;
import net.minecraft.client.util.math.Vector3d;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.TotalDarkness.Darkness;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkyProperties.Nether.class)
public class MixinTheNetherDimension {

	@Inject(method = "getBrightnessDependentFogColor", at = @At(value = "RETURN"), cancellable = true)
	private void onAdjustSkyColor(CallbackInfoReturnable<Vector3d> ci) {
		if (!RubidiumToolkitConfig.trueDarknessEnabled.get())
			return;

		if (!RubidiumToolkitConfig.darkNether.get())
			return;

		final double factor = Darkness.darkNetherFog();

		Darkness.getDarkenedFogColor(ci, factor);
	}
}
