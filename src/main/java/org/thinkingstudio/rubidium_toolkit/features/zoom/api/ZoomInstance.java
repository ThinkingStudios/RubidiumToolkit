package org.thinkingstudio.rubidium_toolkit.features.zoom.api;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/**
 * The zoom instance is essentially the zoom. It contains all the values and sub-instances required to zoom.
 */
@SuppressWarnings("unused")
public interface ZoomInstance {

    /**
     * Obtains the ID of this zoom instance.
     *
     * @return This zoom instance's ID.
     */
    ResourceLocation getInstanceId();

    /**
     * Gets the zoom instance's zoom state.
     * This is used to check if this instance's sub-instances should be active or not.
     *
     * @return The current zoom state.
     */
    boolean getZoom();

    /**
     * Sets the zoom instance's zoom state.
     *
     * @param newZoom The new zoom state.
     * @return the zoom state with the new value
     */
    boolean setZoom(boolean newZoom);

    /**
     * Gets the zoom instance's current zoom divisor.
     * NOTE: This isn't the same as the transition mode's internal multiplier.
     *
     * @return The current zoom divisor.
     */
    double getZoomDivisor();

    /**
     * Sets the zoom instance's current zoom divisor.
     *
     * @param newDivisor The new zoom divisor.
     * @return The zoom divisor with the new value.
     */
    double setZoomDivisor(double newDivisor);

    /**
     * Sets the instance's zoom divisor to the default zoom divisor.
     *
     * @return The zoom divisor with the new value.
     */
    double resetZoomDivisor();

    /**
     * Gets the instance's default zoom divisor.
     * This is used as the initial zoom divisor and as the value used on {@link #resetZoomDivisor()}.
     *
     * @return The default zoom divisor.
     */
    double getDefaultZoomDivisor();

    /**
     * Sets the instance's default zoom divisor.
     *
     * @param newDivisor The new default zoom divisor.
     * @return The default zoom divisor with the new value.
     */
    double setDefaultZoomDivisor(double newDivisor);

    /**
     * Gets the instance's transition mode.
     *
     * @return The transition mode.
     */
    TransitionMode getTransitionMode();

    /**
     * Sets the instance's transition mode.
     *
     * @param transition The new transition mode.
     * @return The transition mode with the new mode.
     */
    TransitionMode setTransitionMode(TransitionMode transition);

    /**
     * Gets the active state from the instance's transtion mode.
     *
     * @return The transition mode's active state.
     */
    boolean isTransitionActive();

    /**
     * Gets the instance's mouse modifier.
     *
     * @return The mouse modifier.
     */
    @Nullable MouseModifier getMouseModifier();

    /**
     * Sets the mouse modifier.
     *
     * @param modifier The new mouse modifier.
     * @return The mouse modifier with the new modifier.
     */
    MouseModifier setMouseModifier(MouseModifier modifier);

    /**
     * Gets the mouse modifier's active state.
     *
     * @return The mouse modifier's active state. If the modifier's null, return {@code false}.
     */
    boolean isModifierActive();

    /**
     * Gets the instance's zoom overlay.
     *
     * @return The zoom overlay.
     */
    @Nullable
    ZoomOverlay getZoomOverlay();

    /**
     * Sets the zoom overlay.
     *
     * @param overlay The new zoom overlay.
     * @return The zoom overlay with the new overlay.
     */
    ZoomOverlay setZoomOverlay(ZoomOverlay overlay);

    /**
     * Gets the zoom overlay's active state.
     *
     * @return The zoom overlay's active state. If the overlay's null, return {@code false}.
     */
    boolean isOverlayActive();
}