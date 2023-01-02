package org.thinkingstudio.rubidium_toolkit.features.zoom.events;

import org.thinkingstudio.rubidium_toolkit.features.zoom.ZoomKeyBinds;
import org.thinkingstudio.rubidium_toolkit.config.ClientConfig;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.RubidiumToolkitNetwork;
import org.thinkingstudio.rubidium_toolkit.features.zoom.utils.ZoomUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import org.thinkingstudio.rubidium_toolkit.config.ConfigEnums;

// This event is responsible for managing the zoom signal.
public class ManageZoomEvent {
	// Used internally in order to make zoom toggling possible
	private static boolean lastZooming = false;

	// Used internally in order to make persistent zoom less buggy
	private static boolean persistentZoom = false;

	private static boolean doSpyglassSound = false;

	public static void endTick(Minecraft client) {
		// We need the player for spyglass shenanigans
		if (client.player == null) return;

		// If zoom is disabled, do not allow for zooming at all
		boolean disableZoom = RubidiumToolkitNetwork.getDisableZoom() ||
			(switch (RubidiumToolkitNetwork.getSpyglassDependency()) {
				case REQUIRE_ITEM, BOTH -> true;
				default -> false;
			} && !client.player.getInventory().contains(ZoomUtils.ZOOM_DEPENDENCIES_TAG));

		if (disableZoom) {
			ZoomUtils.ZOOMER_ZOOM.setZoom(false);
			ZoomUtils.resetZoomDivisor(false);
			lastZooming = false;
			return;
		}

		// Handle zoom mode changes.
		if (!ClientConfig.ZOOM_MODE.get().equals(ConfigEnums.ZoomModes.HOLD)) {
			if (!persistentZoom) {
				persistentZoom = true;
				lastZooming = true;
				ZoomUtils.ZOOMER_ZOOM.resetZoomDivisor();
			}
		} else {
			if (persistentZoom) {
				persistentZoom = false;
				lastZooming = true;
			}
		}

		// Gathers all variables about if the press was with zoom key or with the spyglass
		boolean isUsingSpyglass = switch (RubidiumToolkitNetwork.getSpyglassDependency()) {
			case REPLACE_ZOOM, BOTH -> true;
			default -> false;
		};
		boolean keyPress = ZoomKeyBinds.ZOOM_KEY.isDown();
		boolean spyglassUse = client.player.isScoping();
		boolean zooming = keyPress || (isUsingSpyglass && spyglassUse);

		// If the press state is the same as the previous tick's, cancel the rest
		// This makes toggling usable and the zoom divisor adjustable
		if (zooming == lastZooming) return;

		doSpyglassSound = ClientConfig.USE_SPYGLASS_SOUNDS.get();

		switch (ClientConfig.ZOOM_MODE.get()) {
			case HOLD -> {
				// If the zoom needs to be held, then the zoom signal is determined by if the key is pressed or not
				ZoomUtils.ZOOMER_ZOOM.setZoom(zooming);
				ZoomUtils.resetZoomDivisor(false);
			}
			case TOGGLE -> {
				// If the zoom needs to be toggled, toggle the zoom signal instead
				if (zooming) {
					ZoomUtils.ZOOMER_ZOOM.setZoom(!ZoomUtils.ZOOMER_ZOOM.getZoom());
					ZoomUtils.resetZoomDivisor(false);
				} else {
					doSpyglassSound = false;
				}
			}
			case PERSISTENT -> {
				// If persistent zoom is enabled, just keep the zoom on
				ZoomUtils.ZOOMER_ZOOM.setZoom(true);
				ZoomUtils.keepZoomStepsWithinBounds();
			}
		}

		if (client.player != null && doSpyglassSound && !spyglassUse) {
			boolean soundDirection = !ClientConfig.ZOOM_MODE.get().equals(ConfigEnums.ZoomModes.PERSISTENT)
				? ZoomUtils.ZOOMER_ZOOM.getZoom()
				: keyPress;

			client.player.playSound(soundDirection ? SoundEvents.SPYGLASS_USE : SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);
		}

		// Set the previous zoom signal for the next tick
		lastZooming = zooming;
	}
}
