package com.texstudio.rubidium_toolkit.features.Zoom.events;

import com.texstudio.rubidium_toolkit.features.Zoom.ZoomKeyBinds;
import com.texstudio.rubidium_toolkit.features.Zoom.network.RubidiumToolkitNetwork;
import com.texstudio.rubidium_toolkit.features.Zoom.utils.ZoomUtils;

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
