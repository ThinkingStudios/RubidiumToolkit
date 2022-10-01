package com.texstudio.rubidium_toolkit.mixins.TotalDarkness;

import com.texstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import com.texstudio.rubidium_toolkit.features.TotalDarkness.Darkness;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionRenderInfo.End.class)
public class MixinTheEndDimension {

	@Inject(method = "getBrightnessDependentFogColor", at = @At(value = "RETURN"), cancellable = true)
	private void onAdjustSkyColor(CallbackInfoReturnable<Vector3d> ci) {
		if (!RubidiumToolkitConfig.trueDarknessEnabled.get())
			return;

		if (!RubidiumToolkitConfig.darkEnd.get())
			return;

		final double factor = Darkness.darkEndFog();

		Darkness.getDarkenedFogColor(ci, factor);
	}
}
