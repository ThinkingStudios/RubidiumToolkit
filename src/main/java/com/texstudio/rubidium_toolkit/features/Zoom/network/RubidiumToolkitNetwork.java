package com.texstudio.rubidium_toolkit.features.Zoom.network;

import com.texstudio.rubidium_toolkit.features.Zoom.api.MouseModifier;
import com.texstudio.rubidium_toolkit.features.Zoom.api.OkZoomerAPI;
import com.texstudio.rubidium_toolkit.features.Zoom.api.modifiers.CinematicCameraMouseModifier;
import com.texstudio.rubidium_toolkit.features.Zoom.api.modifiers.ContainingMouseModifier;
import com.texstudio.rubidium_toolkit.features.Zoom.api.modifiers.ZoomDivisorMouseModifier;
import com.texstudio.rubidium_toolkit.features.Zoom.api.overlays.SpyglassZoomOverlay;
import com.texstudio.rubidium_toolkit.features.Zoom.api.transitions.InstantTransitionMode;
import com.texstudio.rubidium_toolkit.features.Zoom.api.transitions.SmoothTransitionMode;
import com.texstudio.rubidium_toolkit.config.ConfigEnums;
import com.texstudio.rubidium_toolkit.config.ConfigEnums.SpyglassDependency;
import com.texstudio.rubidium_toolkit.config.ClientConfig;
import com.texstudio.rubidium_toolkit.features.Zoom.utils.ZoomUtils;
import com.texstudio.rubidium_toolkit.features.Zoom.zoom.LinearTransitionMode;
import com.texstudio.rubidium_toolkit.features.Zoom.zoom.MultipliedCinematicCameraMouseModifier;
import com.texstudio.rubidium_toolkit.features.Zoom.zoom.ZoomerZoomOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.texstudio.rubidium_toolkit.config.ClientConfig.*;

/**
 * Manages the zoom packets and their signals.
 */
