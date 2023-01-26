package org.thinkingstudio.rubidium_toolkit.mixins.zoom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.SmoothUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.Zoom.ZoomUtils;
import org.thinkingstudio.rubidium_toolkit.keybinds.KeyboardInput;


//This mixin is responsible for the mouse-behavior-changing part of the zoom.
@Mixin(Mouse.class)
public class MouseMixin {
	@Final
	@Shadow
	private MinecraftClient minecraft;
	
	@Final
	@Shadow
	private final SmoothUtil smoothTurnX = new SmoothUtil();
	
	@Final
	@Shadow
	private final SmoothUtil smoothTurnY = new SmoothUtil();

	@Shadow
	private double accumulatedDX;

	@Shadow
	private double accumulatedDY;
	
	@Shadow
	private double accumulatedScroll;
	
	@Unique
	private final SmoothUtil cursorXZoomSmoother = new SmoothUtil();

	@Unique
	private final SmoothUtil cursorYZoomSmoother = new SmoothUtil();

	@Unique
	private double extractedE;
	@Unique
	private double adjustedG;
	
	//This mixin handles the "Reduce Sensitivity" option and extracts the g variable for the cinematic cameras.
	@ModifyVariable(
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/Mouse;minecraft:Lnet/minecraft/client/MinecraftClient;", ordinal = 2),
		method = "updateMouse",
		ordinal = 2
	)
	private double applyReduceSensitivity(double g) {
		double modifiedMouseSensitivity = this.minecraft.options.mouseSensitivity;

		if (RubidiumToolkitConfig.lowerZoomSensitivity.get())
		{
			if (!RubidiumToolkitConfig.zoomTransition.get().equals(RubidiumToolkitConfig.ZoomTransitionOptions.OFF.toString())) {
				modifiedMouseSensitivity *= ZoomUtils.zoomFovMultiplier;
			} else if (ZoomUtils.zoomState) {
				modifiedMouseSensitivity /= ZoomUtils.zoomDivisor;
			}
		}

		double appliedMouseSensitivity = modifiedMouseSensitivity * 0.6 + 0.2;
		g = appliedMouseSensitivity * appliedMouseSensitivity * appliedMouseSensitivity * 8.0;
		this.adjustedG = g;
		return g;
	}
	
	//Extracts the e variable for the cinematic cameras.
	@Inject(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Mouse;isCursorLocked()Z"),
		method = "updateMouse",
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void obtainCinematicCameraValues(CallbackInfo info, double d, double e) {
		this.extractedE = e;
	}

	//Applies the cinematic camera on the mouse's X.
	@ModifyVariable(
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/Mouse;cursorDeltaX:D", ordinal = 2, shift = At.Shift.BEFORE),
		method = "updateMouse",
		ordinal = 2
	)
	private double applyCinematicModeX(double l) {
		if (!RubidiumToolkitConfig.cinematicCameraMode.get().equals(RubidiumToolkitConfig.CinematicCameraOptions.OFF.toString())) {
			if (ZoomUtils.zoomState) {
				if (this.minecraft.options.smoothCameraEnabled) {
					l = this.smoothTurnX.smooth(this.accumulatedDX * this.adjustedG, (this.extractedE * this.adjustedG));
					this.cursorXZoomSmoother.clear();
				} else {
					l = this.cursorXZoomSmoother.smooth(this.accumulatedDX * this.adjustedG, (this.extractedE * this.adjustedG));
				}
				if (RubidiumToolkitConfig.cinematicCameraMode.get().equals(RubidiumToolkitConfig.CinematicCameraOptions.MULTIPLIED.toString())) {
					l *= RubidiumToolkitConfig.zoomValues.cinematicMultiplier;
				}
			} else {
				this.cursorXZoomSmoother.clear();
			}
		}
		
		return l;
	}
	
	//Applies the cinematic camera on the mouse's Y.
	@ModifyVariable(
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/Mouse;cursorDeltaY:D", ordinal = 2, shift = At.Shift.BEFORE),
		method = "updateMouse",
		ordinal = 2
	)
	private double applyCinematicModeY(double m) {
		if (!RubidiumToolkitConfig.cinematicCameraMode.get().equals(RubidiumToolkitConfig.CinematicCameraOptions.OFF.toString())) {
			if (ZoomUtils.zoomState) {
				if (this.minecraft.options.smoothCameraEnabled) {
					m = this.smoothTurnY.smooth(this.accumulatedDY * this.adjustedG, (this.extractedE * this.adjustedG));
					this.cursorYZoomSmoother.clear();
				} else {
					m = this.cursorYZoomSmoother.smooth(this.accumulatedDY * this.adjustedG, (this.extractedE * this.adjustedG));
				}
				if (RubidiumToolkitConfig.cinematicCameraMode.get().equals(RubidiumToolkitConfig.CinematicCameraOptions.MULTIPLIED.toString())) {
					m *= RubidiumToolkitConfig.zoomValues.cinematicMultiplier;
				}
			} else {
				this.cursorYZoomSmoother.clear();
			}
		}
		
		return m;
	}
	
	//Handles zoom scrolling.
	@Inject(
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/Mouse;eventDeltaWheel:D", ordinal = 7),
		method = "onMouseScroll",
		cancellable = true
	)
	private void zoomerOnMouseScroll(CallbackInfo info) {
		if (this.accumulatedScroll != 0.0) {
			if (RubidiumToolkitConfig.zoomScrolling.get()) {
				if (RubidiumToolkitConfig.zoomMode.get().equals(RubidiumToolkitConfig.ZoomModes.PERSISTENT.toString())) {
					if (!KeyboardInput.zoomKey.isPressed())
					{
						return;
					}
				}
				
				if (ZoomUtils.zoomState) {
					if (this.accumulatedScroll > 0.0) {
						ZoomUtils.changeZoomDivisor(true);
					} else if (this.accumulatedScroll < 0.0) {
						ZoomUtils.changeZoomDivisor(false);
					}
					
					info.cancel();
				}
			}
		}
	}

	//Handles the zoom scrolling reset through the middle button.
	@Inject(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;set(Lnet/minecraft/client/util/InputMappings$Input;Z)V"),
			method = "onMouseButton(JIII)V",
			cancellable = true,
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void zoomerOnMouseButton(long window, int button, int action, int mods, CallbackInfo info, boolean bl, int i) {
		if (RubidiumToolkitConfig.zoomScrolling.get()) {
			if (RubidiumToolkitConfig.zoomMode.get().equals(RubidiumToolkitConfig.ZoomModes.PERSISTENT.toString())) {
				if (!KeyboardInput.zoomKey.isPressed()) {
					return;
				}
			}

			if (button == 2 && bl) {
				if (KeyboardInput.zoomKey.isPressed()) {
					ZoomUtils.resetZoomDivisor();
					info.cancel();
				}
			}
		}
	}
}