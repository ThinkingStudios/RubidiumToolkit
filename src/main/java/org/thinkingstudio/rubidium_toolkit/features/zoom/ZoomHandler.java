package org.thinkingstudio.rubidium_toolkit.features.zoom;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.thinkingstudio.rubidium_toolkit.RubidiumToolkit;
import org.thinkingstudio.rubidium_toolkit.config.ConfigEnum;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.keybinds.KeyboardInput;

@Mod.EventBusSubscriber(modid = RubidiumToolkit.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ZoomHandler {
    //Used internally in order to make zoom toggling possible.
    private static boolean lastZoomPress = false;

    //Used internally in order to make persistent zoom less buggy.
    private static boolean persistentZoom = false;

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        //Handle zoom mode changes.
        if (!ToolkitConfig.zoomMode.get().equals(ConfigEnum.ZoomModes.HOLD.toString())) {
            if (!persistentZoom) {
                persistentZoom = true;
                lastZoomPress = true;
                ZoomUtils.zoomDivisor = ToolkitConfig.zoomValues.zoomDivisor;
            }
        } else {
            if (persistentZoom) {
                persistentZoom = false;
                lastZoomPress = true;
            }
        }

        //If the press state is the same as the previous tick's, cancel the rest. Makes toggling usable and the zoom divisor adjustable.
        if (KeyboardInput.zoomKey.isDown() == lastZoomPress) {
            return;
        }

        if (ToolkitConfig.zoomMode.get().equals(ConfigEnum.ZoomModes.HOLD.toString()))
        {
            //If the zoom needs to be held, then the zoom signal is determined by if the key is pressed or not.
            ZoomUtils.zoomState = KeyboardInput.zoomKey.isDown();
            ZoomUtils.zoomDivisor = ToolkitConfig.zoomValues.zoomDivisor;
        }
        else if (ToolkitConfig.zoomMode.get().equals(ConfigEnum.ZoomModes.TOGGLE.toString()))
        {
            //If the zoom needs to be toggled, toggle the zoom signal instead.
            if (KeyboardInput.zoomKey.isDown()) {
                ZoomUtils.zoomState = !ZoomUtils.zoomState;
                ZoomUtils.zoomDivisor = ToolkitConfig.zoomValues.zoomDivisor;
            }
        }
        else if (ToolkitConfig.zoomMode.get().equals(ConfigEnum.ZoomModes.PERSISTENT.toString()))
        {
            //If persistent zoom is enabled, just keep the zoom on.
            ZoomUtils.zoomState = true;
        }

        //Manage the post-zoom signal.
        ZoomUtils.lastZoomState = !ZoomUtils.zoomState && lastZoomPress;

        //Set the previous zoom signal for the next tick.
        lastZoomPress = KeyboardInput.zoomKey.isDown();
    }
}