public class RubidiumToolkitNetwork {
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(OkZoomerAPI.MOD_ID, "network"))
            .clientAcceptedVersions(e -> true)
            .serverAcceptedVersions(e -> true)
            .networkProtocolVersion(() -> "hmm :)")
            .simpleChannel();
    public static final EventNetworkChannel EXISTENCE_CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(OkZoomerAPI.MOD_ID, "exists"))
            .clientAcceptedVersions(e -> true)
            .serverAcceptedVersions(e -> true)
            .networkProtocolVersion(() -> "Why'd I exist?!")
            .eventNetworkChannel();

    public enum Acknowledgement {
        NONE,
        HAS_RESTRICTIONS,
        HAS_NO_RESTRICTIONS
    }

    // The signals used by other parts of the zoom in order to enforce the packets
    public static boolean hasRestrictions = false;
    public static boolean disableZoom = false;
    public static boolean disableZoomScrolling = false;
    public static boolean forceClassicMode = false;
    public static boolean forceZoomDivisors = false;
    public static Acknowledgement acknowledgement = Acknowledgement.NONE;
    public static double maximumZoomDivisor = 0.0D;
    public static double minimumZoomDivisor = 0.0D;
    public static SpyglassDependency spyglassDependency = null;
    public static ConfigEnums.ZoomOverlays spyglassOverlay = ConfigEnums.ZoomOverlays.OFF;

    public static boolean getHasRestrictions() {
        return hasRestrictions;
    }

    public static Acknowledgement checkRestrictions() {
        boolean hasRestrictions = disableZoom
                || disableZoomScrolling
                || forceClassicMode
                || forceZoomDivisors
                || spyglassDependency != null
                || spyglassOverlay != ConfigEnums.ZoomOverlays.OFF;
        return RubidiumToolkitNetwork.acknowledgement = hasRestrictions ? Acknowledgement.HAS_RESTRICTIONS : Acknowledgement.HAS_NO_RESTRICTIONS;
    }

    public static boolean getDisableZoom() {
        return disableZoom;
    }

    public static boolean getDisableZoomScrolling() {
        return disableZoomScrolling;
    }

    public static boolean getForceClassicMode() {
        return forceClassicMode;
    }

    public static boolean getForceZoomDivisors() {
        return forceZoomDivisors;
    }

    public static Acknowledgement getAcknowledgement() {
        return acknowledgement;
    }

    public static double getMaximumZoomDivisor() {
        return maximumZoomDivisor;
    }

    public static double getMinimumZoomDivisor() {
        return minimumZoomDivisor;
    }

    public static SpyglassDependency getSpyglassDependency() {
        return spyglassDependency != null ? spyglassDependency : ClientConfig.SPYGLASS_DEPENDENCY.get();
    }

    public static ConfigEnums.ZoomOverlays getSpyglassOverlay() {
        return spyglassOverlay;
    }

    /**
     * The method used to reset the restrictions once left the server.
     */
    public static void resetRestrictions() {
        RubidiumToolkitNetwork.hasRestrictions = false;
        RubidiumToolkitNetwork.disableZoom = false;
        RubidiumToolkitNetwork.disableZoomScrolling = false;
        RubidiumToolkitNetwork.forceZoomDivisors = false;
        RubidiumToolkitNetwork.acknowledgement = Acknowledgement.NONE;
        RubidiumToolkitNetwork.maximumZoomDivisor = 0.0D;
        RubidiumToolkitNetwork.minimumZoomDivisor = 0.0D;
        RubidiumToolkitNetwork.spyglassDependency = null;
        if (RubidiumToolkitNetwork.forceClassicMode || RubidiumToolkitNetwork.spyglassOverlay != ConfigEnums.ZoomOverlays.OFF) {
            RubidiumToolkitNetwork.forceClassicMode = false;
            RubidiumToolkitNetwork.spyglassOverlay = ConfigEnums.ZoomOverlays.OFF;
            configureZoomInstance();
        }
    }

    public static void configureZoomInstance() {
        // Sets zoom transition
        ZoomUtils.ZOOMER_ZOOM.setTransitionMode(
                switch (ZOOM_TRANSITION.get()) {
                    case SMOOTH -> new SmoothTransitionMode((float) (double) SMOOTH_MULTIPLIER.get());
                    case LINEAR -> new LinearTransitionMode(MINIMUM_LINEAR_STEP.get(), MAXIMUM_LINEAR_STEP.get());
                    default -> new InstantTransitionMode();
                }
        );

        // Forces Classic Mode settings
        if (RubidiumToolkitNetwork.getForceClassicMode()) {
            ZoomUtils.ZOOMER_ZOOM.setDefaultZoomDivisor(4.0D);
            ZoomUtils.ZOOMER_ZOOM.setMouseModifier(new CinematicCameraMouseModifier());
            ZoomUtils.ZOOMER_ZOOM.setZoomOverlay(null);
            return;
        }

        // Sets zoom divisor
        ZoomUtils.ZOOMER_ZOOM.setDefaultZoomDivisor(ZOOM_DIVISOR.get());

        // Sets mouse modifier
        configureZoomModifier();

        // Enforce spyglass overlay if necessary
        final var overlay = spyglassOverlay == ConfigEnums.ZoomOverlays.OFF ? ZOOM_OVERLAY.get() : spyglassOverlay;

        // Sets zoom overlay
        final var overlayTextureId = new ResourceLocation(
                (USE_SPYGLASS_TEXTURE.get() || overlay == ConfigEnums.ZoomOverlays.SPYGLASS)
                        ? "textures/misc/spyglass_scope.png"
                        : OkZoomerAPI.MOD_ID + ":textures/misc/zoom_overlay.png");

        ZoomUtils.ZOOMER_ZOOM.setZoomOverlay(
                switch (overlay) {
                    case VIGNETTE -> new ZoomerZoomOverlay(overlayTextureId);
                    case SPYGLASS -> new SpyglassZoomOverlay(overlayTextureId);
                    default -> null;
                }
        );
    }

    public static void configureZoomModifier() {
        final var cinematicCamera = CINEMATIC_CAMERA.get();
        boolean reduceSensitivity = REDUCE_SENSITIVITY.get();
        if (cinematicCamera != ConfigEnums.CinematicCameraOptions.OFF) {
            MouseModifier cinematicModifier = switch (cinematicCamera) {
                case VANILLA -> new CinematicCameraMouseModifier();
                case MULTIPLIED -> new MultipliedCinematicCameraMouseModifier(CINEMATIC_MULTIPLIER.get());
                default -> null;
            };
            ZoomUtils.ZOOMER_ZOOM.setMouseModifier(reduceSensitivity
                    ? new ContainingMouseModifier(cinematicModifier, new ZoomDivisorMouseModifier())
                    : cinematicModifier
            );
        } else {
            ZoomUtils.ZOOMER_ZOOM.setMouseModifier(reduceSensitivity
                    ? new ZoomDivisorMouseModifier()
                    : null
            );
        }
    }
}
