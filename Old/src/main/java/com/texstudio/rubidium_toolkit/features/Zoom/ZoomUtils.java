package com.texstudio.rubidium_toolkit.features.Zoom;

import com.texstudio.rubidium_toolkit.config.RubidiumToolkitConfig;

//The class that contains most of the logic behind the zoom itself.
public class ZoomUtils {
    //The zoom signal, which is managed in an event and used by other mixins.
	public static boolean zoomState = false;

	//Used for post-zoom actions like updating the terrain.
	public static boolean lastZoomState = false;

	//The zoom divisor, managed by the zoom press and zoom scrolling. Used by other mixins.
	public static double zoomDivisor = RubidiumToolkitConfig.zoomValues.zoomDivisor;

	//The zoom FOV multipliers. Used by the GameRenderer mixin.
	public static float zoomFovMultiplier = 1.0F;
	public static float lastZoomFovMultiplier = 1.0F;

	//The zoom overlay's alpha. Used by the InGameHud mixin.
	public static float zoomOverlayAlpha = 0.0F;
	public static float lastZoomOverlayAlpha = 0.0F;

    //The method used for changing the zoom divisor, used by zoom scrolling and the keybinds.
	public static void changeZoomDivisor(boolean increase) {
		double changedZoomDivisor;
		double lesserChangedZoomDivisor;

		if (increase) {
			changedZoomDivisor = zoomDivisor + RubidiumToolkitConfig.zoomValues.scrollStep;
			lesserChangedZoomDivisor = zoomDivisor + RubidiumToolkitConfig.zoomValues.lesserScrollStep;
		} else {
			changedZoomDivisor = zoomDivisor - RubidiumToolkitConfig.zoomValues.scrollStep;
			lesserChangedZoomDivisor = zoomDivisor - RubidiumToolkitConfig.zoomValues.lesserScrollStep;
			lastZoomState = true;
		}

		if (lesserChangedZoomDivisor <= RubidiumToolkitConfig.zoomValues.zoomDivisor) {
			changedZoomDivisor = lesserChangedZoomDivisor;
		}

		if (changedZoomDivisor >= RubidiumToolkitConfig.zoomValues.minimumZoomDivisor) {
			if (changedZoomDivisor <= RubidiumToolkitConfig.zoomValues.maximumZoomDivisor) {
				zoomDivisor = changedZoomDivisor;
			}
		}
	}

	//The method used by both the "Reset zoom" keybind and the "Reset zoom With Mouse" tweak.
	public static void resetZoomDivisor() {
		zoomDivisor = RubidiumToolkitConfig.zoomValues.zoomDivisor;
		lastZoomState = true;
	}

	//The method used for unbinding the "Save Toolbar Activator"
//	public static void unbindConflictingKey(Minecraft client, boolean userPrompted) {
//		if (ZoomKeybinds.zoomKey.isDefault()) {
//			if (client.options.keySaveHotbarActivator.isDefault()) {
//				if (userPrompted) {
//					MagnesiumExtras.LOGGER.info("[Ok Zoomer] The \"Save Toolbar Activator\" keybind was occupying C! Unbinding...");
//				} else {
//					MagnesiumExtras.LOGGER.info("[Ok Zoomer] The \"Save Toolbar Activator\" keybind was occupying C! Unbinding... This process won't be repeated until specified in the config.");
//				}
//				client.options.keySaveToolbarActivator.setBoundKey(InputUtil.UNKNOWN_KEY);
//				client.options.write();
//				KeyBinding.updateKeysByCode();
//			} else {
//				MagnesiumExtras.LOGGER.info("[Ok Zoomer] No conflicts with the \"Save Toolbar Activator\" keybind was found!");
//			}
//		}
//	}

	//The equivalent of GameRenderer's updateFovMultiplier but for zooming. Used by zoom transitions.
	public static void updateZoomFovMultiplier() {
		float zoomMultiplier = 1.0F;
		double dividedZoomMultiplier = 1.0 / ZoomUtils.zoomDivisor;

		if (ZoomUtils.zoomState) {
			zoomMultiplier = (float)dividedZoomMultiplier;
		}

		lastZoomFovMultiplier = zoomFovMultiplier;
		
		if (RubidiumToolkitConfig.zoomTransition.get().equals(RubidiumToolkitConfig.ZoomTransitionOptions.SMOOTH.toString())) {
			zoomFovMultiplier += (zoomMultiplier - zoomFovMultiplier) * RubidiumToolkitConfig.zoomValues.smoothMultiplier;
		}
	}

	//Handles the zoom overlay transparency with transitions. Used by zoom overlay.
	public static void updateZoomOverlayAlpha() {
		float zoomMultiplier = 0.0F;

		if (ZoomUtils.zoomState) {
			zoomMultiplier = 1.0F;
		}

		lastZoomOverlayAlpha = zoomOverlayAlpha;

		if (RubidiumToolkitConfig.zoomTransition.get().equals(RubidiumToolkitConfig.ZoomTransitionOptions.SMOOTH.toString())) {
			zoomOverlayAlpha += (zoomMultiplier - zoomOverlayAlpha) * RubidiumToolkitConfig.zoomValues.smoothMultiplier;
		}
	}
}