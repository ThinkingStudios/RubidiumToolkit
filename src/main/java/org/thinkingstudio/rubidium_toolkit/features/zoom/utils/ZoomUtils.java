package org.thinkingstudio.rubidium_toolkit.features.zoom.utils;

import org.thinkingstudio.rubidium_toolkit.features.zoom.api.OkZoomerAPI;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.ZoomInstance;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.modifiers.ZoomDivisorMouseModifier;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.transitions.SmoothTransitionMode;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.RubidiumToolkitNetwork;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfigClient;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// The class that contains most of the logic behind the zoom itself
public class ZoomUtils {
    // The logger, used everywhere to print messages to the console
    public static final Logger LOGGER = LoggerFactory.getLogger("Ok Zoomer");

    public static final ZoomInstance ZOOMER_ZOOM = OkZoomerAPI.INSTANCE.registerZoom(OkZoomerAPI.INSTANCE.createZoomInstance(
            new ResourceLocation("rubidium_toolkit:zoom"),
            4.0F,
            new SmoothTransitionMode(0.75f),
            new ZoomDivisorMouseModifier(),
            null
    ));

    public static final TagKey<Item> ZOOM_DEPENDENCIES_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(OkZoomerAPI.MOD_ID, "zoom_dependencies"));

    public static int zoomStep = 0;

    // The method used for changing the zoom divisor, used by zoom scrolling and the key binds
    public static void changeZoomDivisor(boolean increase) {
        //If the zoom is disabled, don't allow for zoom scrolling
        if (RubidiumToolkitNetwork.getDisableZoom() || RubidiumToolkitNetwork.getDisableZoomScrolling()) {
            return;
        }

        double zoomDivisor = RubidiumToolkitConfigClient.ZOOM_DIVISOR.get();
        double minimumZoomDivisor = RubidiumToolkitConfigClient.MINIMUM_ZOOM_DIVISOR.get();
        double maximumZoomDivisor = RubidiumToolkitConfigClient.MAXIMUM_ZOOM_DIVISOR.get();
        int upperScrollStep = RubidiumToolkitConfigClient.UPPER_SCROLL_STEPS.get();
        int lowerScrollStep = RubidiumToolkitConfigClient.LOWER_SCROLL_STEPS.get();

        if (RubidiumToolkitNetwork.getForceZoomDivisors()) {
            double packetMinimumZoomDivisor = RubidiumToolkitNetwork.getMinimumZoomDivisor();
            double packetMaximumZoomDivisor = RubidiumToolkitNetwork.getMaximumZoomDivisor();

            if (packetMinimumZoomDivisor < minimumZoomDivisor) {
                minimumZoomDivisor = packetMinimumZoomDivisor;
            }

            if (packetMaximumZoomDivisor > maximumZoomDivisor) {
                maximumZoomDivisor = packetMaximumZoomDivisor;
            }
        }

        if (increase) {
            zoomStep = Math.min(zoomStep + 1, upperScrollStep);
        } else {
            zoomStep = Math.max(zoomStep - 1, -lowerScrollStep);
        }

        if (zoomStep > 0) {
            ZOOMER_ZOOM.setZoomDivisor(zoomDivisor + ((maximumZoomDivisor - zoomDivisor) / upperScrollStep * zoomStep));
        } else if (zoomStep == 0) {
            ZOOMER_ZOOM.setZoomDivisor(zoomDivisor);
        } else {
            ZOOMER_ZOOM.setZoomDivisor(zoomDivisor + ((minimumZoomDivisor - zoomDivisor) / lowerScrollStep * -zoomStep));
        }
    }

    // The method used by both the "Reset zoom" keybind and the "Reset zoom With Mouse" tweak
    public static void resetZoomDivisor(boolean userPrompted) {
        if (userPrompted && (RubidiumToolkitNetwork.getDisableZoom() || RubidiumToolkitNetwork.getDisableZoomScrolling())) {
            return;
        }

        ZOOMER_ZOOM.resetZoomDivisor();
        zoomStep = 0;
    }

    public static void keepZoomStepsWithinBounds() {
        int upperScrollStep = RubidiumToolkitConfigClient.UPPER_SCROLL_STEPS.get();
        int lowerScrollStep = RubidiumToolkitConfigClient.LOWER_SCROLL_STEPS.get();

        zoomStep = Mth.clamp(zoomStep, -lowerScrollStep, upperScrollStep);
    }

}
