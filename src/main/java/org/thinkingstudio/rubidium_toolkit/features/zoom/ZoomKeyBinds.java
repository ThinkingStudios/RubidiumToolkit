package org.thinkingstudio.rubidium_toolkit.features.zoom;

import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfigClient;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

// Manages the zoom keybinds themselves
public class ZoomKeyBinds {
	// The "zoom" category
	public static final String ZOOM_CATEGORY = "key.rubidium_toolkit.category";

	// The zoom key bind, which will be registered
	public static final KeyMapping ZOOM_KEY = new KeyMapping("key.rubidium_toolkit.zoom", GLFW.GLFW_KEY_C, ZOOM_CATEGORY);

	// The "Decrease zoom" key bind
	public static final KeyMapping DECREASE_ZOOM_KEY = getExtraKeyBind("key.rubidium_toolkit.decrease_zoom");

	// The "Increase zoom" key bind
	public static final KeyMapping INCREASE_ZOOM_KEY = getExtraKeyBind("key.rubidium_toolkit.increase_zoom");

	// The "Reset zoom" key bind
	public static final KeyMapping RESET_ZOOM_KEY = getExtraKeyBind("key.rubidium_toolkit.reset_zoom");

	// The method used to check if the zoom manipulation key binds should be disabled, can be used by other mods.
	public static boolean areExtraKeyBindsEnabled() {
		return RubidiumToolkitConfigClient.EXTRA_KEY_BINDS.get();
	}

	// The method used to get the extra keybinds, if disabled, return null.
	public static KeyMapping getExtraKeyBind(String translationKey) {
		if (areExtraKeyBindsEnabled()) {
			return new KeyMapping(translationKey, InputConstants.UNKNOWN.getValue(), ZOOM_CATEGORY);
		}
		return null;
	}
}
