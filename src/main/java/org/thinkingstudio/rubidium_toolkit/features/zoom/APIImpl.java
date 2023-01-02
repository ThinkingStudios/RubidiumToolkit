package org.thinkingstudio.rubidium_toolkit.features.zoom;

import com.google.auto.service.AutoService;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.MouseModifier;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.OkZoomerAPI;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.TransitionMode;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.ZoomInstance;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.ZoomOverlay;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.transitions.InstantTransitionMode;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@AutoService(OkZoomerAPI.class)
public class APIImpl implements OkZoomerAPI {
    private static final Set<ZoomInstance> zoomInstances = new ReferenceArraySet<>();
    private static boolean iterateZoom;
    private static boolean iterateTransitions;
    private static boolean iterateModifiers;
    private static boolean iterateOverlays;

    @Override
    public ZoomInstance registerZoom(ZoomInstance instance) {
        for (ZoomInstance zoomInstance : zoomInstances) {
            if (zoomInstance.getInstanceId().equals(instance.getInstanceId())) {
                throw new RuntimeException("Multiple zoom instances with the ID " + zoomInstance.getInstanceId() + " were found!");
            }
        }
        return zoomInstances.add(instance) ? instance : null;
    }

    @Override
    public ZoomInstance createZoomInstance(ResourceLocation instanceId, float defaultZoomDivisor, TransitionMode transition, @Nullable MouseModifier modifier, @Nullable ZoomOverlay overlay) {
        return new InstanceImpl(instanceId, defaultZoomDivisor, transition, modifier, overlay);
    }

    /**
     * Gets a set of all the registered zoom instances.
     *
     * @return A set of registered zoom instances.
     */
    public static Set<ZoomInstance> getZoomInstances() {
        return zoomInstances;
    }

    /**
     * Determines whenever an iteration through all active zoom instances is necessary.
     *
     * @return {@code true} if an iteration is needed, else {@code false} otherwise.
     */
    public static boolean shouldIterateZoom() {
        return iterateZoom;
    }

    /**
     * Sets the state that determines the need for an iteration through all active zoom instances.
     * This is an internal method and shouldn't be used by other mods.
     *
     * @param iterateZoom The new iteration state.
     */
    @ApiStatus.Internal
    public static void setIterateZoom(boolean iterateZoom) {
        APIImpl.iterateZoom = iterateZoom;
    }

    /**
     * Determines whenever an iteration through all zoom instances with active transitions is necessary.
     *
     * @return {@code true} if an iteration is needed, else {@code false} otherwise.
     */
    public static boolean shouldIterateTransitions() {
        return iterateTransitions;
    }

    /**
     * Sets the state that determines the need for an iteration through all zoom instances with active transitions.
     * This is an internal method and shouldn't be used by other mods.
     *
     * @param iterateTransitions The new iteration state.
     */
    @ApiStatus.Internal
    public static void setIterateTransitions(boolean iterateTransitions) {
        APIImpl.iterateTransitions = iterateTransitions;
    }

    /**
     * Determines whenever an iteration through all zoom instances with active modifiers is necessary.
     *
     * @return {@code true} if an iteration is needed, else {@code false} otherwise.
     */
    public static boolean shouldIterateModifiers() {
        return iterateModifiers;
    }

    /**
     * Sets the state that determines the need for an iteration through all zoom instances with active modifiers.
     * This is an internal method and shouldn't be used by other mods.
     *
     * @param iterateModifiers The new iteration state.
     */
    @ApiStatus.Internal
    public static void setIterateModifiers(boolean iterateModifiers) {
        APIImpl.iterateModifiers = iterateModifiers;
    }

    /**
     * Determines whenever an iteration through all zoom instances with active overlays is necessary.
     *
     * @return {@code true} if an iteration is needed, else {@code false} otherwise.
     */
    public static boolean shouldIterateOverlays() {
        return iterateOverlays;
    }

    /**
     * Sets the state that determines the need for an iteration through all zoom instances with active overlays.
     * This is an internal method and shouldn't be used by other mods.
     *
     * @param iterateOverlays The new iteration state.
     */
    @ApiStatus.Internal
    public static void setIterateOverlays(boolean iterateOverlays) {
        APIImpl.iterateOverlays = iterateOverlays;
    }

    private static final class InstanceImpl implements ZoomInstance {
        private final ResourceLocation instanceId;
        private boolean zoom;
        private double defaultZoomDivisor;
        private double zoomDivisor;
        private TransitionMode transition;
        private MouseModifier modifier;
        private ZoomOverlay overlay;

        /**
         * Initializes a zoom instance. It must be registered by the instance registry before being functional
         *
         * @param instanceId         The ID for this zoom instance.
         * @param defaultZoomDivisor The default zoom divisor. It will be this instance's initial zoom divisor value.
         * @param transition         The zoom instance's transition mode. {@link InstantTransitionMode} is used if null.
         * @param modifier           The zoom instance's mouse modifier. If null, no mouse modifier will be applied.
         * @param overlay            The zoom instance's zoom overlay. If null, no zoom overlay will be applied.
         */
        public InstanceImpl(ResourceLocation instanceId, float defaultZoomDivisor, TransitionMode transition, @Nullable MouseModifier modifier, @Nullable ZoomOverlay overlay) {
            this.instanceId = instanceId;
            this.zoom = false;
            this.defaultZoomDivisor = defaultZoomDivisor;
            this.zoomDivisor = this.defaultZoomDivisor;
            this.transition = transition == null ? new InstantTransitionMode() : transition;
            this.modifier = modifier;
            this.overlay = overlay;
        }

        @Override
        public ResourceLocation getInstanceId() {
            return instanceId;
        }

        @Override
        public boolean getZoom() {
            return zoom;
        }

        @Override
        public boolean setZoom(boolean newZoom) {
            return zoom = newZoom;
        }

        @Override
        public double getZoomDivisor() {
            return zoomDivisor;
        }

        @Override
        public double setZoomDivisor(double newDivisor) {
            return zoomDivisor = newDivisor;
        }

        @Override
        public double resetZoomDivisor() {
            return zoomDivisor = defaultZoomDivisor;
        }

        @Override
        public double getDefaultZoomDivisor() {
            return defaultZoomDivisor;
        }

        @Override
        public double setDefaultZoomDivisor(double newDivisor) {
            return defaultZoomDivisor = newDivisor;
        }

        @Override
        public TransitionMode getTransitionMode() {
            return transition;
        }

        @Override
        public TransitionMode setTransitionMode(TransitionMode transition) {
            return this.transition = transition;
        }

        @Override
        public boolean isTransitionActive() {
            return transition.getActive();
        }

        @Override
        public @Nullable MouseModifier getMouseModifier() {
            return modifier;
        }

        @Override
        public MouseModifier setMouseModifier(MouseModifier modifier) {
            return this.modifier = modifier;
        }

        @Override
        public boolean isModifierActive() {
            if (this.modifier != null) {
                return this.modifier.getActive();
            } else {
                return false;
            }
        }

        @Override
        public @Nullable ZoomOverlay getZoomOverlay() {
            return overlay;
        }

        @Override
        public ZoomOverlay setZoomOverlay(ZoomOverlay overlay) {
            return this.overlay = overlay;
        }

        @Override
        public boolean isOverlayActive() {
            if (this.overlay != null) {
                return this.overlay.getActive();
            } else {
                return false;
            }
        }
    }
}
