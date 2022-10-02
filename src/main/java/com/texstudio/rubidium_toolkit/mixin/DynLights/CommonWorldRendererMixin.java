package com.texstudio.rubidium_toolkit.mixin.DynLights;

import com.texstudio.rubidium_toolkit.features.DynLights.RubidiumDynLights;
import com.texstudio.rubidium_toolkit.features.DynLights.accessor.WorldRendererAccessor;
import com.texstudio.rubidium_toolkit.features.DynLights.config.DynamicLightsConfig;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LevelRenderer.class, priority = 900)
public abstract class CommonWorldRendererMixin implements WorldRendererAccessor {

	@Invoker("setSectionDirty")
	@Override
	public abstract void dynlights_setSectionDirty(int x, int y, int z, boolean important);

	@Inject(
			method = "getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I",
			at = @At("TAIL"),
			cancellable = true
	)
	private static void onGetLightmapCoordinates(BlockAndTintGetter world, BlockState j, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if (!world.getBlockState(pos).isSolidRender(world, pos) && DynamicLightsConfig.Quality.get() != DynamicLightsConfig.QualityMode.OFF)
		{
			int vanilla = cir.getReturnValue();
			int value = RubidiumDynLights.get().getLightmapWithDynamicLight(pos, vanilla);

			cir.setReturnValue(value);
		}

	}
}
