package org.thinkingstudio.rubidium_toolkit.features.zoom.events;

import org.thinkingstudio.rubidium_toolkit.features.zoom.ZoomKeyBinds;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.RubidiumToolkitNetwork;
import org.thinkingstudio.rubidium_toolkit.features.zoom.utils.ZoomUtils;

public class ManageKeyBindsEvent {
    public static void onTickEnd() {
        if (ZoomKeyBinds.areExtraKeyBindsEnabled() && RubidiumToolkitNetwork.getDisableZoomScrolling()) {
            if (ZoomKeyBinds.DECREASE_ZOOM_KEY.isDown() && !ZoomKeyBinds.INCREASE_ZOOM_KEY.isDown()) {
                ZoomUtils.changeZoomDivisor(false);
            }

            if (ZoomKeyBinds.INCREASE_ZOOM_KEY.isDown() && !ZoomKeyBinds.DECREASE_ZOOM_KEY.isDown()) {
                ZoomUtils.changeZoomDivisor(true);
            }

            if (ZoomKeyBinds.RESET_ZOOM_KEY.isDown()) {
                ZoomUtils.resetZoomDivisor(true);
            }
        }
    }
}
