package org.thinkingstudio.rubidium_toolkit.mixins.zoom;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.thinkingstudio.rubidium_toolkit.config.ConfigEnum;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.zoom.ZoomUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This mixin is responsible for managing the fov-changing part of the zoom.
@Mixin(GameRenderer.class)
public class GameRendererMixin {
	//The zoom overlay's texture identifier.
	@Unique
	private static final Identifier ZOOM_OVERLAY = new Identifier("rubidium_toolkit:textures/misc/zoom/zoom_overlay.png");

	@Final
	@Shadow
	private MinecraftClient client;

	//Handle transitioned zoom FOV multiplier and zoom overlay alphas each tick.
	@Inject(
		at = @At("HEAD"),
		method = "tick()V"
	)
	private void zoomTick(CallbackInfo info) {
		//If zoom transitions are enabled, update the zoom FOV multiplier.
		if (!ToolkitConfig.zoomTransition.get().equals(ConfigEnum.ZoomTransitionOptions.OFF.toString())) {
			ZoomUtils.updateZoomFovMultiplier();
		}

		//If the zoom overlay is enabled, update the zoom overlay alpha.
		if (ToolkitConfig.zoomOverlay.get()) {
			ZoomUtils.updateZoomOverlayAlpha();
		}
	}
	
	//Handles zooming of both modes (Transitionless and with Smooth Transitions).
	@Inject(at = @At("RETURN"), method = "getFov", cancellable = true)
	private void getZoomedFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> info) {
		double fov = info.getReturnValue();

		if (!ToolkitConfig.zoomTransition.get().equals(ConfigEnum.ZoomTransitionOptions.OFF.toString())) {
			//Handle the zoom with smooth transitions enabled.
			if (ZoomUtils.zoomFovMultiplier != 1.0F) {
				fov *= MathHelper.lerp(tickDelta, ZoomUtils.lastZoomFovMultiplier, ZoomUtils.zoomFovMultiplier);
				info.setReturnValue(fov);
			}
		} else {
			//Handle the zoom without smooth transitions.
			if (ZoomUtils.zoomState) {
				double zoomedFov = fov / ZoomUtils.zoomDivisor;
				info.setReturnValue(zoomedFov);
			}
		}

		//Regardless of the mode, if the zoom is over, update the terrain in order to stop terrain glitches.
		if (ZoomUtils.lastZoomState) {
			if (changingFov) {
				this.client.worldRenderer.scheduleTerrainUpdate();
			}
		}
	}

	//This applies the zoom overlay itself.
	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;hudHidden:Z"), method = "render(FJZ)V")
	public void injectZoomOverlay(float tickDelta, long startTime, boolean tick, CallbackInfo info) {
		if (ToolkitConfig.zoomOverlay.get()) {
			if (this.client.options.hudHidden) {
				return;
			}

			RenderSystem.defaultAlphaFunc();
			RenderSystem.enableBlend();
			//If zoom transitions is on, apply the transition to the overlay.
			if (!ToolkitConfig.zoomTransition.get().equals(ConfigEnum.ZoomTransitionOptions.OFF.toString())) {
				if (ZoomUtils.zoomFovMultiplier != 0.0F) {
					float transparency = MathHelper.lerp(tickDelta, ZoomUtils.lastZoomOverlayAlpha, ZoomUtils.zoomOverlayAlpha);
					this.renderZoomOverlay(transparency);
				}
			} else {
				//Else, just do a simple toggle on the overlay.
				if (ZoomUtils.zoomState) {
					this.renderZoomOverlay(1.0F);
				}
			}
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
     	 	RenderSystem.enableAlphaTest();
			RenderSystem.clear(256, MinecraftClient.IS_SYSTEM_MAC);
		}
	}

	//This renders the zoom overlay.
	@Unique
	public void renderZoomOverlay(float f) {
		RenderSystem.disableAlphaTest();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, f);

		this.client.getTextureManager().bindTexture(ZOOM_OVERLAY);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);

		bufferBuilder.vertex(0.0D, this.client.getWindow().getScaledHeight(), -90.0D).texture(0.0F, 1.0F).next();
		bufferBuilder.vertex(this.client.getWindow().getScaledWidth(), this.client.getWindow().getScaledHeight(), -90.0D).texture(1.0F, 1.0F).next();
		bufferBuilder.vertex((double)this.client.getWindow().getScaledWidth(), 0.0D, -90.0D).texture(1.0F, 0.0F).next();
		bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();

		tessellator.draw();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
